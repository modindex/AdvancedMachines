package jaminv.advancedmachines.util.recipe.stabilizer;

import java.util.List;

import jaminv.advancedmachines.ModConfig;
import jaminv.advancedmachines.lib.parser.DataParser;
import jaminv.advancedmachines.lib.recipe.RecipeManager;
import jaminv.advancedmachines.lib.recipe.RecipeImpl;
import jaminv.advancedmachines.lib.recipe.RecipeManagerImpl;

public class StabilizerManager {
	
	public static class StabilizerRecipe extends RecipeImpl {
		@Override
		public int getInputCount() { return 1; }

		@Override
		public int getOutputCount() { return 1; }
		
		public StabilizerRecipe(String id, int energy) {
			super(id, energy, ModConfig.general.processTimeBasic);
		}
	}

	protected static RecipeManagerImpl<StabilizerRecipe> manager = new RecipeManagerImpl<>();
	
	public static RecipeManager getRecipeManager() { return manager; }
	public static List<StabilizerRecipe> getRecipeList() { return manager.getRecipeList(); }

	public static void init() {
		DataParser.parseFolder("data/recipes/stabilizer", new FileHandlerStabilizerRecipe());
	}
}
