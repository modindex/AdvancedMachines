package jaminv.advancedmachines.util.recipe.machine.purifier;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import jaminv.advancedmachines.util.ModConfig;
import jaminv.advancedmachines.util.logger.Logger;
import jaminv.advancedmachines.util.parser.DataParserException;
import jaminv.advancedmachines.util.parser.FileHandlerRecipe;
import jaminv.advancedmachines.util.recipe.RecipeInput;
import jaminv.advancedmachines.util.recipe.RecipeOutput;
import jaminv.advancedmachines.util.recipe.machine.purifier.PurifierManager.PurifierRecipe;

public class FileHandlerPurifierRecipe extends FileHandlerRecipe {

	@Override
	protected boolean parseRecipe(Logger logger, String filename, String path, JsonObject json) throws DataParserException {
		logger = logger.getLogger("purifier");
		RecipeInput input = parseInput(logger, path + ".input", getObject(path, json, "input", true));
		RecipeOutput output = parseOutput(logger, path + ".output", getObject(path, json, "output", true));
		int energy = getEnergy(path, json, ModConfig.general.defaultGrinderEnergy);
		
		if (input == null || output == null) { return false; }
		
		PurifierRecipe recipe = new PurifierRecipe(filename + "." + path, energy);
		recipe.setInput(input);
		recipe.setOutput(output);
		
		JsonArray secondary = getArray(path, json, "secondary", false);
		for(JsonElement element : secondary) {
			RecipeOutput sec = parseOutputWithChance(logger, path + ".secondary", assertObject(path + ".secondary", element));
			if (sec == null || sec.isEmpty()) { continue; }
			recipe.addSecondary(sec);
		}
		
		PurifierManager.getRecipeManager().addRecipe(recipe);
		
		return true; 
	}

}
