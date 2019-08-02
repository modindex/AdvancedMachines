package jaminv.advancedmachines.util.parser;

import java.util.Map;

import com.google.gson.JsonObject;

import jaminv.advancedmachines.util.logger.Logger;

public interface IFileHandler {
	public boolean parseData(Logger logger, String filename, JsonObject json) throws DataParserException;
	
	public void setConstants(Map<String, JsonObject> constants);
	public void setFactories(Map<String, Map<String, Class>> factories);
}
