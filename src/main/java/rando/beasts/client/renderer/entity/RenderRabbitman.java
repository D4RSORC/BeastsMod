package rando.beasts.client.renderer.entity;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import rando.beasts.client.model.ModelRabbitman;
import rando.beasts.client.renderer.entity.layers.LayerRabbitmanItem;
import rando.beasts.common.entity.passive.EntityRabbitman;
import rando.beasts.common.utils.BeastsReference;

public class RenderRabbitman extends MobRenderer<EntityRabbitman, ModelRabbitman> {

	private static final ResourceLocation[] TEXTURES = new ResourceLocation[EntityRabbitman.VARIANTS];

	public RenderRabbitman(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, new ModelRabbitman(), 0.5F);
		this.addLayer(new LayerRabbitmanItem(this));
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityRabbitman entity) {
		return TEXTURES[entity.getVariant()];
	}

	static {
		for (int i = 0; i < TEXTURES.length; i++)
			TEXTURES[i] = new ResourceLocation(BeastsReference.ID,
					"textures/entity/rabbitman/texture_" + (i + 1) + ".png");
	}
}
