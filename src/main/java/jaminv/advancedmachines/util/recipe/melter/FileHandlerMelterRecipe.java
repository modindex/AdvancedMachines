package jaminv.advancedmachines.util.recipe.melter;

import com.google.gson.JsonObject;

import jaminv.advancedmachines.ModConfig;
import jaminv.advancedmachines.lib.recipe.RecipeInput;
import jaminv.advancedmachines.lib.recipe.RecipeOutput;
import jaminv.advancedmachines.lib.util.logger.Logger;
import jaminv.advancedmachines.lib.util.parser.DataParserException;
import jaminv.advancedmachines.lib.util.parser.FileHandlerRecipe;
import jaminv.advancedmachines.lib.util.parser.ParseUtils;
import jaminv.advancedmachines.util.recipe.melter.MelterManager.MelterRecipe;

public class FileHandlerMelterRecipe extends FileHandlerRecipe {

	@Override
	protected boolean parseRecipe(Logger logger, String filename, String path, JsonObject recipe) throws DataParserException {
		logger = logger.getLogger("melter");		
		logger.info("Parsing recipe '" + path + "'.");
		
		RecipeInput input = parseInput(recipe.get("input"), "input");
		RecipeOutput output = new RecipeOutput(ParseUtils.parseFluidStack(recipe.get("output"), "output"));
		int energy = getEnergy(recipe, ModConfig.general.defaultGrinderEnergyCost);
		
		if (input == null || input.isEmpty() || output == null || output.isEmpty()) { return false; }
		if (!checkConditions(recipe, "conditions", logger)) { return false; }
		
		MelterRecipe rec = new MelterRecipe(filename + "." + path, energy);
		rec.setInput(input).setOutput(output);

		MelterManager.manager.addRecipe(rec);		
		
		return true; 
	}

}
