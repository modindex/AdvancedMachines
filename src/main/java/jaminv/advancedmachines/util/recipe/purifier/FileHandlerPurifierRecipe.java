package jaminv.advancedmachines.util.recipe.purifier;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import jaminv.advancedmachines.ModConfig;
import jaminv.advancedmachines.lib.parser.DataParserException;
import jaminv.advancedmachines.lib.parser.FileHandlerRecipe;
import jaminv.advancedmachines.lib.recipe.RecipeInput;
import jaminv.advancedmachines.lib.recipe.RecipeOutput;
import jaminv.advancedmachines.lib.util.logger.Logger;
import jaminv.advancedmachines.util.recipe.purifier.PurifierManager.PurifierRecipe;
import net.minecraft.util.JsonUtils;

public class FileHandlerPurifierRecipe extends FileHandlerRecipe {

	@Override
	protected boolean parseRecipe(Logger logger, String filename, String path, JsonObject json) throws DataParserException {
		logger = logger.getLogger("purifier");
		RecipeInput input = parseInput(json.get("input"), "input");
		RecipeOutput output = parseOutput(json.get("output"), "output");
		int energy = getEnergy(json, ModConfig.general.defaultGrinderEnergyCost);
		
		if (input == null || input.isEmpty() || output == null || output.isEmpty()) { return false; }
		if (!checkConditions(json, "conditions", logger)) { return false; }
		
		PurifierRecipe recipe = new PurifierRecipe(filename + "." + path, energy);
		recipe.addInput(input);
		recipe.addOutput(output);
		
		JsonArray secondary = JsonUtils.getJsonArray(json, "secondary", null);
		if (secondary != null) {
			int i = 0;
			for(JsonElement element : secondary) {
				RecipeOutput sec = parseOutputWithChance(element, "secondary[" + i + "]");
				if (sec == null || sec.isEmpty()) { continue; }
				recipe.addSecondary(sec);
				
				i++;
			}
		}
		
		PurifierManager.manager.addRecipe(recipe);
		
		return true; 
	}

}
