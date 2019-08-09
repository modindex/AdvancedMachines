package jaminv.advancedmachines.util.recipe.machine.melter;

import com.google.gson.JsonObject;

import jaminv.advancedmachines.lib.recipe.RecipeInput;
import jaminv.advancedmachines.lib.recipe.RecipeOutput;
import jaminv.advancedmachines.util.ModConfig;
import jaminv.advancedmachines.util.Reference;
import jaminv.advancedmachines.util.conditions.ConfigConditionFactory;
import jaminv.advancedmachines.util.conditions.OreDictionaryConditionFactory;
import jaminv.advancedmachines.util.logger.Logger;
import jaminv.advancedmachines.util.parser.DataParserException;
import jaminv.advancedmachines.util.parser.FileHandlerRecipe;
import jaminv.advancedmachines.util.recipe.machine.grinder.GrinderManager.GrinderRecipe;
import jaminv.advancedmachines.util.recipe.machine.melter.MelterManager.MelterRecipe;

public class FileHandlerMelterRecipe extends FileHandlerRecipe {

	@Override
	protected boolean parseRecipe(Logger logger, String filename, String path, JsonObject recipe) throws DataParserException {
		logger = logger.getLogger("melter");		
		logger.info("Parsing recipe '" + path + "'.");
		
		RecipeInput input = parseInput(recipe.get("input"), "input");
		RecipeOutput output = new RecipeOutput(parseFluidStack(recipe.get("output"), "output"));
		int energy = getEnergy(recipe, ModConfig.general.defaultGrinderEnergyCost);
		
		if (input == null || input.isEmpty() || output == null || output.isEmpty()) { return false; }
		if (!checkConditions(recipe, "conditions", logger)) { return false; }
		
		MelterRecipe rec = new MelterRecipe(filename + "." + path, energy);
		rec.setInput(input).setOutput(output);

		MelterManager.getRecipeManager().addRecipe(rec);		
		
		return true; 
	}

}
