package jaminv.advancedmachines.util.recipe;

import java.util.List;
import java.util.Map;

import jaminv.advancedmachines.lib.recipe.RecipeBase;
import jaminv.advancedmachines.lib.recipe.RecipeManager;
import jaminv.advancedmachines.util.ModConfig;
import jaminv.advancedmachines.util.helper.ItemHelper;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class FurnaceManager {
	
	public static class FurnaceRecipe extends RecipeBase {
		@Override
		public int getInputCount() { return 1; }

		@Override
		public int getOutputCount() { return 1; }
		
		public FurnaceRecipe(String id, int energy) {
			super(id, energy, ModConfig.general.processTimeBasic);
		}
	}
	
	public static class FurnaceRecipeManager extends RecipeManager<FurnaceRecipe> {
		@Override
		protected void addRecipe(FurnaceRecipe recipe) {
			super.addRecipe(recipe);
		}
	}
	private static FurnaceRecipeManager manager = new FurnaceRecipeManager();
	
	public static FurnaceRecipeManager getRecipeManager() { return manager; }
	public static List<FurnaceRecipe> getRecipeList() { return manager.getRecipeList(); }

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
			
			manager.addRecipe((FurnaceRecipe)new FurnaceRecipe("smelting_list." + key.getItem().getUnlocalizedName(), energy).setInput(key).setOutput(output));
		}
	}
}
