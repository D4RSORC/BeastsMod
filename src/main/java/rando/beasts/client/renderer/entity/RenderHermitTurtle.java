package rando.beasts.client.renderer.entity;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;
import rando.beasts.client.model.ModelHermitTurtle;
import rando.beasts.common.entity.passive.EntityHermitTurtle;
import rando.beasts.common.utils.BeastsReference;

public class RenderHermitTurtle extends LivingRenderer<EntityHermitTurtle, ModelHermitTurtle> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(BeastsReference.ID, "textures/entity/hermit_turtle.png");

    public RenderHermitTurtle(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ModelHermitTurtle(), 0.1f);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityHermitTurtle entity) {
        return TEXTURE;
    }
}
