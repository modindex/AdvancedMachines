package jaminv.advancedmachines.util.recipe.melter;

import java.util.List;

import jaminv.advancedmachines.lib.recipe.RecipeBase;
import jaminv.advancedmachines.lib.recipe.RecipeManager;
import jaminv.advancedmachines.util.ModConfig;
import jaminv.advancedmachines.util.parser.DataParser;

public class MelterManager {
	
	public static class MelterRecipe extends RecipeBase {
		@Override
		public int getInputCount() { return 1; }

		@Override
		public int getOutputCount() { return 1; }
		
		public MelterRecipe(String id, int energy) {
			super(id, energy, ModConfig.general.processTimeBasic);
		}
	}
	
	public static class MelterRecipeManager extends RecipeManager<MelterRecipe> {
		@Override
		protected void addRecipe(MelterRecipe recipe) {
			super.addRecipe(recipe);
		}
	}
	private static MelterRecipeManager manager = new MelterRecipeManager();
	
	public static MelterRecipeManager getRecipeManager() { return manager; }
	public static List<MelterRecipe> getRecipeList() { return manager.getRecipeList(); }

	public static void init() {
		DataParser.parseFolder("data/recipes/melter", new FileHandlerMelterRecipe());
	}
}
