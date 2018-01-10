package jaminv.advancedmachines.util.recipe.machine;

import java.util.HashMap;
import java.util.Map;

import jaminv.advancedmachines.util.recipe.RecipeBase;
import jaminv.advancedmachines.util.recipe.RecipeInput;
import jaminv.advancedmachines.util.recipe.RecipeManagerSimple;
import jaminv.advancedmachines.util.recipe.RecipeOutput;

public class PurifierManager {
	
	public static class PurifierRecipe extends RecipeBase {
		@Override
		public int getInputCount() { return 1; }

		@Override
		public int getOutputCount() { return 1; }
		
		public PurifierRecipe(int energy) {
			super(energy);
		}
	}
	
	public static class PurifierRecipeManager extends RecipeManagerSimple<PurifierRecipe> {
		@Override
		protected void addRecipe(PurifierRecipe recipe) {
			super.addRecipe(recipe);
		}
	}
	private static PurifierRecipeManager manager = new PurifierRecipeManager();
	
	public static PurifierRecipeManager getRecipeManager() { return manager; }

	public static void init() {
		manager.addRecipe((PurifierRecipe)new PurifierRecipe(30000)
			.setInput("dustCopper", 2)
			.setOutput("dustCopperPure")
			.addSecondary(new RecipeOutput("dustIron").withChance(20))
			.addSecondary(new RecipeOutput("dustGold").withChance(5))
			.addSecondary(new RecipeOutput("dustNickel").withChance(5))
			.addSecondary(new RecipeOutput("dustSilver").withChance(10))
		);
		
		manager.addRecipe((PurifierRecipe)new PurifierRecipe(30000)
			.setInput("dustGold", 2)
			.setOutput("dustGoldPure")
			.addSecondary(new RecipeOutput("dustSilver").withChance(20))
			.addSecondary(new RecipeOutput("dustPlatinum").withChance(5))
		);
		
		manager.addRecipe((PurifierRecipe)new PurifierRecipe(30000)
			.setInput("dustSilver", 2)
			.setOutput("dustSilverPure")
			.addSecondary(new RecipeOutput("dustLead").withChance(10))
			.addSecondary(new RecipeOutput("dustCopper").withChance(15))
		);
		
		manager.addRecipe((PurifierRecipe)new PurifierRecipe(45000)
			.setInput("dustDiamond")
			.setOutput("dustDiamondPure")
			.addSecondary(new RecipeOutput("dustCoal").withChance(40))
		);
	}
}
