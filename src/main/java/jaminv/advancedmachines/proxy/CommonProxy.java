package jaminv.advancedmachines.proxy;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.init.RecipeInit;
import jaminv.advancedmachines.util.handlers.OreDictionaryHandler;
import jaminv.advancedmachines.util.handlers.RegistryHandler;
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
		//File directory = e.getModConfigurationDirectory();
		//ConfigManager.
		//config = new Configuration(new File(directory.getPath(), "advancedmachines.cfg"));
		//Config.readConfig();
		
		Main.logger.info("Other registries");
		
		RegistryHandler.otherRegistries();
		
		Main.logger.info("Completed - Other registries");
	}
	
	public void init(FMLInitializationEvent e) {
		OreDictionaryHandler.registerOreDictionary();
		RecipeInit.init();
		
		NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, new GuiProxy());
	}

	public void postInit(FMLPostInitializationEvent e) {
		DataParser.parse();
	}
	
	public void registerItemRenderer(Item item, int meta, String id) {}	
	public void registerVariantRenderer(Item item, int meta, String filename, String id) {}
}
