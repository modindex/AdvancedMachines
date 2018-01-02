package jaminv.advancedmachines.proxy;

import java.io.File;

import jaminv.advancedmachines.util.Config;
import jaminv.advancedmachines.util.handlers.OreDictionaryHandler;
import jaminv.advancedmachines.util.handlers.RegistryHandler;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@EventBusSubscriber
public class CommonProxy {
	public static Configuration config;
	
	public void preInit(FMLPreInitializationEvent e) {
		File directory = e.getModConfigurationDirectory();
		config = new Configuration(new File(directory.getPath(), "advancedmachines.cfg"));
		Config.readConfig();
		
		RegistryHandler.otherRegistries();
	}
	
	public void init(FMLInitializationEvent e) {
		OreDictionaryHandler.registerOreDictionary();
	}

	public void postInit(FMLPostInitializationEvent e) {
	}
	
	public void registerItemRenderer(Item item, int meta, String id) {}	
	public void registerVariantRenderer(Item item, int meta, String filename, String id) {}
}
