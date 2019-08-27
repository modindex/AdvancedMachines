package jaminv.advancedmachines.proxy;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.client.textureset.FileHandlerTextureSet;
import jaminv.advancedmachines.init.RecipeInit;
import jaminv.advancedmachines.proxy.handlers.EventHandlerClient;
import jaminv.advancedmachines.proxy.handlers.OreDictionaryHandler;
import jaminv.advancedmachines.proxy.handlers.RegistryHandler;
import jaminv.advancedmachines.util.parser.DataParser;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@EventBusSubscriber
public class CommonProxy {
	private static final Class RedstoneStateMessage = null;
	public static Configuration config;
	
	public void preInit(FMLPreInitializationEvent e) {
		MinecraftForge.EVENT_BUS.register(RegistryHandler.class);
		
		Main.logger.info("Other registries");
		
		RegistryHandler.otherRegistries();
		
		Main.logger.info("Completed - Other registries");
	}
	
	public void init(FMLInitializationEvent e) {
		DataParser.parseConstants();
		
		OreDictionaryHandler.registerOreDictionary();
		RecipeInit.init();
		
		NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, new GuiProxy());
	}

	public void postInit(FMLPostInitializationEvent e) {
	}
	
	public void registerItemRenderer(Item item, int meta, String id) {}	
	public void registerVariantRenderer(Item item, int meta, String filename, String id) {}
}
