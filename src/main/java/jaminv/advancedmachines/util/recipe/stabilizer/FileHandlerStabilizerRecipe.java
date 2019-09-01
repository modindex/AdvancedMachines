package jaminv.advancedmachines.util.recipe.stabilizer;

import com.google.gson.JsonObject;

import jaminv.advancedmachines.ModConfig;
import jaminv.advancedmachines.lib.parser.DataParserException;
import jaminv.advancedmachines.lib.parser.FileHandlerRecipe;
import jaminv.advancedmachines.lib.parser.ParseUtils;
import jaminv.advancedmachines.lib.recipe.RecipeInput;
import jaminv.advancedmachines.lib.recipe.RecipeOutput;
import jaminv.advancedmachines.lib.util.logger.Logger;
import jaminv.advancedmachines.util.recipe.stabilizer.StabilizerManager.StabilizerRecipe;

public class FileHandlerStabilizerRecipe extends FileHandlerRecipe {

	@Override
	protected boolean parseRecipe(Logger logger, String filename, String path, JsonObject recipe) throws DataParserException {
		logger = logger.getLogger("stabilizer");		
		logger.info("Parsing recipe '" + path + "'.");
		
		RecipeInput input = new RecipeInput(ParseUtils.parseFluidStack(recipe.get("input"), "input"));
		RecipeOutput output = parseOutput(recipe.get("output"), "output");
		int energy = getEnergy(recipe, ModConfig.general.defaultGrinderEnergyCost);
		
		if (input == null || input.isEmpty() || output == null || output.isEmpty()) { return false; }
		if (!checkConditions(recipe, "conditions", logger)) { return false; }
		
		StabilizerRecipe rec = new StabilizerRecipe(filename + "." + path, energy);
		rec.addInput(input).addOutput(output);

		StabilizerManager.manager.addRecipe(rec);		
		
		return true; 
	}

}
