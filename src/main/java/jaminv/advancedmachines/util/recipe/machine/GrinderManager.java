package jaminv.advancedmachines.util.recipe.machine;

import java.util.List;

import jaminv.advancedmachines.util.Config;
import jaminv.advancedmachines.util.recipe.RecipeBase;
import jaminv.advancedmachines.util.recipe.RecipeInput;
import jaminv.advancedmachines.util.recipe.RecipeManagerSimple;
import jaminv.advancedmachines.util.recipe.RecipeOutput;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class GrinderManager {
	
	public static class GrinderRecipe extends RecipeBase {
		@Override
		public int getInputCount() { return 1; }

		@Override
		public int getOutputCount() { return 1; }
		
		public GrinderRecipe(int energy) {
			super(energy);
		}
	}
	
	public static class GrinderRecipeManager extends RecipeManagerSimple<GrinderRecipe> {
		@Override
		protected void addRecipe(GrinderRecipe recipe) {
			super.addRecipe(recipe);
		}
	}
	private static GrinderRecipeManager manager = new GrinderRecipeManager();
	
	public static GrinderRecipeManager getRecipeManager() { return manager; }
	public static List<GrinderRecipe> getRecipeList() { return manager.getRecipeList(); }

	public static void init() {
		manager.addRecipe((GrinderRecipe)new GrinderRecipe(Config.defaultGrinderEnergy)
			.setInput("ingotGold")
			.setOutput("dustGold")
		);

		manager.addRecipe((GrinderRecipe)new GrinderRecipe(Config.defaultGrinderEnergy)
			.setInput(new RecipeInput(Item.REGISTRY.getObject(new ResourceLocation("minecraft:coal"))))
			.setOutput("dustCoal")
		);
		
		manager.addRecipe((GrinderRecipe)new GrinderRecipe(Config.defaultFurnaceEnergy)
			.setInput(new RecipeInput(Item.REGISTRY.getObject(new ResourceLocation("minecraft:ender_pearl"))))
			.setOutput("dustEnder")
		);
	}
}
