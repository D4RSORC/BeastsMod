package rando.beasts.client.model;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.LivingEntity;

public class ModelSpartapodArmor extends BipedModel<LivingEntity> {

	public RendererModel helmetBase;
	public RendererModel headFin;

	public ModelSpartapodArmor() {
		// TODO this needs fixing
		this.textureWidth = 128;
		this.textureHeight = 128;
		this.bipedHeadwear = new RendererModel(this, 24, 0);
		this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedHeadwear.addBox(-3.0F, -6.0F, -1.0F, 6, 6, 1, 0.0F);
		this.bipedHead = new RendererModel(this, 0, 0);
		this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
		this.headFin = new RendererModel(this, 65, 11);
		this.headFin.setRotationPoint(0.0F, -9.0F, 0.5F);
		this.headFin.addBox(0.0F, -6.0F, -6.0F, 0, 12, 13, 0.0F);
		this.helmetBase = new RendererModel(this, 64, 0);
		this.helmetBase.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.helmetBase.addBox(-5.0F, -9.0F, -5.0F, 10, 11, 10, 0.0F);
		this.helmetBase.addChild(this.headFin);
		this.bipedHead.addChild(this.helmetBase);
	}

	@Override
	public void render(LivingEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.bipedHeadwear.render(f5);
		this.bipedHead.render(f5);
	}
}
