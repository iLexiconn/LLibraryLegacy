package net.ilexiconn.llibrary.common.asm;

import net.ilexiconn.llibrary.common.asm.mappings.Mappings;
import net.ilexiconn.llibrary.common.json.container.JsonHook;
import net.ilexiconn.llibrary.common.plugin.LLibraryPlugin;
import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class HookPatchManager implements IClassTransformer {
    public Logger logger = LogManager.getLogger("LLibraryHooks");

    @Override
    public byte[] transform(String name, String transformedName, byte[] classBytes) {
        ClassNode classNode = createClassFromByteArray(classBytes);
        for (JsonHook hook : LLibraryPlugin.hookList) {
            if (transformedName.equals(hook.hookClass.name.replaceAll("/", "."))) {
                logger.info("Transforming class " + transformedName);
                logger.info(" -> " + Mappings.getClassMappings(transformedName));

                String methodName = Mappings.getMethodMappings(hook.hookClass.name.replaceAll("/", ".") + "/" + hook.hookMethod.name);
                String desc = "(";
                for (String s : hook.hookMethod.args) {
                    if (s.equals("int")) {
                        desc += "I";
                    } else if (s.equals("boolean")) {
                        desc += "Z";
                    } else if (s.equals("float")) {
                        desc += "F";
                    } else if (s.equals("double")) {
                        desc += "D";
                    } else if (s.equals("long")) {
                        desc += "J";
                    } else if (s.equals("byte")) {
                        desc += "B";
                    } else if (s.equals("char")) {
                        desc += "C";
                    } else if (s.equals("string")) {
                        desc += "Ljava/lang/String;";
                    } else {
                        desc += "L" + s + ";";
                    }
                }
                desc += ")V";
                String methodDesc = Mappings.getClassMappings(desc);

                boolean flag = false;
                for (MethodNode method : classNode.methods) {
                    if (method.name.equals(methodName) && method.desc.equals(methodDesc)) {
                        transform(method, hook);
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    logger.info("Transformed class");
                } else {
                    throw new RuntimeException("Unable to patch method " + methodName + methodDesc);
                }
            }
        }
        classBytes = createByteArrayFromClass(classNode, ClassWriter.COMPUTE_MAXS);
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
                insnList.add(new VarInsnNode(Opcodes.DLOAD, j++));
            } else if (s.equals("long")) {
                desc += "J";
                insnList.add(new VarInsnNode(Opcodes.LLOAD, j++));
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
        /*LabelNode labelNode = new LabelNode();
        insnList.add(new JumpInsnNode(Opcodes.IFEQ, labelNode));
        insnList.add(new InsnNode(Opcodes.RETURN));
        insnList.add(labelNode);
        insnList.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));*/
        methodNode.instructions.insertBefore(methodNode.instructions.get(0), insnList);
    }

    public static ClassNode createClassFromByteArray(byte[] classBytes) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(classBytes);
        classReader.accept(classNode, ClassReader.EXPAND_FRAMES);
        return classNode;
    }

    public static byte[] createByteArrayFromClass(ClassNode classNode, int flags) {
        ClassWriter classWriter = new ClassWriter(flags);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
}
