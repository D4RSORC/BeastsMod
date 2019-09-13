package rando.beasts.client.renderer.entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import rando.beasts.client.model.ModelSkewerShrimp;
import rando.beasts.common.entity.monster.EntitySkewerShrimp;
import rando.beasts.common.utils.BeastsReference;

public class RenderSkewerShrimp extends MobRenderer<EntitySkewerShrimp, ModelSkewerShrimp> {

	public static final ResourceLocation TEXTURE = new ResourceLocation(BeastsReference.ID,
			"textures/entity/skewer_shrimp.png");

	public RenderSkewerShrimp(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, new ModelSkewerShrimp(), 0.1f);
	}

	@Override
	protected void preRenderCallback(EntitySkewerShrimp entitylivingbaseIn, float partialTickTime) {
//        GlStateManager.translate(-0.2, 0, 0);
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(@Nonnull EntitySkewerShrimp entity) {
		return TEXTURE;
	}
}
