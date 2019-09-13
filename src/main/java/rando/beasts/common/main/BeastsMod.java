package rando.beasts.common.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import rando.beasts.client.proxy.ClientProxy;
import rando.beasts.common.proxy.CommonProxy;
import rando.beasts.common.utils.BeastsReference;

@Mod(BeastsReference.ID)
public class BeastsMod {

	// public static final RabbitVillageGenerator RABBIT_VILLAGE = new
	// RabbitVillageGenerator();

	private static Logger logger = LogManager.getLogger(BeastsReference.ID);

	public static BeastsMod instance;
	public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

	public BeastsMod() {
		instance = this;

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::preInit);
		MinecraftForge.EVENT_BUS.addListener(this::serverStart);
	}

	public void preInit(FMLCommonSetupEvent event) {
		proxy.preInit();
	}

	public void serverStart(FMLServerStartingEvent event) {
		// TODO
		event.getCommandDispatcher();
	}

	public static Logger getLogger() {
		return logger;
	}
}
