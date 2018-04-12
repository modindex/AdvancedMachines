package jaminv.advancedmachines.proxy;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.Level;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.init.RecipeInit;
import jaminv.advancedmachines.objects.blocks.machine.expansion.inventory.InventoryStateMessage;
import jaminv.advancedmachines.objects.blocks.machine.expansion.inventory.InventoryStateMessage.InventoryStateMessageHandler;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.MultiblockUpdateMessage;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.MultiblockUpdateMessage.MultiblockUpdateMessageHandler;
import jaminv.advancedmachines.util.Config;
import jaminv.advancedmachines.util.handlers.OreDictionaryHandler;
import jaminv.advancedmachines.util.handlers.RegistryHandler;
import jaminv.advancedmachines.util.material.MaterialBase;
import jaminv.advancedmachines.util.message.RedstoneStateMessage;
import jaminv.advancedmachines.util.message.RedstoneStateMessage.RedstoneStateMessageHandler;
import jaminv.advancedmachines.util.models.BlockstateInventory;
import jaminv.advancedmachines.util.models.BlockstateMaker;
import jaminv.advancedmachines.util.models.BlockstateMaterial;
import jaminv.advancedmachines.util.models.BlockstateMaterialFace;
import jaminv.advancedmachines.util.models.BlockstateMachine;
import jaminv.advancedmachines.util.models.ModelRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber
public class CommonProxy {
	private static final Class RedstoneStateMessage = null;
	public static Configuration config;
	
	public void preInit(FMLPreInitializationEvent e) {
		File directory = e.getModConfigurationDirectory();
		config = new Configuration(new File(directory.getPath(), "advancedmachines.cfg"));
		Config.readConfig();
		
		RegistryHandler.otherRegistries();
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
