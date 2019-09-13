package rando.beasts.client.renderer.tileentity;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import rando.beasts.client.model.ModelCoconut;
import rando.beasts.client.renderer.entity.RenderCoconutCrab;
import rando.beasts.common.tileentity.TileEntityCoconut;

public class TileEntityCoconutRenderer extends TileEntityRenderer<TileEntityCoconut> {

	private final ModelCoconut model = new ModelCoconut();

	@Override
	public void render(TileEntityCoconut te, double x, double y, double z, float partialTicks, int destroyStage) {
		GlStateManager.pushMatrix();
		super.render(te, x, y, z, partialTicks, destroyStage);
		bindTexture(RenderCoconutCrab.TEXTURE);
		model.render(null, partialTicks, 0, destroyStage, 0, 0, 1);
		GlStateManager.popMatrix();
	}
}
