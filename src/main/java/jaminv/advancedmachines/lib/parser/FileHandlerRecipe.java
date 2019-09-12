package jaminv.advancedmachines.lib.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import jaminv.advancedmachines.ModConfig;
import jaminv.advancedmachines.Reference;
import jaminv.advancedmachines.lib.recipe.RecipeImpl;
import jaminv.advancedmachines.lib.recipe.RecipeInput;
import jaminv.advancedmachines.lib.recipe.RecipeOutput;
import jaminv.advancedmachines.lib.util.logger.Logger;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import scala.actors.threadpool.Arrays;

public class FileHandlerRecipe implements FileHandler {
	
	public static enum IngredientType {
		ITEM("item"),
		FLUID("fluid");

		protected final String name;
		private IngredientType(String name) {
			this.name = name;
		}
		
		public String getName() { return name; }
	}
	
	public static enum RecipeSection {
		INPUT("input"),
		OUTPUT("output"),
		SECONDARY("secondary"),
		CATALYST("catalyst");
		
		protected final String name;
		private RecipeSection(String name) {
			this.name = name;
		}
		
		public String getName() { return name; }
	}
	
	/** Functional Interface */
	public static interface RecipeHandler {
		public void addRecipe(RecipeImpl recipe);
	}
	
	protected final String logname;
	protected int limit[][] = new int[RecipeSection.values().length][IngredientType.values().length];
	protected RecipeHandler handler;
	protected int count[][] = new int[RecipeSection.values().length][IngredientType.values().length];
	
	public FileHandlerRecipe(String logname, RecipeHandler handler) {
		//addConditionFactory(Reference.MODID + ":config", new ConfigConditionFactory());
		//addConditionFactory(Reference.MODID + ":oredictionary", new OreDictionaryConditionFactory());
		
		this.logname = logname;
		this.handler = handler;
		for (RecipeSection section : RecipeSection.values()) {
			Arrays.fill(limit[section.ordinal()], 0);
		}
	}
	
	public FileHandlerRecipe setLimit(RecipeSection section, IngredientType type, int limit) {
		this.limit[section.ordinal()][type.ordinal()] = limit;
		return this;
	}
	
	protected void addCount(RecipeSection section, IngredientType type) {
		this.count[section.ordinal()][type.ordinal()]++;
	}

	@Override
	public boolean parseData(Logger logger, String filename, JsonObject json) throws DataParserException {
		logger = logger.getLogger("recipe");
		
		int i = 0, c = 0;
		for (Map.Entry<String,JsonElement> entry : json.entrySet()) {
			String name = entry.getKey();
			JsonObject recipe = ParseUtils.getJsonObject(json, entry.getKey());
			
			try {
				if (parseRecipe(logger, filename, name, recipe)) { c++; }
			} catch(DataParserException | JsonSyntaxException e) {
				logger.error(e.getMessage());
			}
			i++;
		}

		ParseUtils.logComplete(logger, c, i, "%d recipes added successfully.", "%d recipes not added.");
		
		return true;
	}
	
	protected boolean parseRecipe(Logger logger, String filename, String path, JsonObject json) throws DataParserException {
		logger = logger.getLogger(logname);		
		logger.info("Parsing recipe '" + path + "'.");
		for (RecipeSection section : RecipeSection.values()) {
			Arrays.fill(count[section.ordinal()], 0);
		}

		int energy = getEnergy(json, ModConfig.general.defaultGrinderEnergyCost);
		int time = getTime(json, ModConfig.general.processTimeBasic);
		RecipeImpl recipe = new RecipeImpl(filename + "." + path, energy, time);
		recipe.setXp(JsonUtils.getFloat(json, "xp", 0.0f));
		
		for (RecipeInput input : parseInputs(json, RecipeSection.INPUT)) {
			recipe.addInput(input);
		}
		for (RecipeOutput output : parseOutputs(json, RecipeSection.OUTPUT, false)) {
			recipe.addOutput(output);
		}
		
		if (json.has(RecipeSection.SECONDARY.getName())) {
			for (RecipeOutput secondary : parseOutputs(json, RecipeSection.SECONDARY, true)) {
				recipe.addSecondary(secondary);
			}
		}
		
		if (json.has(RecipeSection.CATALYST.getName())) {
			for (RecipeInput catalyst : parseInputs(json, RecipeSection.CATALYST)) {
				recipe.addCatalyst(catalyst);
			}
		}
		
		for (RecipeSection section : RecipeSection.values()) {
			for (IngredientType type : IngredientType.values()) {
				if (count[section.ordinal()][type.ordinal()] > limit[section.ordinal()][type.ordinal()]) {
					throw new DataParserException("Recipe exceeds " + type.getName() + " " + section.getName() + " limit of " +
							limit[section.ordinal()][type.ordinal()] + ".");
				}
			}
		}
		if (count[RecipeSection.INPUT.ordinal()][IngredientType.ITEM.ordinal()] + count[RecipeSection.CATALYST.ordinal()][IngredientType.ITEM.ordinal()] >
				limit[RecipeSection.INPUT.ordinal()][IngredientType.ITEM.ordinal()]) {
			throw new DataParserException("Recipe has combined item input and catalyst count that exceeds input limit.");
		}
		if (count[RecipeSection.INPUT.ordinal()][IngredientType.FLUID.ordinal()] + count[RecipeSection.CATALYST.ordinal()][IngredientType.FLUID.ordinal()] >
		limit[RecipeSection.INPUT.ordinal()][IngredientType.FLUID.ordinal()]) {
			throw new DataParserException("Recipe has combined fluid input and catalyst count that exceeds input limit.");
		}

		if (!checkConditions(json, "conditions", logger)) { return false; }
		handler.addRecipe(recipe);		
		
		return true; 
	}
	
