package jaminv.advancedmachines.lib.util.parser;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import jaminv.advancedmachines.lib.util.logger.Logger;
import net.minecraft.util.JsonUtils;

public class FileHandlerJsonMap implements FileHandler {
	
	protected Map<String, JsonObject> map = new HashMap<String, JsonObject>();
	public Map<String, JsonObject> getMap() { return ImmutableMap.copyOf(map); }
	
	String logName;
	
	public FileHandlerJsonMap(String logName) {
		this.logName = logName;		
	}
	
	@Override
	public boolean parseData(Logger logger, String filename, JsonObject json) throws DataParserException {
		logger = logger.getLogger(logName);
		
		int i = 0, c = 0;
		for (Map.Entry<String,JsonElement> entry : json.entrySet()) {
			String name = entry.getKey();
			try {
				JsonObject constant = JsonUtils.getJsonObject(entry.getValue(), name);
				map.put(name, constant);
				c++;
			} catch(JsonSyntaxException e) {
				logger.error(e.getMessage());
			}
			i++;
		}
		
		ParseUtils.logComplete(logger, c, i, "%d " + logName + " added successfully.", "%d " + logName + " not added.");
		return true;
	}
}
