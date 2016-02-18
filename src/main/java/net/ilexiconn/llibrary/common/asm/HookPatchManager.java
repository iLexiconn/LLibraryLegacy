package net.ilexiconn.llibrary.common.asm;

import net.ilexiconn.llibrary.common.asm.hook.FishingPole;
import net.ilexiconn.llibrary.common.asm.mappings.Mappings;
import net.ilexiconn.llibrary.common.json.container.JsonHook;
import net.ilexiconn.llibrary.common.plugin.LLibraryPlugin;
import net.minecraft.launchwrapper.IClassTransformer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class HookPatchManager implements IClassTransformer {
    public Logger logger = LogManager.getLogger("LLibraryHooks");

    @Override
    public byte[] transform(String name, String transformedName, byte[] classBytes) {
        ClassNode classNode = ASMHelper.createClassNode(classBytes);
        for (FishingPole hook : LLibraryPlugin.hookList) {
            
        }
        classBytes = ASMHelper.createBytes(classNode, ClassWriter.COMPUTE_MAXS);
        return classBytes;
    }

    public void transform(MethodNode methodNode, JsonHook hook) {
        InsnList insnList = new InsnList();
        String desc = "(";
        if (hook.hookClass.addClassAsArg) {
            desc += "L" + hook.hookClass.name + ";";
            insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        }
        int j = 1;
        for (String s : hook.hookMethod.args) {
            if (s.equals("int")) {
                desc += "I";
                insnList.add(new VarInsnNode(Opcodes.ILOAD, j++));
            } else if (s.equals("boolean")) {
                desc += "Z";
                insnList.add(new VarInsnNode(Opcodes.ILOAD, j++));
            } else if (s.equals("float")) {
                desc += "F";
                insnList.add(new VarInsnNode(Opcodes.FLOAD, j++));
            } else if (s.equals("double")) {
                desc += "D";
                insnList.add(new VarInsnNode(Opcodes.DLOAD, j));
                j += 2;
            } else if (s.equals("long")) {
                desc += "J";
                insnList.add(new VarInsnNode(Opcodes.LLOAD, j));
                j += 2;
            } else if (s.equals("byte")) {
                desc += "B";
                insnList.add(new VarInsnNode(Opcodes.ILOAD, j++));
            } else if (s.equals("char")) {
                desc += "C";
                insnList.add(new VarInsnNode(Opcodes.ILOAD, j++));
            } else if (s.equals("string")) {
                desc += "Ljava/lang/String;";
                insnList.add(new VarInsnNode(Opcodes.ALOAD, j++));
            } else {
                desc += "L" + s + ";";
                insnList.add(new VarInsnNode(Opcodes.ALOAD, j++));
            }
        }
        desc += ")V";
        insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, hook.callerClass, hook.hookMethod.name, Mappings.getClassMappings(desc), false));
        /*
         * LabelNode labelNode = new LabelNode(); insnList.add(new JumpInsnNode(Opcodes.IFEQ, labelNode)); insnList.add(new InsnNode(Opcodes.RETURN));
         * insnList.add(labelNode); insnList.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
         */
        methodNode.instructions.insertBefore(methodNode.instructions.get(0), insnList);
    }
}
