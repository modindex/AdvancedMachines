package jaminv.advancedmachines.util.recipe.machine;

import java.util.HashMap;
import java.util.Map;

import jaminv.advancedmachines.util.recipe.RecipeBase;
import jaminv.advancedmachines.util.recipe.RecipeInput;
import jaminv.advancedmachines.util.recipe.RecipeManagerSimple;
import jaminv.advancedmachines.util.recipe.RecipeManagerThreeIngredient;
import jaminv.advancedmachines.util.recipe.RecipeOutput;

public class AlloyManager {
	
	public static class AlloyRecipe extends RecipeBase {
		@Override
		public int getInputCount() { return 3; }

		@Override
		public int getOutputCount() { return 1; }
		
		public AlloyRecipe(int energy) {
			super(energy);
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

	public static void init() {
		manager.addRecipe((AlloyRecipe)new AlloyRecipe(4000)
			.addInput(0, "dustIron")
			.addInput(1, "dustCoal", 4)
			.setOutput("ingotSteel")
		);
		
		manager.addRecipe((AlloyRecipe)new AlloyRecipe(4400)
			.addInput(0, "ingotIron")
			.addInput(1, "dustCoal", 4)
			.setOutput("ingotSteel")
		);
	}
}
