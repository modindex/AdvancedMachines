package jaminv.advancedmachines.util.parser;

import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import jaminv.advancedmachines.util.logger.Logger;
import jaminv.advancedmachines.util.recipe.RecipeInput;
import jaminv.advancedmachines.util.recipe.RecipeOutput;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public abstract class FileHandlerRecipe extends FileHandlerBase {

	@Override
	public boolean parseData(Logger logger, String filename, JsonObject json) throws DataParserException {
		logger = logger.getLogger("recipe");
		
		int i = 0, c = 0;
		for (Map.Entry<String,JsonElement> entry : json.entrySet()) {
			String name = entry.getKey();
			if (!entry.getValue().isJsonObject()) { throw new DataParserException(I18n.format("error.parser.recipe.recipe_not_object", name)); }
			JsonObject recipe = (JsonObject)entry.getValue();
			
			try {
				if (parseRecipe(logger, filename, name, recipe)) { c++; }
			} catch(DataParserException e) {
				logger.error(e.getMessage());
			}
			i++;
		}
		
		logComplete(logger, c, i, "info.parser.recipe.complete", "info.parser.recipe.incomplete");
		
		return true;
	}
	
	protected abstract boolean parseRecipe(Logger logger, String filename, String path, JsonObject recipe) throws DataParserException;
	
	protected RecipeInput parseInput(Logger logger, String path, JsonObject input) throws DataParserException {
		if (input == null) { return null; }
		
		int count = getInt(path, input, "count", 1);
		
		String ore = getString(path, input, "ore", false);
		if (ore != null) { return new RecipeInput(ore, count); }
		
		ItemStack item = parseItemStack(logger, path, input);
		if (item != null) { return new RecipeInput(item); }
		
		return null;
	}
	
	protected RecipeOutput parseOutput(Logger logger, String path, JsonObject output) throws DataParserException {
		if (output == null) { return null; }
		
		int count = getInt(path, output, "count", 1);
		
		String ore = getString(path, output, "ore", false);
		if (ore != null) { return new RecipeOutput(ore, count); }
		
		ItemStack item = parseItemStack(logger, path, output);
		if (item != null) { return new RecipeOutput(item); }
		
		return null;
	}
	
	protected RecipeOutput parseOutputWithChance(Logger logger, String path, JsonObject output) throws DataParserException {
		if (output == null) { return null; }
		
		RecipeOutput ret = parseOutput(logger, path, output);
		if (ret == null) { return null; }
		
		int chance = getInt(path, output, "chance", 100);
		return ret.withChance(chance);
	}

	protected int getEnergy(String name, JsonObject input, int def) throws DataParserException {
		int energy = getInt(name, input, "energy", def);
		int energy_percent = getInt(name, input, "energy_percent", 100);
		
		return (int)Math.floor(energy * energy_percent / 100.0f);
	}
	
}
