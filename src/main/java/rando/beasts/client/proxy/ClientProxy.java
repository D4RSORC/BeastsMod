package rando.beasts.client.proxy;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import rando.beasts.client.model.ModelSpartapodArmor;
import rando.beasts.client.renderer.entity.RenderBeastsPainting;
import rando.beasts.client.renderer.entity.RenderBranchie;
import rando.beasts.client.renderer.entity.RenderCoconutBomb;
import rando.beasts.client.renderer.entity.RenderCoconutCrab;
import rando.beasts.client.renderer.entity.RenderGiantGardenEel;
import rando.beasts.client.renderer.entity.RenderLandwhale;
import rando.beasts.client.renderer.entity.RenderPufferfishDog;
import rando.beasts.client.renderer.entity.RenderRabbitman;
import rando.beasts.client.renderer.entity.RenderSkewerShrimp;
import rando.beasts.client.renderer.entity.RenderVileEel;
import rando.beasts.client.renderer.tileentity.TileEntityCoconutRenderer;
import rando.beasts.common.entity.item.EntityBeastsPainting;
import rando.beasts.common.entity.monster.EntityBranchie;
import rando.beasts.common.entity.monster.EntityCoconutCrab;
import rando.beasts.common.entity.monster.EntityGiantGardenEel;
import rando.beasts.common.entity.monster.EntitySkewerShrimp;
import rando.beasts.common.entity.monster.EntityVileEel;
import rando.beasts.common.entity.passive.EntityLandwhale;
import rando.beasts.common.entity.passive.EntityPufferfishDog;
import rando.beasts.common.entity.passive.EntityRabbitman;
import rando.beasts.common.entity.projectile.EntityCoconutBomb;
import rando.beasts.common.init.BeastsItems;
import rando.beasts.common.proxy.CommonProxy;
import rando.beasts.common.tileentity.TileEntityCoconut;

public class ClientProxy extends CommonProxy {

	private static final ModelSpartapodArmor SPARTAPOD = new ModelSpartapodArmor();

	@Override
	public void preInit() {
		super.preInit();
		this.registerRenders();
	}

	private void registerRenders() {
		RenderingRegistry.registerEntityRenderingHandler(EntityCoconutCrab.class, RenderCoconutCrab::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityPufferfishDog.class, RenderPufferfishDog::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityRabbitman.class, RenderRabbitman::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySkewerShrimp.class, RenderSkewerShrimp::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityGiantGardenEel.class, RenderGiantGardenEel::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityVileEel.class, RenderVileEel::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityLandwhale.class, RenderLandwhale::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCoconutBomb.class, RenderCoconutBomb::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBranchie.class, RenderBranchie::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBeastsPainting.class, RenderBeastsPainting::new);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCoconut.class, new TileEntityCoconutRenderer());
	}

	@Override
	public BipedModel getArmorModel(Item armorItem, EquipmentSlotType armorSlot) {
		if (armorItem == BeastsItems.SPARTAPOD_HELMET && armorSlot == EquipmentSlotType.HEAD)
			return SPARTAPOD;
		return null;
	}
}
