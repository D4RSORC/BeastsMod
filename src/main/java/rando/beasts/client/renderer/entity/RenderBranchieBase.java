package rando.beasts.client.renderer.entity;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import rando.beasts.client.model.ModelBranchie;
import rando.beasts.common.entity.monster.EntityBranchieBase;

@OnlyIn(Dist.CLIENT)
public abstract class RenderBranchieBase<T extends EntityBranchieBase> extends MobRenderer<T, ModelBranchie<T>> {
	public RenderBranchieBase(EntityRendererManager rm) {
		super(rm, new ModelBranchie(), 0.1F);
	}

	@Override
	protected void preRenderCallback(T e, float partialTickTime) {
		GlStateManager.scalef(1.0F, 1.0F, 1.0F);
		GlStateManager.translatef(0.0F, 0.0F, 0.0F);
		super.preRenderCallback(e, partialTickTime);
	}
}
