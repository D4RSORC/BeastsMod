package rando.beasts.common.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import rando.beasts.client.gui.GuiLandwhaleInventory;
import rando.beasts.common.entity.passive.EntityLandwhale;
import rando.beasts.common.inventory.ContainerLandwhaleInventory;
import rando.beasts.common.utils.BeastsReference;

@EventBusSubscriber(modid = BeastsReference.ID, bus = Bus.MOD)
public class BeastsContainers {
	public static final ContainerType<ContainerLandwhaleInventory> LANDWHALE = IForgeContainerType.<ContainerLandwhaleInventory>create((id, inv, data) -> {
    	int entity = data.readInt();
    	World world = Minecraft.getInstance().world;
    	return new ContainerLandwhaleInventory(id, (EntityLandwhale)world.getEntityByID(entity), inv.player);
    });
	
	
    @SubscribeEvent
    public static void setup(FMLClientSetupEvent event)
    {
    	
        ScreenManager.<ContainerLandwhaleInventory,GuiLandwhaleInventory>registerFactory(LANDWHALE, GuiLandwhaleInventory::new);
    }

}
