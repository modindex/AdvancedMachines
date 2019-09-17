package jaminv.advancedmachines.machine.instance.injector;

import java.util.List;

import jaminv.advancedmachines.ModReference;
import jaminv.advancedmachines.lib.parser.DataParser;
import jaminv.advancedmachines.lib.parser.FileHandlerRecipe;
import jaminv.advancedmachines.lib.parser.FileHandlerRecipe.IngredientType;
import jaminv.advancedmachines.lib.parser.FileHandlerRecipe.RecipeSection;
import jaminv.advancedmachines.lib.recipe.MachineRecipe;
import jaminv.advancedmachines.lib.recipe.RecipeManager;
import jaminv.advancedmachines.lib.recipe.MachineRecipeManager;

public class InjectorManager {
	
	protected static MachineRecipeManager<MachineRecipe> manager = new MachineRecipeManager<>();
	
	public static RecipeManager getRecipeManager() { return manager; }
	public static List<MachineRecipe> getRecipeList() { return manager.getRecipeList(); }

	public static void init() {
		DataParser.parseFolder(ModReference.MODID, "data/recipes/injector", new FileHandlerRecipe("injector", (recipe) -> { 
			manager.addRecipe(recipe); 
		}).setLimit(RecipeSection.INPUT, IngredientType.ITEM, 1).setLimit(RecipeSection.INPUT, IngredientType.FLUID, 1)
			.setLimit(RecipeSection.OUTPUT, IngredientType.ITEM, 1)
		);
	}
}
