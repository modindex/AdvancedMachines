package jaminv.advancedmachines.util.recipe.melter;

import java.util.List;

import jaminv.advancedmachines.lib.parser.DataParser;
import jaminv.advancedmachines.lib.recipe.RecipeImpl;
import jaminv.advancedmachines.lib.recipe.RecipeManager;
import jaminv.advancedmachines.lib.recipe.RecipeManagerImpl;

public class MelterManager {

	protected static RecipeManagerImpl<RecipeImpl> manager = new RecipeManagerImpl<>();
	
	public static RecipeManager getRecipeManager() { return manager; }
	public static List<RecipeImpl> getRecipeList() { return manager.getRecipeList(); }

	public static void init() {
		DataParser.parseFolder("data/recipes/melter", new FileHandlerMelterRecipe());
	}
}
