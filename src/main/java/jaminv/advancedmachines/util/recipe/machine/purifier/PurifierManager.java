package jaminv.advancedmachines.util.recipe.machine.purifier;

import java.util.List;

import jaminv.advancedmachines.util.parser.DataParser;
import jaminv.advancedmachines.util.recipe.RecipeBase;
import jaminv.advancedmachines.util.recipe.RecipeManager;

public class PurifierManager {
	
	public static class PurifierRecipe extends RecipeBase {
		@Override
		public int getInputCount() { return 1; }

		@Override
		public int getOutputCount() { return 1; }
		
		public PurifierRecipe(String id, int energy) {
			super(id, energy);
		}
	}
	
	public static class PurifierRecipeManager extends RecipeManager<PurifierRecipe> {
		@Override
		protected void addRecipe(PurifierRecipe recipe) {
			super.addRecipe(recipe);
		}
	}
	private static PurifierRecipeManager manager = new PurifierRecipeManager();
	
	public static PurifierRecipeManager getRecipeManager() { return manager; }
	public static List<PurifierRecipe> getRecipeList() { return manager.getRecipeList(); }

	public static void init() {
		DataParser.parseFolder("data/recipes/purifier", new FileHandlerPurifierRecipe());
	}
}