	protected List<RecipeInput> parseInputs(JsonObject json, RecipeSection section) throws DataParserException {
		List<RecipeInput> ret = new ArrayList<>();
		
		if (JsonUtils.isJsonArray(json, section.getName())) {
			JsonArray input = JsonUtils.getJsonArray(json, section.getName());
			int i = 0;
			for(JsonElement element : input) {
				RecipeInput recipeInput = parseInput((JsonObject)element, section.getName() + "["+i+"]");
				addCount(section, recipeInput.isFluid() ? IngredientType.FLUID : IngredientType.ITEM);
				i++;
				ret.add(recipeInput);
			}
		} else {
			RecipeInput recipeInput = parseInput(json.get(section.getName()), section.getName());
			addCount(section, recipeInput.isFluid() ? IngredientType.FLUID : IngredientType.ITEM);
			ret.add(recipeInput);
		}
		return ret;
	}
	
	protected List<RecipeOutput> parseOutputs(JsonObject json, RecipeSection section, boolean withChance) throws DataParserException {
		List<RecipeOutput> ret = new ArrayList<>();
		
		if (JsonUtils.isJsonArray(json, section.getName())) {
			JsonArray output = JsonUtils.getJsonArray(json, section.getName());
			int i = 0;
			for(JsonElement element : output) {
				RecipeOutput recipeOutput = parseOutput((JsonObject)element, section.getName() + "["+i+"]", withChance);
				addCount(section, recipeOutput.isFluid() ? IngredientType.FLUID : IngredientType.ITEM);
				i++;
				ret.add(recipeOutput);
			}
		} else {
			RecipeOutput recipeOutput = parseOutput(json.get(section.getName()), section.getName(), withChance);
			addCount(section, recipeOutput.isFluid() ? IngredientType.FLUID : IngredientType.ITEM);
			ret.add(recipeOutput);
		}
		return ret;
	}
	
	/**
	 * Parse a RecipeInput
	 * @param input JsonElement, can be a string (item name), or an object with multiple elements that describe an item or an ore dictionary entry.
	 * @param memberName Used for error logging
	 * @return RecipeInput
	 * @throws DataParserException
	 */
	protected RecipeInput parseInput(JsonElement input, String memberName) throws DataParserException {
		if (input == null) { throw new DataParserException("Missing recipe element: '" + memberName + "'"); }
		
		RecipeInput ret;
		if (input.isJsonPrimitive()) {
			ret = new RecipeInput(ParseUtils.parseItemStack(input, memberName));
		} else {
			JsonObject inputob = ParseUtils.getJsonObject(input, memberName);
			
			if (inputob.has("fluid")) {
				ret = new RecipeInput(ParseUtils.parseFluidStack(inputob, memberName));
			} else if (!inputob.has("ore")) {
				ret = new RecipeInput(ParseUtils.parseItemStack(input, memberName));
			} else {
				int count = JsonUtils.getInt(inputob, "count", 1);
				String ore = JsonUtils.getString(inputob, "ore");
				ret = new RecipeInput(ore, count);
			}

			ret.setExtract(JsonUtils.getBoolean(inputob, "extract", true));
		}
		
		if (ret == null || ret.hasError()) { throw new DataParserException("Invalid input at " + memberName); }
		return ret;
	}
	
	/**
	 * Parse a RecipeOutput
	 * @param output JsonElement, can be a string (item name), or an object with multiple elements that describe an item or an ore dictionary entry.
	 * @param memberName Used for error logging
	 * @return RecipeOutput
	 * @throws DataParserException
	 */
	protected RecipeOutput parseOutput(JsonElement output, String memberName, boolean withChance) throws DataParserException {
		if (output == null) { throw new DataParserException("Missing recipe element: '" + memberName + "'"); }
		
		RecipeOutput ret;
		if (output.isJsonPrimitive()) {
			ret = new RecipeOutput(ParseUtils.parseItemStack(output, memberName));
		} else {
			JsonObject outputob = ParseUtils.getJsonObject(output, memberName);
			if (outputob.has("fluid")) {
				ret = new RecipeOutput(ParseUtils.parseFluidStack(outputob, memberName));
			} else if (!outputob.has("ore")) {
				ret = new RecipeOutput(ParseUtils.parseItemStack(output, memberName));
			} else {
				int count = JsonUtils.getInt(outputob, "count", 1);
				String ore = JsonUtils.getString(outputob, "ore");
				ret =  new RecipeOutput(ore, count);
			}
			
			if (withChance) {
				ret.withChance(JsonUtils.getInt(outputob, "chance", 100));
			}
		}
		
		if (ret == null || ret.hasError()) { throw new DataParserException("Invalid output at " + memberName); }
		return ret;
	}

	protected int getEnergy(JsonObject obj, int def) {
		int energy = JsonUtils.getInt(obj, "energy", def);
		int energy_percent = JsonUtils.getInt(obj, "energy_percent", 100);
		
		return (int)Math.floor(energy * energy_percent / 100.0f);
	}
	
	protected int getTime(JsonObject obj, int def) {
		int energy = JsonUtils.getInt(obj, "time", def);
		int energy_percent = JsonUtils.getInt(obj, "time_percent", 100);
		
		return (int)Math.floor(energy * energy_percent / 100.0f);
	}
	
	protected boolean checkConditions(JsonObject parent, String key, Logger logger) throws DataParserException {
		JsonArray list = JsonUtils.getJsonArray(parent, key, null);
		if (list == null) { return true; }
		
		int i = 0;
		for (JsonElement condition : list) {
			JsonObject ob = ParseUtils.getJsonObject(condition, key + "[" + i + "]");
			String value = JsonUtils.getString(ob, "type");
			
			IConditionFactory factory = DataParser.getCondition(value);
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
