package rando.beasts.client.renderer.entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import rando.beasts.client.model.ModelCoconut;
import rando.beasts.common.entity.monster.EntityCoconutCrab;
import rando.beasts.common.utils.BeastsReference;

public class RenderCoconutCrab extends MobRenderer<EntityCoconutCrab, ModelCoconut> {

	public static final ResourceLocation TEXTURE = new ResourceLocation(BeastsReference.ID,
			"textures/entity/coconut_crab.png");

	public RenderCoconutCrab(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCoconut(), 0.1f);
		this.addLayer(new LayerCrabItem(this));
	}

	@Override
	protected void preRenderCallback(EntityCoconutCrab entitylivingbaseIn, float partialTickTime) {
		GlStateManager.translatef(-0.2f, 0, 0);
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(@Nonnull EntityCoconutCrab entity) {
		return TEXTURE;
	}

	@OnlyIn(Dist.CLIENT)
	public static class LayerCrabItem extends LayerRenderer<EntityCoconutCrab, ModelCoconut> {
		public LayerCrabItem(IEntityRenderer<EntityCoconutCrab, ModelCoconut> entityRendererIn) {
			super(entityRendererIn);
		}

		private ItemEntity item = new ItemEntity(EntityType.ITEM, Minecraft.getInstance().world);

		@Override
		public void render(EntityCoconutCrab entity, float f, float f1, float f2, float f3, float f4, float f5,
				float f6) {
			if (!entity.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
				if (!entity.isInvisible() && entity.getHeldItem(Hand.MAIN_HAND) != ItemStack.EMPTY) {
					item.setItem(entity.getHeldItem(Hand.MAIN_HAND));
					GlStateManager.pushMatrix();
					GlStateManager.rotatef(180, 0, 0, 1);
					GlStateManager.rotatef(90, 0, 1, 0);
					GlStateManager.translatef(0.6F, -1.5F, 0.25f);
					GlStateManager.scalef(0.7F, 0.7F, 0.7F);
					Minecraft.getInstance().getRenderManager().renderEntity(item, 0, 0, 0, 0, 0, false);
					GlStateManager.popMatrix();
				}
			}
		}

		@Override
		public boolean shouldCombineTextures() {
			return false;
		}
	}
}
