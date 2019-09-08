package jaminv.advancedmachines.machine.instance.furnace;

import java.util.List;
import java.util.Map;

import jaminv.advancedmachines.ModConfig;
import jaminv.advancedmachines.lib.parser.DataParser;
import jaminv.advancedmachines.lib.parser.FileHandlerRecipe;
import jaminv.advancedmachines.lib.parser.FileHandlerRecipe.IngredientType;
import jaminv.advancedmachines.lib.parser.FileHandlerRecipe.RecipeSection;
import jaminv.advancedmachines.lib.recipe.RecipeImpl;
import jaminv.advancedmachines.lib.recipe.RecipeInput;
import jaminv.advancedmachines.lib.recipe.RecipeManager;
import jaminv.advancedmachines.lib.recipe.RecipeManagerImpl;
import jaminv.advancedmachines.lib.recipe.RecipeOutput;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class FurnaceManager {
	protected static RecipeManagerImpl<RecipeImpl> manager = new RecipeManagerImpl<>();
	
	public static RecipeManager getRecipeManager() { return manager; }
	public static List<RecipeImpl> getRecipeList() { return manager.getRecipeList(); }
	
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
				energy *= 14/20.0f;
			}
			
			manager.addRecipe(new RecipeImpl("smelting_list." + key.getItem().getUnlocalizedName(), energy, ModConfig.general.processTimeBasic)
				.addInput(new RecipeInput(key)).addOutput(new RecipeOutput(output)));
		}

		DataParser.parseFolder("data/recipes/furnace", new FileHandlerRecipe("furnace", recipe -> {
			FurnaceManager.manager.addRecipe(recipe);
			
			for (ItemStack stack : recipe.getInput(0).getItems()) {
				GameRegistry.addSmelting(stack, recipe.getOutput(0).toItemStack(), recipe.getXp());
			}			
		}).setLimit(RecipeSection.INPUT, IngredientType.ITEM, 1).setLimit(RecipeSection.OUTPUT, IngredientType.ITEM, 1));	
	}
}
