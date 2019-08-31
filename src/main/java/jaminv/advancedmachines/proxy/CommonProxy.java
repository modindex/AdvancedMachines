package jaminv.advancedmachines.proxy;


import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.lib.util.parser.DataParser;
import jaminv.advancedmachines.lib.util.parser.FileHandlerOreDictionary;
import jaminv.advancedmachines.proxy.handlers.RegistryHandler;
import jaminv.advancedmachines.util.network.MessageRegistry;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

@EventBusSubscriber
public class CommonProxy {
	private static final Class RedstoneStateMessage = null;
	public static Configuration config;
	
	public void preInit(FMLPreInitializationEvent e) {
		MinecraftForge.EVENT_BUS.register(RegistryHandler.class);
		
		Main.logger.info("Other registries");
		
		MessageRegistry.register();
		// FIXME: World Gen
		//GameRegistry.registerWorldGenerator(new WorldGenCustomOres(), 0);
		
		Main.logger.info("Completed - Other registries");
	}
	
	public void init(FMLInitializationEvent e) {
		DataParser.parseConstants();	
		DataParser.parseFolder("data/ore_dictionary", new FileHandlerOreDictionary());
		
		RecipeInit.init();
		
		NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, new GuiProxy());
	}

	public void postInit(FMLPostInitializationEvent e) {}
	
	public World getMessageWorld(MessageContext ctx) { return null; }
}
