package jaminv.advancedmachines.util.recipe.machine.grinder;

import com.google.gson.JsonObject;

import jaminv.advancedmachines.util.ModConfig;
import jaminv.advancedmachines.util.logger.Logger;
import jaminv.advancedmachines.util.parser.DataParserException;
import jaminv.advancedmachines.util.parser.FileHandlerRecipe;
import jaminv.advancedmachines.util.recipe.RecipeInput;
import jaminv.advancedmachines.util.recipe.RecipeOutput;
import jaminv.advancedmachines.util.recipe.machine.grinder.GrinderManager.GrinderRecipe;

public class FileHandlerGrinderRecipe extends FileHandlerRecipe {

	@Override
	protected boolean parseRecipe(Logger logger, String filename, String path, JsonObject recipe) throws DataParserException {
		logger = logger.getLogger("grinder");
		RecipeInput input = parseInput(logger, path + ".input", getObject(path, recipe, "input", true));
		RecipeOutput output = parseOutput(logger, path + ".output", getObject(path, recipe, "output", true));
		RecipeOutput secondary = parseOutputWithChance(logger, path + ".secondary", getObject(path, recipe, "secondary", false));
		int energy = getEnergy(path, recipe, ModConfig.general.defaultGrinderEnergy);
		
		if (input == null || output == null) { return false; }
		
		GrinderManager.getRecipeManager().addRecipe(
			(GrinderRecipe)new GrinderRecipe(filename + "." + path, energy)
				.setInput(input)
				.setOutput(output)
				.addSecondary(secondary)
		);		
		
		return true; 
	}

}
