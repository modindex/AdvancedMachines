package jaminv.advancedmachines.util.recipe.machine.grinder;

import java.util.List;

import org.apache.commons.lang3.text.WordUtils;

import jaminv.advancedmachines.util.ModConfig;
import jaminv.advancedmachines.util.parser.DataParser;
import jaminv.advancedmachines.util.recipe.RecipeBase;
import jaminv.advancedmachines.util.recipe.RecipeInput;
import jaminv.advancedmachines.util.recipe.RecipeManagerSimple;
import jaminv.advancedmachines.util.recipe.RecipeOutput;
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
		DataParser.parseFolder("data/recipes/grinder", new FileHandlerGrinderRecipe());

		if (ModConfig.recipe.scanGrinderOre) {
			for (String oreName : OreDictionary.getOreNames()) {
				String oreType;
				if (oreName.startsWith("ore") || oreName.startsWith("gem")) {
					oreType = oreName.substring(3);
					addRecipes(oreType);
				} else if (oreName.startsWith("dust")) {
					oreType = oreName.substring(4);
					addRecipes(oreType);
				}
			}
		}
	}
	
	protected static void addRecipes(String oreType) {
		if (oreType == null || oreType.isEmpty()) { return; }
		
		String ore = "ore" + oreType;
		String gem = "gem" + oreType;
		String dust = "dust" + oreType;
		String ingot = "ingot" + oreType;
		String oreNether = "oreNether" + oreType;
		String oreEnd = "oreEnd" + oreType;
		int energy = ModConfig.general.defaultGrinderEnergyCost;
		
		if (OreDictionary.doesOreNameExist(gem)) {
			addRecipe(energy, ore, gem, 2);
			addRecipe(energy, oreNether, gem, 4);
			addRecipe(energy, oreEnd, gem, 4);
			addRecipe(energy / 2, gem, dust, 1);
		} else {
			addRecipe(energy, ore, dust, 2);
			addRecipe(energy, oreNether, dust, 4);
			addRecipe(energy, oreEnd, ingot, 4);
			addRecipe(energy / 2, gem, ingot, 1);
		}
	}
	
	protected static void addRecipe(int energy, String oreInput, String oreOutput, int count) {
		GrinderRecipe recipe = new GrinderRecipe("auto." + oreInput, energy);
		recipe.setInput(new RecipeInput(oreInput));
		recipe.setOutput(new RecipeOutput(oreOutput, count));
		manager.addRecipe(recipe);
	}
}
