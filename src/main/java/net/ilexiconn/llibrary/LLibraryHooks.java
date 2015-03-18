package net.ilexiconn.llibrary;

import net.minecraft.entity.Entity;

public class LLibraryHooks
{
	public static void renderHook(Entity entity, float limbSwing, float limbSwingAmount, float rotationFloat, float rotationYaw, float rotationPitch, float partialTicks)
	{
		System.out.println("It works!");
	}
}
