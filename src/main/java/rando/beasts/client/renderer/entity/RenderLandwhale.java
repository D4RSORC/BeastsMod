package rando.beasts.client.renderer.entity;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import rando.beasts.client.model.ModelLandwhale;
import rando.beasts.common.entity.passive.EntityLandwhale;
import rando.beasts.common.utils.BeastsReference;

@OnlyIn(Dist.CLIENT)
public class RenderLandwhale extends MobRenderer<EntityLandwhale, ModelLandwhale> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(BeastsReference.ID,
			"textures/entity/landwhale.png");

	@Override
	protected void preRenderCallback(EntityLandwhale e, float partialTickTime) {
		GlStateManager.scalef(1.5F, 1.5F, 1.5F);
		GlStateManager.translatef(0.0F, 0.0F, 0.0F);
		super.preRenderCallback(e, partialTickTime);
	}

	public RenderLandwhale(EntityRendererManager rm) {
		super(rm, new ModelLandwhale(), 1.0F);

	}

	@Override
	protected ResourceLocation getEntityTexture(EntityLandwhale entity) {
		return TEXTURE;
	}
}