package rando.beasts.client.renderer.entity;

import java.util.HashMap;
import java.util.Map;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import rando.beasts.client.model.ModelBranchie;
import rando.beasts.common.entity.monster.EntityBranchie;
import rando.beasts.common.utils.BeastsReference;

@OnlyIn(Dist.CLIENT)
public class RenderBranchie extends MobRenderer<EntityBranchie, ModelBranchie> {
	private static final Map<String, ResourceLocation> TEXTURES = new HashMap<>();

	public RenderBranchie(EntityRendererManager rm) {
		super(rm, new ModelBranchie(), 0.1F);
	}

	@Override
	protected void preRenderCallback(EntityBranchie e, float partialTickTime) {
		GlStateManager.scalef(1.0F, 1.0F, 1.0F);
		GlStateManager.translatef(0.0F, 0.0F, 0.0F);
		super.preRenderCallback(e, partialTickTime);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityBranchie entity) {
		return TEXTURES.putIfAbsent(entity.getVariant().getName(), new ResourceLocation(BeastsReference.ID,
				"textures/entity/branchie/" + entity.getVariant().getName() + ".png"));
	}
}
