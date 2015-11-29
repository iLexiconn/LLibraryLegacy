package net.ilexiconn.llibrary.asm;

import java.io.IOException;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import com.google.common.base.Objects;

import net.minecraft.launchwrapper.Launch;

public class ObfMapping {
    public static ObfRemapper obfMapper = new ObfRemapper();
    public static Remapper mcpMapper = null;

    public static void loadMCPRemapper() {
        if (mcpMapper == null) {
            mcpMapper = new MCPRemapper();
        }
    }

    public static final boolean obfuscated;

    static {
        boolean obf = true;
        try {
            obf = Launch.classLoader.getClassBytes("net.minecraft.world.World") == null;
        } catch (IOException ignored) {
        }
        obfuscated = obf;
        if (!obf) {
            loadMCPRemapper();
        }
    }

    public String owner;
    public String name;
    public String desc;

    public ObfMapping(String owner) {
        this(owner, "", "");
    }

    public ObfMapping(String owner, String name, String desc) {
        this.owner = owner;
        this.name = name;
        this.desc = desc;

        if (owner.contains(".")) {
            throw new IllegalArgumentException(owner);
        }
    }

    public ObfMapping(ObfMapping descmap, String subclass) {
        this(subclass, descmap.name, descmap.desc);
    }

    public ObfMapping copy() {
        return new ObfMapping(owner, name, desc);
    }

    public ObfMapping subclass(String subclass) {
        return new ObfMapping(this, subclass);
    }

    public ObfMapping map(Remapper mapper) {
        if (mapper == null) {
            return this;
        }

        if (isMethod()) {
            name = mapper.mapMethodName(owner, name, desc);
        } else if (isField()) {
            name = mapper.mapFieldName(owner, name, desc);
        }
        owner = mapper.mapType(owner);

        if (isMethod()) {
            desc = mapper.mapMethodDesc(desc);
        } else if (desc.length() > 0) {
            desc = mapper.mapDesc(desc);
        }
        return this;
    }

    public ObfMapping toRuntime() {
        map(mcpMapper);
        return this;
    }

    public ObfMapping toClassloading() {
        if (!obfuscated) {
            map(mcpMapper);
        } else if (obfMapper.isObf(owner)) {
            map(obfMapper);
        }
        return this;
    }

    @SuppressWarnings("deprecation")
    public AbstractInsnNode toInsn(int opcode) {
        if (isClass()) {
            return new TypeInsnNode(opcode, owner);
        } else if (isMethod()) {
            return new MethodInsnNode(opcode, owner, name, desc);
        } else {
            return new FieldInsnNode(opcode, owner, name, desc);
        }
    }

    public void visitTypeInsn(MethodVisitor mv, int opcode) {
        mv.visitTypeInsn(opcode, owner);
    }

    @SuppressWarnings("deprecation")
    public void visitMethodInsn(MethodVisitor mv, int opcode) {
        mv.visitMethodInsn(opcode, owner, name, desc);
    }

    public void visitFieldInsn(MethodVisitor mv, int opcode) {
        mv.visitFieldInsn(opcode, owner, name, desc);
    }

    public MethodVisitor visitMethod(ClassVisitor visitor, int access, String[] exceptions) {
        return visitor.visitMethod(access, name, desc, null, exceptions);
    }

    public FieldVisitor visitField(ClassVisitor visitor, int access, Object value) {
        return visitor.visitField(access, name, desc, null, value);
    }

    public boolean isClass(String name) {
        return name.replace('.', '/').equals(owner);
    }

    public boolean matches(String name, String desc) {
        return name.equals(name) && desc.equals(desc);
    }

    public boolean matches(MethodNode node) {
        return name.equals(node.name) && desc.equals(node.desc);
    }

    public boolean matches(MethodInsnNode node) {
        return owner.equals(node.owner) && name.equals(node.name) && desc.equals(node.desc);
    }

    public boolean matches(FieldNode node) {
        return name.equals(node.name) && desc.equals(node.desc);
    }

    public boolean matches(FieldInsnNode node) {
        return owner.equals(node.owner) && name.equals(node.name) && desc.equals(node.desc);
    }

    public boolean isClass() {
        return name.length() == 0;
    }

    public boolean isMethod() {
        return desc.contains("(");
    }

    public boolean isField() {
        return !isClass() && !isMethod();
    }

    public String javaClass() {
        return owner.replace('/', '.');
    }

    public String methodDesc() {
        return owner + "." + name + desc;
    }

    public String fieldDesc() {
        return owner + "." + name + ":" + desc;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ObfMapping)) {
            return false;
        }
        ObfMapping desc = (ObfMapping) obj;
        return owner.equals(desc.owner) && name.equals(desc.name) && desc.equals(desc.desc);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(desc, name, owner);
    }

    @Override
    public String toString() {
        if (isClass()) {
            return "[" + owner + "]";
        } else if (desc.length() == 0) {
            return "[" + owner + "." + name + "]";
        } else {
            return "[" + (isMethod() ? methodDesc() : fieldDesc()) + "]";
        }
    }

    public static ObfMapping fromDesc(String s) {
        int lastDot = s.lastIndexOf('.');
        if (lastDot < 0) {
            return new ObfMapping(s, "", "");
        }
        int sep = s.indexOf('('); // methods
        int sep_end = sep;
        if (sep < 0) {
            sep = s.indexOf(' '); // some stuffs
            sep_end = sep + 1;
        }
        if (sep < 0) {
            sep = s.indexOf(':'); // fields
            sep_end = sep + 1;
        }
        if (sep < 0) {
            return new ObfMapping(s.substring(0, lastDot), s.substring(lastDot + 1), "");
        }
        return new ObfMapping(s.substring(0, lastDot), s.substring(lastDot + 1, sep), s.substring(sep_end));
    }
}
