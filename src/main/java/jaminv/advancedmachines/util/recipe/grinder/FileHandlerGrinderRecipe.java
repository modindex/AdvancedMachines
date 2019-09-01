package jaminv.advancedmachines.util.recipe.grinder;

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
import jaminv.advancedmachines.util.recipe.grinder.GrinderManager.GrinderRecipe;

public class FileHandlerGrinderRecipe extends FileHandlerRecipe {

	public FileHandlerGrinderRecipe() {
		super();
		addConditionFactory(Reference.MODID + ":config", new ConfigConditionFactory());
		addConditionFactory(Reference.MODID + ":oredictionary", new OreDictionaryConditionFactory());
	}

	@Override
	protected boolean parseRecipe(Logger logger, String filename, String path, JsonObject recipe) throws DataParserException {
		logger = logger.getLogger("grinder");		
		logger.info("Parsing recipe '" + path + "'.");
		
		RecipeInput input = parseInput(recipe.get("input"), "input");
		RecipeOutput output = parseOutput(recipe.get("output"), "output");
		RecipeOutput secondary = parseOutputWithChance(recipe.get("secondary"), "secondary");
		int energy = getEnergy(recipe, ModConfig.general.defaultGrinderEnergyCost);
		
		if (input == null || input.isEmpty() || output == null || output.isEmpty()) { return false; }
		if (!checkConditions(recipe, "conditions", logger)) { return false; }
		
		GrinderRecipe rec = new GrinderRecipe(filename + "." + path, energy);
		rec.setInput(input);
		rec.setOutput(output);
		if (secondary != null && !secondary.isEmpty()) { rec.addSecondary(secondary); }

		GrinderManager.manager.addRecipe(rec);		
		
		return true; 
	}

}
