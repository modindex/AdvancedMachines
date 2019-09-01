package jaminv.advancedmachines.util.recipe.grinder;

import java.util.List;

import jaminv.advancedmachines.ModConfig;
import jaminv.advancedmachines.lib.parser.DataParser;
import jaminv.advancedmachines.lib.recipe.RecipeImpl;
import jaminv.advancedmachines.lib.recipe.RecipeInput;
import jaminv.advancedmachines.lib.recipe.RecipeManager;
import jaminv.advancedmachines.lib.recipe.RecipeManagerImpl;
import jaminv.advancedmachines.lib.recipe.RecipeOutput;
import net.minecraftforge.oredict.OreDictionary;

public class GrinderManager {

	protected static RecipeManagerImpl<RecipeImpl> manager = new RecipeManagerImpl<>();
	
	public static RecipeManager getRecipeManager() { return manager; }
	public static List<RecipeImpl> getRecipeList() { return manager.getRecipeList(); }

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
		int time = ModConfig.general.processTimeBasic;
		
		if (OreDictionary.doesOreNameExist(gem)) {
			addRecipe(energy, ore, gem, 2, time);
			addRecipe(energy, oreNether, gem, 4, time);
			addRecipe(energy, oreEnd, gem, 4, time);
			addRecipe(energy / 2, gem, dust, 1, time);
		} else {
			addRecipe(energy, ore, dust, 2, time);
			addRecipe(energy, oreNether, dust, 4, time);
			addRecipe(energy, oreEnd, ingot, 4, time);
			addRecipe(energy / 2, ingot, dust, 1, time);
		}
	}
	
	protected static void addRecipe(int energy, String oreInput, String oreOutput, int count, int time) {
		RecipeImpl recipe = new RecipeImpl("auto." + oreInput, energy, time);
		recipe.addInput(new RecipeInput(oreInput));
		recipe.addOutput(new RecipeOutput(oreOutput, count));
		manager.addRecipe(recipe);
	}
}
