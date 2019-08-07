package jaminv.advancedmachines.util.recipe.machine;

import java.util.List;

import jaminv.advancedmachines.util.recipe.RecipeBase;
import jaminv.advancedmachines.util.recipe.RecipeManagerThreeIngredient;

public class AlloyManager {
	
	public static class AlloyRecipe extends RecipeBase {
		@Override
		public int getInputCount() { return 3; }

		@Override
		public int getOutputCount() { return 1; }
		
		public AlloyRecipe(String id, int energy) {
			super(id, energy);
		}
	}
	
	public static class AlloyRecipeManager extends RecipeManagerThreeIngredient<AlloyRecipe> {
		@Override
		protected void addRecipe(AlloyRecipe recipe) {
			super.addRecipe(recipe);
		}
	}
	private static AlloyRecipeManager manager = new AlloyRecipeManager();
	
	public static AlloyRecipeManager getRecipeManager() { return manager; }
	public static List<AlloyRecipe> getRecipeList() { return manager.getRecipeList(); }

	public static void init() {
		manager.addRecipe((AlloyRecipe)new AlloyRecipe("1", 4000)
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
		);
	}
}
