package jaminv.advancedmachines.util.recipe.injector;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import jaminv.advancedmachines.lib.recipe.RecipeInput;
import jaminv.advancedmachines.lib.recipe.RecipeOutput;
import jaminv.advancedmachines.util.ModConfig;
import jaminv.advancedmachines.util.logger.Logger;
import jaminv.advancedmachines.util.parser.DataParserException;
import jaminv.advancedmachines.util.parser.FileHandlerRecipe;
import jaminv.advancedmachines.util.parser.ParseUtils;
import jaminv.advancedmachines.util.recipe.injector.InjectorManager.InjectorRecipe;
import net.minecraft.util.JsonUtils;

public class FileHandlerInjectorRecipe extends FileHandlerRecipe {

	@Override
	protected boolean parseRecipe(Logger logger, String filename, String path, JsonObject json) throws DataParserException {
		logger = logger.getLogger("injector");		
		logger.info("Parsing recipe '" + path + "'.");

		int energy = getEnergy(json, ModConfig.general.defaultGrinderEnergyCost);
		int time = getTime(json, ModConfig.general.processTimeBasic);
		InjectorRecipe recipe = new InjectorRecipe(filename + "." + path, energy, time);
		
		// Input Loop
		List<RecipeInput> inputs = new ArrayList<RecipeInput>();
		JsonArray input = JsonUtils.getJsonArray(json, "input");
		int i = 0;
		for(JsonElement element : input) {
			String name = "input[" + i + "]"; 
			
			RecipeInput recipeInput;
			if (element.isJsonObject() && ((JsonObject)element).has("fluid")) {
				recipeInput = new RecipeInput(ParseUtils.parseFluidStack((JsonObject)element, name));
			} else {
				recipeInput = parseInput(element, name);
			}
			if (recipeInput == null || recipeInput.hasError()) { return false; }
			recipe.addInput(i, recipeInput);
			i++;
		}
		
		RecipeOutput output = parseOutput(json.get("output"), "output");
		if (output == null || output.isEmpty()) { return false; }
		recipe.setOutput(output);

		if (!checkConditions(json, "conditions", logger)) { return false; }
		InjectorManager.getRecipeManager().addRecipe(recipe);		
		
		return true; 
	}

}
