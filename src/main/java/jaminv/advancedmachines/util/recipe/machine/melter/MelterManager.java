package jaminv.advancedmachines.util.recipe.machine.melter;

import java.util.List;

import org.apache.commons.lang3.text.WordUtils;

import jaminv.advancedmachines.util.ModConfig;
import jaminv.advancedmachines.util.parser.DataParser;
import jaminv.advancedmachines.util.recipe.RecipeBase;
import jaminv.advancedmachines.util.recipe.RecipeInput;
import jaminv.advancedmachines.util.recipe.RecipeManagerSimple;
import jaminv.advancedmachines.util.recipe.RecipeOutput;
import net.minecraftforge.oredict.OreDictionary;

public class MelterManager {
	
	public static class MelterRecipe extends RecipeBase {
		@Override
		public int getInputCount() { return 1; }

		@Override
		public int getOutputCount() { return 1; }
		
		public MelterRecipe(String id, int energy) {
			super(id, energy);
		}
	}
	
	public static class MelterRecipeManager extends RecipeManagerSimple<MelterRecipe> {
		@Override
		protected void addRecipe(MelterRecipe recipe) {
			super.addRecipe(recipe);
		}
	}
	private static MelterRecipeManager manager = new MelterRecipeManager();
	
	public static MelterRecipeManager getRecipeManager() { return manager; }
	public static List<MelterRecipe> getRecipeList() { return manager.getRecipeList(); }

	public static void init() {
		DataParser.parseFolder("data/recipes/melter", new FileHandlerMelterRecipe());
	}
}
