package jaminv.advancedmachines.util.recipe.melter;

import java.util.List;

import jaminv.advancedmachines.ModConfig;
import jaminv.advancedmachines.lib.recipe.IRecipeManager;
import jaminv.advancedmachines.lib.recipe.RecipeBase;
import jaminv.advancedmachines.lib.recipe.RecipeManager;
import jaminv.advancedmachines.lib.util.parser.DataParser;

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
	
	protected static RecipeManager<MelterRecipe> manager = new RecipeManager<>();
	
	public static IRecipeManager getRecipeManager() { return manager; }
	public static List<MelterRecipe> getRecipeList() { return manager.getRecipeList(); }

	public static void init() {
		DataParser.parseFolder("data/recipes/melter", new FileHandlerMelterRecipe());
	}
}
