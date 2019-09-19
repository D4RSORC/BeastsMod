package rando.beasts.client.renderer.entity;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import rando.beasts.common.entity.monster.EntityWoodBranchie;
import rando.beasts.common.utils.BeastsReference;

public class RenderWoodBranchie extends RenderBranchieBase<EntityWoodBranchie> {
    private static final Map<String, ResourceLocation> TEXTURES = new HashMap<>();

    public RenderWoodBranchie(EntityRendererManager rm) {
        super(rm);
    }

    protected ResourceLocation getEntityTexture(EntityWoodBranchie entity) {
        return TEXTURES.putIfAbsent(entity.getVariant().getName(), new ResourceLocation(BeastsReference.ID, "textures/entity/branchie/wood/" + entity.getVariant().getName() + ".png"));
    }
}
