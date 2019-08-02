package jaminv.advancedmachines.util.parser;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import jaminv.advancedmachines.util.logger.Logger;

public class FileHandlerFactories extends FileHandlerBase {
	
	@Override
	public boolean parseData(Logger logger, String filename, JsonObject json) throws DataParserException {
		logger = logger.getLogger("factories");
		
		int i = 0, c = 0;
		for (Map.Entry<String,JsonElement> entry : json.entrySet()) {
			String name = entry.getKey();
			try {
				if (loadFactories(logger, entry.getKey(), entry.getValue())) { c++; }
			} catch(DataParserException e) {
				logger.error(e.getMessage());
			}
			i++;
		}
		
		logComplete(logger, c, i, "info.parser.constant.complete", "info.parser.constant.incomplete");
		return true;
	}
	
	protected boolean loadFactories(Logger logger, String type, JsonElement element) throws DataParserException {
/*		JsonObject obj = assertObject(type, element);

		Map<String, Map<String, Class>> factories = getFactories();
		Map<String, Class> map = new HashMap<String, Class>();
		
		for (Map.Entry<String,JsonElement> entry : obj.entrySet()) {
			try {
				String className = assertString(type + "." + entry.getKey(), entry.getValue());
				Class factory = Class.forName(className);
				
				map.put(entry.getKey(), factory);
			} catch(DataParserException | ClassNotFoundException e) {
				logger.error(e.getMessage());
			}
		}
		
		factories.put(type, map); */
		return true;
	}
}
