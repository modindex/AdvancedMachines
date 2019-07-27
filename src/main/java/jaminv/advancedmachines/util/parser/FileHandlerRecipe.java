package jaminv.advancedmachines.util.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import jaminv.advancedmachines.util.Reference;
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
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public abstract class FileHandlerRecipe extends FileHandlerBase {
	
	protected HashMap<String, IConditionFactory>condition_factories = new HashMap<String, IConditionFactory>();
	protected void addConditionFactory(String name, IConditionFactory factory) { condition_factories.put(name, factory); }

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
	
	protected RecipeInput parseInput(Logger logger, String path, JsonElement input) throws DataParserException {
		if (input == null) { return null; }
		if (input.isJsonPrimitive()) {
			ItemStack item = parseItemStack(logger, path, input);
			if (item != null) { return new RecipeInput(item); } else { return null; }
		}
		JsonObject inputob = assertObject(path, input);
		
		int count = getInt(path, inputob, "count", 1);
		
		String ore = getString(path, inputob, "ore", false);
		if (ore != null) { return new RecipeInput(ore, count); }
		
		ItemStack item = parseItemStack(logger, path, input);
		if (item != null) { return new RecipeInput(item); }
		
		return null;
	}
	
	protected RecipeOutput parseOutput(Logger logger, String path, JsonElement output) throws DataParserException {
		if (output == null) { return null; }
		if (output.isJsonPrimitive()) {
			ItemStack item = parseItemStack(logger, path, output);
			if (item != null) { return new RecipeOutput(item); } else { return null; }
		}
		JsonObject outputob = assertObject(path, output);
		
		int count = getInt(path, outputob, "count", 1);
		
		String ore = getString(path, outputob, "ore", false);
		if (ore != null) { return new RecipeOutput(ore, count); }
		
		ItemStack item = parseItemStack(logger, path, outputob);
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
	
	protected boolean checkConditions(Logger logger, String path, JsonObject parent, String key) throws DataParserException {
		JsonArray list = getArray(path, parent, key, false);
		if (list == null) { return true; }
		
		for (JsonElement condition : list) {
			JsonObject ob = assertObject(path, condition);
			String value = getString(path, ob, "type", true);
			
			IConditionFactory factory = condition_factories.get(value);
			if (factory == null) {
				logger.error(I18n.format("error.parser.recipe.condition_not_found", path, value));
			}
			
			JsonContext context = new JsonContext(Reference.MODID);
			BooleanSupplier func = factory.parse(context, ob);  
			if (!func.getAsBoolean()) { logger.info(I18n.format("info.parser.recipe.condition_not_met", path, value)); return false; }
			logger.info(I18n.format("info.parser.recipe.condition_met", path, value));
		}
		
		return true;
	}
	
}
