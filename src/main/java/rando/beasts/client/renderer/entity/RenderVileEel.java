package rando.beasts.client.renderer.entity;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import rando.beasts.client.model.ModelVileEel;
import rando.beasts.common.entity.monster.EntityVileEel;
import rando.beasts.common.utils.BeastsReference;

@OnlyIn(Dist.CLIENT)
public class RenderVileEel extends MobRenderer<EntityVileEel, ModelVileEel> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(BeastsReference.ID, "textures/entity/vileeel.png");
	
	public RenderVileEel(EntityRendererManager rm) {
		super(rm, new ModelVileEel(), 1.0F);
	}

	@Override
	protected void preRenderCallback(EntityVileEel e, float partialTickTime) {
		GlStateManager.scalef(1.5F, 1.5F, 1.5F);
		GlStateManager.translatef(0.0F, 0.0F, 0.0F);
		super.preRenderCallback(e, partialTickTime);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityVileEel entity) {
		return TEXTURE;
	}
}