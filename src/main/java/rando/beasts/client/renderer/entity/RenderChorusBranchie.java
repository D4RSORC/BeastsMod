package rando.beasts.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import rando.beasts.common.entity.monster.EntityChorusBranchie;
import rando.beasts.common.utils.BeastsReference;

public class RenderChorusBranchie extends RenderBranchieBase<EntityChorusBranchie> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(BeastsReference.ID, "textures/entity/branchie/chorus.png");

    public RenderChorusBranchie(EntityRendererManager rm) {
        super(rm);
    }

    protected ResourceLocation getEntityTexture(EntityChorusBranchie entity) {
        return TEXTURE;
    }
}
