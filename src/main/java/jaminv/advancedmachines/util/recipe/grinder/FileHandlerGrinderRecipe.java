package jaminv.advancedmachines.util.recipe.grinder;

import com.google.gson.JsonObject;

import jaminv.advancedmachines.ModConfig;
import jaminv.advancedmachines.Reference;
import jaminv.advancedmachines.lib.parser.DataParserException;
import jaminv.advancedmachines.lib.parser.FileHandlerRecipe;
import jaminv.advancedmachines.lib.recipe.RecipeImpl;
import jaminv.advancedmachines.lib.recipe.RecipeInput;
import jaminv.advancedmachines.lib.recipe.RecipeOutput;
import jaminv.advancedmachines.lib.util.logger.Logger;
import jaminv.advancedmachines.util.conditions.ConfigConditionFactory;
import jaminv.advancedmachines.util.conditions.OreDictionaryConditionFactory;

public class FileHandlerGrinderRecipe extends FileHandlerRecipe {

	public FileHandlerGrinderRecipe() {
		super();
		addConditionFactory(Reference.MODID + ":config", new ConfigConditionFactory());
		addConditionFactory(Reference.MODID + ":oredictionary", new OreDictionaryConditionFactory());
	}

	@Override
	protected boolean parseRecipe(Logger logger, String filename, String path, JsonObject json) throws DataParserException {
		logger = logger.getLogger("grinder");		
		logger.info("Parsing recipe '" + path + "'.");
		
		RecipeInput input = parseInput(json.get("input"), "input");
		RecipeOutput output = parseOutput(json.get("output"), "output");
		RecipeOutput secondary = parseOutputWithChance(json.get("secondary"), "secondary");
		int energy = getEnergy(json, ModConfig.general.defaultGrinderEnergyCost);
		int time = getTime(json, ModConfig.general.processTimeBasic);
		
		if (input == null || input.isEmpty() || output == null || output.isEmpty()) { return false; }
		if (!checkConditions(json, "conditions", logger)) { return false; }
		
		RecipeImpl recipe = new RecipeImpl(filename + "." + path, energy, time);
		recipe.addInput(input);
		recipe.addOutput(output);
		if (secondary != null && !secondary.isEmpty()) { recipe.addSecondary(secondary); }

		GrinderManager.manager.addRecipe(recipe);		
		
		return true; 
	}

}
