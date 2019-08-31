package jaminv.advancedmachines.util.recipe.injector;

import java.util.List;

import jaminv.advancedmachines.lib.recipe.RecipeBase;
import jaminv.advancedmachines.lib.recipe.RecipeManager;
import jaminv.advancedmachines.lib.util.parser.DataParser;
import jaminv.advancedmachines.util.ModConfig;

public class InjectorManager {
	
	public static class InjectorRecipe extends RecipeBase {
		@Override
		public int getInputCount() { return 2; }

		@Override
		public int getOutputCount() { return 1; }
		
		public InjectorRecipe(String id, int energy, int time) {
			super(id, energy, time);
		}
	}
	
	public static class InjectorRecipeManager extends RecipeManager<InjectorRecipe> {
		@Override
		protected void addRecipe(InjectorRecipe recipe) {
			super.addRecipe(recipe);
		}
	}
	private static InjectorRecipeManager manager = new InjectorRecipeManager();
	
	public static InjectorRecipeManager getRecipeManager() { return manager; }
	public static List<InjectorRecipe> getRecipeList() { return manager.getRecipeList(); }

	public static void init() {
		DataParser.parseFolder("data/recipes/injector", new FileHandlerInjectorRecipe());
	}
}
