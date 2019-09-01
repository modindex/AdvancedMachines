package jaminv.advancedmachines.util.recipe.injector;

import java.util.List;

import jaminv.advancedmachines.lib.parser.DataParser;
import jaminv.advancedmachines.lib.recipe.RecipeManager;
import jaminv.advancedmachines.lib.recipe.RecipeImpl;
import jaminv.advancedmachines.lib.recipe.RecipeManagerImpl;

public class InjectorManager {
	
	public static class InjectorRecipe extends RecipeImpl {
		@Override
		public int getInputCount() { return 2; }

		@Override
		public int getOutputCount() { return 1; }
		
		public InjectorRecipe(String id, int energy, int time) {
			super(id, energy, time);
		}
	}
	
	protected static RecipeManagerImpl<InjectorRecipe> manager = new RecipeManagerImpl<>();
	
	public static RecipeManager getRecipeManager() { return manager; }
	public static List<InjectorRecipe> getRecipeList() { return manager.getRecipeList(); }

	public static void init() {
		DataParser.parseFolder("data/recipes/injector", new FileHandlerInjectorRecipe());
	}
}
