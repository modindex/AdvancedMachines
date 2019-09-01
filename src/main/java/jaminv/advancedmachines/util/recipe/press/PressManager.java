package jaminv.advancedmachines.util.recipe.press;

import java.util.List;

import jaminv.advancedmachines.lib.parser.DataParser;
import jaminv.advancedmachines.lib.recipe.RecipeManager;
import jaminv.advancedmachines.lib.recipe.RecipeImpl;
import jaminv.advancedmachines.lib.recipe.RecipeManagerImpl;

public class PressManager {
	
	public static class PressRecipe extends RecipeImpl {
		@Override
		public int getInputCount() { return 3; }

		@Override
		public int getOutputCount() { return 1; }
		
		public PressRecipe(String id, int energy, int time) {
			super(id, energy, time);
		}
	}
	
	protected static RecipeManagerImpl<PressRecipe> manager = new RecipeManagerImpl<>();
	
	public static RecipeManager getRecipeManager() { return manager; }
	public static List<PressRecipe> getRecipeList() { return manager.getRecipeList(); }

	public static void init() {
		DataParser.parseFolder("data/recipes/press", new FileHandlerPressRecipe());
	}
}
