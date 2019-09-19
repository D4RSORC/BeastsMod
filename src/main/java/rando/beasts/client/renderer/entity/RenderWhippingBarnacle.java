package rando.beasts.client.renderer.entity;

import javax.annotation.Nullable;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import rando.beasts.client.model.ModelWhippingBarnacle;
import rando.beasts.common.entity.monster.EntityWhippingBarnacle;
import rando.beasts.common.utils.BeastsReference;

public class RenderWhippingBarnacle extends LivingRenderer<EntityWhippingBarnacle, ModelWhippingBarnacle> {

    private static final ResourceLocation BLUE = new ResourceLocation(BeastsReference.ID, "textures/entity/whipping_barnacle/blue.png");
    private static final ResourceLocation GREEN = new ResourceLocation(BeastsReference.ID, "textures/entity/whipping_barnacle/green.png");

    public RenderWhippingBarnacle(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ModelWhippingBarnacle(), 0);
    }

    @Override
    protected void preRenderCallback(EntityWhippingBarnacle entitylivingbaseIn, float partialTickTime) {
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
        Direction facing = entitylivingbaseIn.getHorizontalFacing();
        switch (facing.getAxis()) {
            case X:
                //GlStateManager.rotate(90, 0, 0, facing.getAxisDirection().getOffset());
                break;
            case Z:
                GlStateManager.rotatef(90, facing.getAxisDirection().getOffset(), 0, 0);
                break;
        }
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityWhippingBarnacle entity) {
        return entity.getColor() == 1 ? GREEN : BLUE;
    }
}
