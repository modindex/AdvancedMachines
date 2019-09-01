package jaminv.advancedmachines.util.recipe.purifier;

import java.util.List;

import jaminv.advancedmachines.ModConfig;
import jaminv.advancedmachines.lib.parser.DataParser;
import jaminv.advancedmachines.lib.recipe.IRecipeManager;
import jaminv.advancedmachines.lib.recipe.RecipeBase;
import jaminv.advancedmachines.lib.recipe.RecipeManager;

public class PurifierManager {
	
	public static class PurifierRecipe extends RecipeBase {
		@Override
		public int getInputCount() { return 1; }

		@Override
		public int getOutputCount() { return 1; }
		
		public PurifierRecipe(String id, int energy) {
			super(id, energy, ModConfig.general.processTimeBasic);
		}
	}
	
	protected static RecipeManager<PurifierRecipe> manager = new RecipeManager<>();
	
	public static IRecipeManager getRecipeManager() { return manager; }
	public static List<PurifierRecipe> getRecipeList() { return manager.getRecipeList(); }

	public static void init() {
		DataParser.parseFolder("data/recipes/purifier", new FileHandlerPurifierRecipe());
	}
}
