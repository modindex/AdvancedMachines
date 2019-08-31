package jaminv.advancedmachines.util.recipe.stabilizer;

import java.util.List;

import jaminv.advancedmachines.ModConfig;
import jaminv.advancedmachines.lib.recipe.RecipeBase;
import jaminv.advancedmachines.lib.recipe.RecipeManager;
import jaminv.advancedmachines.lib.util.parser.DataParser;

public class StabilizerManager {
	
	public static class StabilizerRecipe extends RecipeBase {
		@Override
		public int getInputCount() { return 1; }

		@Override
		public int getOutputCount() { return 1; }
		
		public StabilizerRecipe(String id, int energy) {
			super(id, energy, ModConfig.general.processTimeBasic);
		}
	}
	
	public static class StabilizerRecipeManager extends RecipeManager<StabilizerRecipe> {
		@Override
		protected void addRecipe(StabilizerRecipe recipe) {
			super.addRecipe(recipe);
		}
	}
	private static StabilizerRecipeManager manager = new StabilizerRecipeManager();
	
	public static StabilizerRecipeManager getRecipeManager() { return manager; }
	public static List<StabilizerRecipe> getRecipeList() { return manager.getRecipeList(); }

	public static void init() {
		DataParser.parseFolder("data/recipes/stabilizer", new FileHandlerStabilizerRecipe());
	}
}
