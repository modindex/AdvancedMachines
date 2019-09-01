package jaminv.advancedmachines.util.recipe.furnace;

import java.util.List;
import java.util.Map;

import jaminv.advancedmachines.ModConfig;
import jaminv.advancedmachines.lib.parser.DataParser;
import jaminv.advancedmachines.lib.recipe.RecipeImpl;
import jaminv.advancedmachines.lib.recipe.RecipeInput;
import jaminv.advancedmachines.lib.recipe.RecipeManager;
import jaminv.advancedmachines.lib.recipe.RecipeManagerImpl;
import jaminv.advancedmachines.lib.recipe.RecipeOutput;
import jaminv.advancedmachines.util.helper.ItemHelper;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class FurnaceManager {
	protected static RecipeManagerImpl<RecipeImpl> manager = new RecipeManagerImpl<>();
	
	public static RecipeManager getRecipeManager() { return manager; }
	public static List<RecipeImpl> getRecipeList() { return manager.getRecipeList(); }

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
			if (ItemHelper.isDust(key) && ItemHelper.isIngot(output)) {
				energy *= 14/20.0f;
			}
			
			manager.addRecipe(new RecipeImpl("smelting_list." + key.getItem().getUnlocalizedName(), energy, ModConfig.general.processTimeBasic)
				.addInput(new RecipeInput(key)).addOutput(new RecipeOutput(output)));
		}

		DataParser.parseFolder("data/recipes/furnace", new FileHandlerFurnaceRecipe());	
	}
}
