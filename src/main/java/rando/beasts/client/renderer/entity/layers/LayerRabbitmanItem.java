package rando.beasts.client.renderer.entity.layers;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import rando.beasts.client.model.ModelRabbitman;
import rando.beasts.common.entity.passive.EntityRabbitman;

public class LayerRabbitmanItem extends LayerRenderer<EntityRabbitman, ModelRabbitman> {

	public LayerRabbitmanItem(IEntityRenderer<EntityRabbitman, ModelRabbitman> entityRendererIn) {
		super(entityRendererIn);
	}

	@Override
	public void render(EntityRabbitman entity, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (entity.getVariant() == 5 && !entity.hasTarget) {
			GlStateManager.pushMatrix();
			if (entity.isSneaking())
				GlStateManager.translatef(0.0F, 0.2F, 0.0F);
			GlStateManager.rotatef(-90.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotatef(180.0F, 0.0F, 1.0F, 1.0F);
			GlStateManager.rotatef(-5.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotatef(-25.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotatef(20F, 1.0F, 1.0F, 0.0F);
			GlStateManager.translatef(-0.1f, 0.15f, 0.35f);
			Minecraft.getInstance().getItemRenderer().renderItem(entity.getHeldItemMainhand(), entity,
					ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, false);
			GlStateManager.popMatrix();
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
}
