package jaminv.advancedmachines.util.recipe.press;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import jaminv.advancedmachines.ModConfig;
import jaminv.advancedmachines.Reference;
import jaminv.advancedmachines.lib.parser.DataParserException;
import jaminv.advancedmachines.lib.parser.FileHandlerRecipe;
import jaminv.advancedmachines.lib.recipe.RecipeInput;
import jaminv.advancedmachines.lib.recipe.RecipeOutput;
import jaminv.advancedmachines.lib.util.logger.Logger;
import jaminv.advancedmachines.util.conditions.ConfigConditionFactory;
import jaminv.advancedmachines.util.conditions.OreDictionaryConditionFactory;
import jaminv.advancedmachines.util.recipe.press.PressManager.PressRecipe;
import net.minecraft.util.JsonUtils;

public class FileHandlerPressRecipe extends FileHandlerRecipe {

	public FileHandlerPressRecipe() {
		super();
		addConditionFactory(Reference.MODID + ":config", new ConfigConditionFactory());
		addConditionFactory(Reference.MODID + ":oredictionary", new OreDictionaryConditionFactory());
	}	
	
	@Override
	protected boolean parseRecipe(Logger logger, String filename, String path, JsonObject json) throws DataParserException {
		logger = logger.getLogger("press");		
		logger.info("Parsing recipe '" + path + "'.");
		
		int energy = getEnergy(json, ModConfig.general.defaultGrinderEnergyCost);
		int time = getTime(json, ModConfig.general.processTimeBasic);
		PressRecipe recipe = new PressRecipe(filename + "." + path, energy, time);
		
		JsonArray input = JsonUtils.getJsonArray(json, "input");
		int i = 0;
		for(JsonElement element : input) {
			String name = "input[" + i + "]"; 
			
			RecipeInput recipeInput = parseInput(element, name);
			if (recipeInput == null || recipeInput.hasError()) { return false; }
			recipe.addInput(i, recipeInput);
			i++;
		}		
		
		RecipeOutput output = parseOutput(json.get("output"), "output");
		if (output == null || output.isEmpty()) { return false; }
		recipe.setOutput(output);

		if (!checkConditions(json, "conditions", logger)) { return false; }
		
		PressManager.manager.addRecipe(recipe);		
		
		return true; 
	}

}
