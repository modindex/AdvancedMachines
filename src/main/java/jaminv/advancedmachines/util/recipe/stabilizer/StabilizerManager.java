package jaminv.advancedmachines.util.recipe.stabilizer;

import java.util.List;

import jaminv.advancedmachines.lib.parser.DataParser;
import jaminv.advancedmachines.lib.recipe.RecipeImpl;
import jaminv.advancedmachines.lib.recipe.RecipeManager;
import jaminv.advancedmachines.lib.recipe.RecipeManagerImpl;

public class StabilizerManager {
	protected static RecipeManagerImpl<RecipeImpl> manager = new RecipeManagerImpl<>();
	
	public static RecipeManager getRecipeManager() { return manager; }
	public static List<RecipeImpl> getRecipeList() { return manager.getRecipeList(); }

	public static void init() {
		DataParser.parseFolder("data/recipes/stabilizer", new FileHandlerStabilizerRecipe());
	}
}
