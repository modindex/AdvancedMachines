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
import jaminv.advancedmachines.util.models.BlockstateInventory;
import jaminv.advancedmachines.util.models.BlockstateMaker;
import jaminv.advancedmachines.util.models.BlockstateMaterial;
import jaminv.advancedmachines.util.models.BlockstateMaterialFace;
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
			BlockstateMaterial expansion = new BlockstateMaterial("machine_expansion", "machine/expansion/", MaterialBase.MaterialType.EXPANSION);
			expansion.make();
			
			BlockstateInventory inventory = new BlockstateInventory("machine_inventory", "machine/expansion/", "machine/inventory/input/", "machine/inventory/output/", MaterialBase.MaterialType.EXPANSION);
			inventory.make();
			
			BlockstateMaterialFace energy = new BlockstateMaterialFace("machine_energy", "machine/expansion/", "machine/energy/", MaterialBase.MaterialType.EXPANSION);
			energy.make();
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
