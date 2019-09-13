package rando.beasts.client.renderer.entity.layers;

import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import rando.beasts.client.model.ModelPufferFishDog;
import rando.beasts.client.renderer.entity.RenderPufferfishDog;
import rando.beasts.common.entity.passive.EntityPufferfishDog;
import rando.beasts.common.utils.BeastsReference;

public class LayerGlasses extends LayerRenderer<EntityPufferfishDog, ModelPufferFishDog> {
	private static final ResourceLocation BUFFORD = new ResourceLocation(BeastsReference.ID,
			"textures/entity/pufferfish_dog/bufford.png");
	private static final ResourceLocation BUFFORD_INFLATED = new ResourceLocation(BeastsReference.ID,
			"textures/entity/pufferfish_dog/bufford_inflated.png");

	public LayerGlasses(RenderPufferfishDog render) {
		super(render);
	}

	@Override
	public void render(EntityPufferfishDog entity, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (entity.hasCustomName() && entity.getCustomName().getString().equalsIgnoreCase("Bufford")
				&& !entity.isInvisible()) {
			this.bindTexture(entity.getInflated() ? BUFFORD_INFLATED : BUFFORD);
			this.getEntityModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return true;
	}

}
