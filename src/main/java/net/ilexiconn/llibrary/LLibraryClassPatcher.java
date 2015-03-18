package net.ilexiconn.llibrary;

import java.util.Iterator;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class LLibraryClassPatcher implements IClassTransformer
{
	@Override
	public byte[] transform(String name, String deobfName, byte[] bytes)
	{
		//String modelBaseName = ModelBase.class.getCanonicalName();

		//		if (deobfName.equals(modelBaseName) || name.equals(modelBaseName))
		//			return patchModelBaseClass(name, bytes, deobfName);
		//		else
		return bytes;
	}

	public byte[] patchModelBaseClass(String name, byte[] bytes, String deobfName) //TODO
	{	
		String renderMethodName = "render";
		String setRotationAnglesMethodName = "setRotationAngles";

		boolean hasPatchedRender = false;
		boolean hasPatchedSetRotationAngles = false;

		boolean obfuscated;

		if(!name.equals(deobfName))
			obfuscated = true;
		else
			obfuscated = false;

		if(obfuscated)
		{
			renderMethodName = "a";
			setRotationAnglesMethodName = "a"; //TODO Check what they really are when obfuscated.
		}

		String formattedEntityClassName = "L" + Entity.class.getCanonicalName().replaceAll("\\.", "/") + ";";
		String formattedName = "L" + name.replaceAll("\\.", "/") + ";";

		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);

		Iterator<MethodNode> methods = classNode.methods.iterator();
		while(methods.hasNext())
		{
			MethodNode m = methods.next();

			if ((m.name.equals(renderMethodName) && m.desc.equals("(" + formattedEntityClassName + "FFFFFF)V")))
			{
				InsnList toInject = new InsnList();
				toInject.add(new VarInsnNode(Opcodes.ALOAD, 0)); //TODO insert the entity
				toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "injectclasshere", "injectmethod", "(" + formattedEntityClassName +  "FFFFFF" + ")V"));

				m.instructions.insert(toInject);

				hasPatchedRender = true;

				if(hasPatchedSetRotationAngles)
				{
					break;
				}
			}
			else if((m.name.equals(setRotationAnglesMethodName) && m.desc.equals("(FF)V")))
			{
				m.instructions.clear();

				InsnList toInject = new InsnList();
				toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
				toInject.add(new VarInsnNode(Opcodes.FLOAD, 1));
				toInject.add(new VarInsnNode(Opcodes.FLOAD, 2));
				toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/gegy1000/advancedgravity/patcher/SetAnglesInjectCode", "customSetAngles", "(" + formattedName + "FF)V"));
				toInject.add(new InsnNode(Opcodes.RETURN));

				m.instructions.insert(toInject);

				hasPatchedSetRotationAngles = true;

				if(hasPatchedRender)
				{
					break;
				}
			}
		}

		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);

		return writer.toByteArray();
	}
}
