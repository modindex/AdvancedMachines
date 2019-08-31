package jaminv.advancedmachines.lib.util.parser;

import java.util.Map;

import com.google.gson.JsonObject;

import jaminv.advancedmachines.lib.util.logger.Logger;

public interface FileHandler {
	public boolean parseData(Logger logger, String filename, JsonObject json) throws DataParserException;
}
