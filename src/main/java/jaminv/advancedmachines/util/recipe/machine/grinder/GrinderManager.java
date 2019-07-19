package jaminv.advancedmachines.util.recipe.machine.grinder;

import java.util.List;

import jaminv.advancedmachines.util.ModConfig;
import jaminv.advancedmachines.util.parser.DataParser;
import jaminv.advancedmachines.util.recipe.RecipeBase;
import jaminv.advancedmachines.util.recipe.RecipeManagerSimple;
import net.minecraftforge.oredict.OreDictionary;

public class GrinderManager {
	
	public static class GrinderRecipe extends RecipeBase {
		@Override
		public int getInputCount() { return 1; }

		@Override
		public int getOutputCount() { return 1; }
		
		public GrinderRecipe(String id, int energy) {
			super(id, energy);
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
		DataParser.addFolder("data/recipes/grinder", new FileHandlerGrinderRecipe());
		
		if (ModConfig.recipe.scanGrinderOre) {
			for (String oreName : OreDictionary.getOreNames()) {
				String oreType;
				if (oreName.startsWith("ore") || oreName.startsWith("gem")) {
					oreType = oreName.substring(3);
					
				}
			}
		}
	}
}
