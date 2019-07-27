package jaminv.advancedmachines.util.recipe.machine.grinder;

import com.google.gson.JsonObject;

import jaminv.advancedmachines.util.ModConfig;
import jaminv.advancedmachines.util.Reference;
import jaminv.advancedmachines.util.conditions.ConfigConditionFactory;
import jaminv.advancedmachines.util.conditions.OreDictionaryConditionFactory;
import jaminv.advancedmachines.util.logger.Logger;
import jaminv.advancedmachines.util.parser.DataParserException;
import jaminv.advancedmachines.util.parser.FileHandlerRecipe;
import jaminv.advancedmachines.util.recipe.RecipeInput;
import jaminv.advancedmachines.util.recipe.RecipeOutput;
import jaminv.advancedmachines.util.recipe.machine.grinder.GrinderManager.GrinderRecipe;

public class FileHandlerGrinderRecipe extends FileHandlerRecipe {

	public FileHandlerGrinderRecipe() {
		super();
		addConditionFactory(Reference.MODID + ":config", new ConfigConditionFactory());
		addConditionFactory(Reference.MODID + ":oredictionary", new OreDictionaryConditionFactory());
	}

	@Override
	protected boolean parseRecipe(Logger logger, String filename, String path, JsonObject recipe) throws DataParserException {
		logger = logger.getLogger("grinder");
		RecipeInput input = parseInput(logger, path + ".input", getElement(path, recipe, "input", true));
		RecipeOutput output = parseOutput(logger, path + ".output", getElement(path, recipe, "output", true));
		RecipeOutput secondary = parseOutputWithChance(logger, path + ".secondary", getObject(path, recipe, "secondary", false));
		int energy = getEnergy(path, recipe, ModConfig.general.defaultGrinderEnergy);
		
		if (input == null || output == null) { return false; }
		if (!checkConditions(logger, path + ".conditions", recipe, "conditions")) { return false; }
		
		GrinderRecipe rec = new GrinderRecipe(filename + "." + path, energy);
		rec.setInput(input);
		rec.setOutput(output);
		if (secondary != null && !secondary.isEmpty()) { rec.addSecondary(secondary); }

		GrinderManager.getRecipeManager().addRecipe(rec);		
		
		return true; 
	}

}
