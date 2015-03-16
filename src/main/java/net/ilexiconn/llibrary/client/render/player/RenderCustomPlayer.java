package net.ilexiconn.llibrary.client.render.player;

import net.ilexiconn.llibrary.client.model.player.ModelCustomBiped;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;

public final class RenderCustomPlayer extends RenderPlayer
{
	public RenderCustomPlayer()
	{
		setRenderManager(RenderManager.instance);
		mainModel = new ModelCustomBiped();
		modelBipedMain = (ModelBiped) mainModel;
	}
}
