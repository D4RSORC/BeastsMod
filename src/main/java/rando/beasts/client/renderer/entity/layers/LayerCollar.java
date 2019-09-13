package rando.beasts.client.renderer.entity.layers;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import rando.beasts.client.model.ModelPufferFishDog;
import rando.beasts.client.renderer.entity.RenderPufferfishDog;
import rando.beasts.common.entity.passive.EntityPufferfishDog;
import rando.beasts.common.utils.BeastsReference;

@OnlyIn(Dist.CLIENT)
public class LayerCollar extends LayerRenderer<EntityPufferfishDog, ModelPufferFishDog> {
	private static final ResourceLocation COLLAR = new ResourceLocation(BeastsReference.ID,
			"textures/entity/pufferfish_dog/collar.png");

	public LayerCollar(RenderPufferfishDog render) {
		super(render);
	}

	@Override
	public void render(EntityPufferfishDog entity, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (entity.isTamed() && !entity.isInvisible()) {
			this.bindTexture(COLLAR);
			float[] colors = entity.getCollarColor().getColorComponentValues();
			GlStateManager.color3f(colors[0], colors[1], colors[2]);
			this.getEntityModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return true;
	}
}
