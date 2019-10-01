package jaminv.advancedmachines.init.handlers;

import jaminv.advancedmachines.ModReference;
import jaminv.advancedmachines.init.init.BlockInit;
import jaminv.advancedmachines.init.init.FluidInit;
import jaminv.advancedmachines.init.init.ItemInit;
import jaminv.advancedmachines.lib.util.registry.RegistryHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = ModReference.MODID)
public class EventHandlerCommon {

	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {
		ItemInit.init();		
		RegistryHelper.registerItems(event);
	}
	
	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event) {
		BlockInit.init();
		FluidInit.init();		
		RegistryHelper.registerBlocks(event);
	}
	
	@SubscribeEvent
	public static void onRecipeRegister(RegistryEvent.Register<IRecipe> event) {
		//RecipeInit.init();
	}
}
