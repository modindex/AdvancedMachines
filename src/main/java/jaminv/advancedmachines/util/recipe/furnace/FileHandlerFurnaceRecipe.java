package jaminv.advancedmachines.util.recipe.furnace;

import com.google.gson.JsonObject;

import jaminv.advancedmachines.ModConfig;
import jaminv.advancedmachines.lib.parser.DataParserException;
import jaminv.advancedmachines.lib.parser.FileHandlerRecipe;
import jaminv.advancedmachines.lib.recipe.RecipeInput;
import jaminv.advancedmachines.lib.recipe.RecipeOutput;
import jaminv.advancedmachines.lib.util.logger.Logger;
import jaminv.advancedmachines.util.recipe.furnace.FurnaceManager.FurnaceRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class FileHandlerFurnaceRecipe extends FileHandlerRecipe {

	@Override
	protected boolean parseRecipe(Logger logger, String filename, String path, JsonObject json) throws DataParserException {
		logger = logger.getLogger("furnace");
		RecipeInput input = parseInput(json.get("input"), "input");
		RecipeOutput output = parseOutput(json.get("output"), "output");
		int energy = getEnergy(json, ModConfig.general.defaultGrinderEnergyCost);
		int time = getTime(json, ModConfig.general.processTimeBasic);
		float xp = JsonUtils.getFloat(json, "xp", 0.5f);
		
		if (input == null || input.isEmpty() || output == null || output.isEmpty()) { return false; }
		if (!checkConditions(json, "conditions", logger)) { return false; }
		
		FurnaceRecipe recipe = new FurnaceRecipe(filename + "." + path, energy, time);
		recipe.addInput(input);
		recipe.addOutput(output);
				
		FurnaceManager.manager.addRecipe(recipe);
		
		for (ItemStack stack : input.getItems()) {
			GameRegistry.addSmelting(stack, output.toItemStack(), xp);
		}
		
		return true;
	}

}
