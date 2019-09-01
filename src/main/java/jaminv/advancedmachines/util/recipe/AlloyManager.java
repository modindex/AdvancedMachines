package jaminv.advancedmachines.util.recipe;

import java.util.List;

import jaminv.advancedmachines.lib.recipe.RecipeImpl;
import jaminv.advancedmachines.lib.recipe.RecipeManager;
import jaminv.advancedmachines.lib.recipe.RecipeManagerImpl;

public class AlloyManager {
	protected static RecipeManagerImpl<RecipeImpl> manager = new RecipeManagerImpl<>();
	
	public static RecipeManager getRecipeManager() { return manager; }
	public static List<RecipeImpl> getRecipeList() { return manager.getRecipeList(); }

	public static void init() {
/*		manager.addRecipe((AlloyRecipe)new AlloyRecipe("1", 4000)
			.addInput(0, "dustIron")
			.addInput(1, "dustCoal", 4)
			.setOutput("ingotSteel")
		);
		
		manager.addRecipe((AlloyRecipe)new AlloyRecipe("2", 4400)
			.addInput(0, "ingotIron")
			.addInput(1, "dustCoal", 4)
			.setOutput("ingotSteel")
		);
		
		manager.addRecipe((AlloyRecipe)new AlloyRecipe("3", 8000)
			.addInput(0, "ingotTitanium")
			.addInput(1, "dustDiamondPure")
			.setOutput("ingotTitaniumCarbide")
		);*/
	}
}
