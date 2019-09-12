package jaminv.advancedmachines.lib.parser;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import jaminv.advancedmachines.lib.util.logger.Logger;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;

public class FileHandlerFactories implements FileHandler {
	
	protected Map<String, IConditionFactory> conditions = new HashMap<>();
	public Map<String, IConditionFactory> getConditions() { return ImmutableMap.copyOf(conditions); }
	
	@Override
	public boolean parseData(Logger logger, String filename, JsonObject json) throws DataParserException {
		logger = logger.getLogger("factories");
		
		int i = 0, c = 0;
		if (json.has("conditions")) {
			for (Map.Entry<String, JsonElement> entry : JsonUtils.getJsonObject(json, "conditions").entrySet()) {
				c++;
				try {
					String clsName = JsonUtils.getString(entry.getValue(), "conditions[" + entry.getValue() + "]");
					conditions.put(entry.getKey(), loadFactory(entry.getKey(), clsName, IConditionFactory.class));
					i++;
				} catch(DataParserException e) {
					logger.error(e.getMessage());
				}
			}
		}
		
		ParseUtils.logComplete(logger, c, i, "%d added successfully.", "%d not added.");
		return true;
	}
	
	protected <T> T loadFactory(String key, String className, Class<? extends T> factoryClass) throws DataParserException {
	    try{
	        return factoryClass.cast(Class.forName(className, true, ClassLoader.getSystemClassLoader()).newInstance());
	    } catch(InstantiationException
	          | IllegalAccessException
	          | ClassNotFoundException e) {
	        throw new DataParserException("Class at " + key + " instantiation error: " + e.toString());
	    }
	}
}
