package jaminv.advancedmachines.proxy;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.Level;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.init.RecipeInit;
import jaminv.advancedmachines.util.Config;
import jaminv.advancedmachines.util.handlers.OreDictionaryHandler;
import jaminv.advancedmachines.util.handlers.RegistryHandler;
import jaminv.advancedmachines.util.material.MaterialBase;
import jaminv.advancedmachines.util.models.BlockstateMaker;
import jaminv.advancedmachines.util.models.ModelMaker;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@EventBusSubscriber
public class CommonProxy {
	public static Configuration config;
	
	public void preInit(FMLPreInitializationEvent e) {
		File directory = e.getModConfigurationDirectory();
		config = new Configuration(new File(directory.getPath(), "advancedmachines.cfg"));
		Config.readConfig();
		
		RegistryHandler.otherRegistries();
		
		try {
			ModelMaker.make("machine\\expansion", "machine_expansion", MaterialBase.MaterialType.EXPANSION);
			BlockstateMaker.make("machine\\expansion", "machine_expansion", MaterialBase.MaterialType.EXPANSION);
		} catch (IOException e1) {
			Main.logger.log(Level.ERROR, "Error creating model file", e.toString());
			e1.printStackTrace();
		}
	}
	
	public void init(FMLInitializationEvent e) {
		OreDictionaryHandler.registerOreDictionary();
		RecipeInit.init();
		
		NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, new GuiProxy());
	}

	public void postInit(FMLPostInitializationEvent e) {
	}
	
	public void registerItemRenderer(Item item, int meta, String id) {}	
	public void registerVariantRenderer(Item item, int meta, String filename, String id) {}
}
