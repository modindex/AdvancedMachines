package jaminv.advancedmachines.util.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

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
import net.minecraft.util.JsonUtils;
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
			JsonObject recipe = getJsonObject(json, entry.getKey());
			
			try {
				if (parseRecipe(logger, filename, name, recipe)) { c++; }
			} catch(DataParserException | JsonSyntaxException e) {
				logger.error(e.getMessage());
			}
			i++;
		}

		logComplete(logger, c, i, "%d recipes added successfully.", "%d recipes not added.");
		
		return true;
	}
	
	protected abstract boolean parseRecipe(Logger logger, String filename, String path, JsonObject recipe) throws DataParserException;
	
	protected RecipeInput parseInput(JsonElement input, String memberName) throws DataParserException {
		if (input == null) { throw new DataParserException("Missing recipe element: '" + memberName + "'"); }
		
		if (JsonUtils.isString(input)) {
			return new RecipeInput(parseItemStack(input, memberName));
		}
		JsonObject inputob = getJsonObject(input, memberName);
		if (!inputob.has("ore")) {
			return new RecipeInput(parseItemStack(input, memberName));
		}
		
		int count = JsonUtils.getInt(inputob, "count", 1);
		String ore = JsonUtils.getString(inputob, "ore");
		return new RecipeInput(ore, count);
	}
	
	protected RecipeOutput parseOutput(JsonElement output, String memberName) throws DataParserException {
		if (output == null) { throw new DataParserException("Missing recipe element: '" + memberName + "'"); }
		
		if (JsonUtils.isString(output)) {
			return new RecipeOutput(parseItemStack(output, memberName));
		}
		JsonObject outputob = getJsonObject(output, memberName);
		if (!outputob.has("ore")) {
			return new RecipeOutput(parseItemStack(output, memberName));
		}
		
		int count = JsonUtils.getInt(outputob, "count", 1);
		String ore = JsonUtils.getString(outputob, "ore");
		return new RecipeOutput(ore, count);
	}
	
	protected RecipeOutput parseOutputWithChance(JsonElement output, String memberName) throws DataParserException {
		if (output == null) { return null; }
		if (JsonUtils.isString(output)) { return parseOutput(output, memberName); }
		
		RecipeOutput ret = parseOutput(output, memberName);
		JsonObject outputob = getJsonObject(output, memberName);
		int chance = JsonUtils.getInt(outputob, "chance", 100);
		return ret.withChance(chance);
	}

	protected int getEnergy(JsonObject obj, int def) throws DataParserException {
		int energy = JsonUtils.getInt(obj, "energy", def);
		int energy_percent = JsonUtils.getInt(obj, "energy_percent", 100);
		
		return (int)Math.floor(energy * energy_percent / 100.0f);
	}
	
	protected boolean checkConditions(JsonObject parent, String key, Logger logger) throws DataParserException {
		JsonArray list = JsonUtils.getJsonArray(parent, key, null);
		if (list == null) { return true; }
		
		int i = 0;
		for (JsonElement condition : list) {
			JsonObject ob = getJsonObject(condition, key + "[" + i + "]");
			String value = JsonUtils.getString(ob, "type");
			
			// TODO: Add Factories
			IConditionFactory factory = condition_factories.get(value);
			if (factory == null) {
				throw new DataParserException("Condition '" + value + "' not found.");
			}
			
			JsonContext context = new JsonContext(Reference.MODID);
			BooleanSupplier func = factory.parse(context, ob);  
			if (!func.getAsBoolean()) { logger.info("Condition '" + value + "' not met."); return false; }
			logger.info("Condition '" + value + "' met.");
			
			i++;
		}
		
		return true;
	}
	
}
