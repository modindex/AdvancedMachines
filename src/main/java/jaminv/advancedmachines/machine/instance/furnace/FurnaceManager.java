package jaminv.advancedmachines.machine.instance.furnace;

import java.util.List;
import java.util.Map;

import jaminv.advancedmachines.ModConfig;
import jaminv.advancedmachines.ModReference;
import jaminv.advancedmachines.lib.parser.DataParser;
import jaminv.advancedmachines.lib.parser.FileHandler;
import jaminv.advancedmachines.lib.parser.FileHandlerRecipe;
import jaminv.advancedmachines.lib.parser.FileHandlerRecipe.IngredientType;
import jaminv.advancedmachines.lib.parser.FileHandlerRecipe.RecipeSection;
import jaminv.advancedmachines.lib.recipe.MachineRecipe;
import jaminv.advancedmachines.lib.recipe.MachineRecipeManager;
import jaminv.advancedmachines.lib.recipe.RecipeInput;
import jaminv.advancedmachines.lib.recipe.RecipeManager;
import jaminv.advancedmachines.lib.recipe.RecipeOutput;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class FurnaceManager {
	protected static MachineRecipeManager<MachineRecipe> manager = new MachineRecipeManager<>();
	
	public static RecipeManager getRecipeManager() { return manager; }
	public static List<MachineRecipe> getRecipeList() { return manager.getRecipeList(); }
	
	protected static String getOreName(ItemStack stack) {
		int[] ids = OreDictionary.getOreIDs(stack);
		if (ids != null && ids.length >= 1) {
			return OreDictionary.getOreName(ids[0]);
		}
		return "";
	}	

	public static void init() {
		Map<ItemStack, ItemStack> smeltingList = FurnaceRecipes.instance().getSmeltingList();
		for (ItemStack key : smeltingList.keySet()) {
			if (key.isEmpty() || manager.isItemValid(key, null)) {
				continue;
			}
			ItemStack output = smeltingList.get(key);
			if (output.isEmpty()) {
				continue;
			}
			int energy = ModConfig.general.defaultFurnaceEnergyCost;
			
			if (output.getItem() instanceof ItemFood) {
				energy /= 2;
			}
			if (getOreName(key).startsWith("dust") && getOreName(output).startsWith("ingot")) {
				energy *= 0.75;
			}
			
			manager.addRecipe(new MachineRecipe("smelting_list." + key.getItem().getUnlocalizedName(), energy, ModConfig.general.processTimeBasic)
				.addInput(new RecipeInput(key)).addOutput(new RecipeOutput(output)));
		}

		FileHandler handler = new FileHandlerRecipe("furnace", ModConfig.general.defaultFurnaceEnergyCost, recipe -> {
			if (ModConfig.doExclude("furnace", recipe.getRecipeId())) {	return;	}
			FurnaceManager.manager.addRecipe(recipe);
			
			for (ItemStack stack : recipe.getInput(0).getItems()) {
				GameRegistry.addSmelting(stack, recipe.getOutput(0).toItemStack(), recipe.getXp());
			}			
		}).setLimit(RecipeSection.INPUT, IngredientType.ITEM, 1).setLimit(RecipeSection.OUTPUT, IngredientType.ITEM, 1);
		
		DataParser.parseJarFolder(ModReference.MODID, "data/recipes/furnace", handler);
		DataParser.parseConfigFolder(ModReference.MODID, "data/furnace", handler);
	}	
}
