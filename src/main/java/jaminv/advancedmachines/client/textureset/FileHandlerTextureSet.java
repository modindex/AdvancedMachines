package jaminv.advancedmachines.client.textureset;

import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import jaminv.advancedmachines.util.logger.Logger;
import jaminv.advancedmachines.util.parser.DataParserException;
import jaminv.advancedmachines.util.parser.FileHandler;
import jaminv.advancedmachines.util.parser.ParseUtils;
import net.minecraft.util.JsonUtils;

public class FileHandlerTextureSet implements FileHandler {

	@Override
	public boolean parseData(Logger logger, String filename, JsonObject json) {
		logger = logger.getLogger("texturesets");
		
		int i = 0, c = 0;
		for (Map.Entry<String,JsonElement> entry : json.entrySet()) {
			String name = entry.getKey();
			try {
				String path = filename + "." + entry.getKey();
				if (getTextureSet(path, ParseUtils.getJsonObject(entry.getValue(), entry.getKey()), logger)) { c++; }
			} catch(DataParserException | JsonSyntaxException e) {
				logger.error(e.getMessage());
			}
			i++;
		}
		
		ParseUtils.logComplete(logger, c, i, "info.parser.constant.complete", "info.parser.constant.incomplete");
		return true;
	}
	
	protected boolean getTextureSet(String path, JsonObject json, Logger logger) {
		for(Map.Entry<String,JsonElement> entry : json.entrySet()) {
			String newpath = path + "." + entry.getKey();
			
			try {
				if (JsonUtils.isString(json, entry.getKey())) {
					TextureSets.put(newpath, JsonUtils.getString(json, entry.getKey()));
				} else {
					getTextureSet(newpath, ParseUtils.getJsonObject(entry.getValue(), entry.getKey()), logger);
				}
			} catch(DataParserException | JsonSyntaxException e) {
				logger.error(e.getMessage());
			}
		}	
		
		return true;
	}
}
