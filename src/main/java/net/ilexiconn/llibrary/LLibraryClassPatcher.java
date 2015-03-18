package net.ilexiconn.llibrary;

import java.util.Iterator;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class LLibraryClassPatcher implements IClassTransformer
{
	@Override
	public byte[] transform(String name, String deobfName, byte[] bytes)
	{
		boolean obfuscated = !name.equals(deobfName);
		
		String modelBaseName = "net.minecraft.client.model.ModelBase"; 
		
		if(obfuscated)
		{
			modelBaseName = "bhr";
		}
		
		if (deobfName.equals(modelBaseName) || name.equals(modelBaseName))
			return patchModelBaseClass(name, bytes, deobfName, obfuscated);
		else
			return bytes;
	}

	public byte[] patchModelBaseClass(String name, byte[] bytes, String deobfName, boolean obfuscated) //TODO
	{	
		try 
		{
			ClassNode classNode = new ClassNode();

			ClassReader classReader = new ClassReader(bytes);
			classReader.accept(classNode, 0);

			final String RENDER_METHOD_NAME = obfuscated ? "a" : "render";
			final String RENDER_METHOD_DESC = obfuscated ? "(Lsa;FFFFFF)V" : "(Lnet/minecraft/entity/Entity;FFFFFF)V";
			
			for (MethodNode method : classNode.methods)
			{
				if(method.name.equals(RENDER_METHOD_NAME) && method.desc.equals(RENDER_METHOD_DESC))
				{
					LabelNode instruction = new LabelNode();
					
					InsnList toInsert = new InsnList();
					toInsert.add(new VarInsnNode(Opcodes.ALOAD, 1)); //Inputs arg 0 (Wrote 1 because 0 is the current class you're editing) of Render into custom method.
					toInsert.add(new VarInsnNode(Opcodes.FLOAD, 2));
					toInsert.add(new VarInsnNode(Opcodes.FLOAD, 3));
					toInsert.add(new VarInsnNode(Opcodes.FLOAD, 4));
					toInsert.add(new VarInsnNode(Opcodes.FLOAD, 5));
					toInsert.add(new VarInsnNode(Opcodes.FLOAD, 6));
					toInsert.add(new VarInsnNode(Opcodes.FLOAD, 7));
					toInsert.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/ilexiconn/llibrary/LLibraryHooks", "renderHook", RENDER_METHOD_DESC, false));
				
					method.instructions.insert(toInsert);
				}
			}
			
			ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);

			classNode.accept(classWriter);

			return classWriter.toByteArray();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return bytes;
	}
}
