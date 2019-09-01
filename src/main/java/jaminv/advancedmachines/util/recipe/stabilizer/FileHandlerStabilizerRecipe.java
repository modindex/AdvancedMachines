package jaminv.advancedmachines.util.recipe.stabilizer;

import com.google.gson.JsonObject;

import jaminv.advancedmachines.ModConfig;
import jaminv.advancedmachines.lib.parser.DataParserException;
import jaminv.advancedmachines.lib.parser.FileHandlerRecipe;
import jaminv.advancedmachines.lib.parser.ParseUtils;
import jaminv.advancedmachines.lib.recipe.RecipeImpl;
import jaminv.advancedmachines.lib.recipe.RecipeInput;
import jaminv.advancedmachines.lib.recipe.RecipeOutput;
import jaminv.advancedmachines.lib.util.logger.Logger;

public class FileHandlerStabilizerRecipe extends FileHandlerRecipe {

	@Override
	protected boolean parseRecipe(Logger logger, String filename, String path, JsonObject json) throws DataParserException {
		logger = logger.getLogger("stabilizer");		
		logger.info("Parsing recipe '" + path + "'.");
		
		RecipeInput input = new RecipeInput(ParseUtils.parseFluidStack(json.get("input"), "input"));
		RecipeOutput output = parseOutput(json.get("output"), "output");
		int energy = getEnergy(json, ModConfig.general.defaultGrinderEnergyCost);
		int time = getTime(json, ModConfig.general.processTimeBasic);
		
		if (input == null || input.isEmpty() || output == null || output.isEmpty()) { return false; }
		if (!checkConditions(json, "conditions", logger)) { return false; }
		
		RecipeImpl recipe = new RecipeImpl(filename + "." + path, energy, time);
		recipe.addInput(input).addOutput(output);

		StabilizerManager.manager.addRecipe(recipe);		
		
		return true; 
	}

}
