package jaminv.advancedmachines.util.parser;

import com.google.gson.JsonObject;

import jaminv.advancedmachines.util.logger.Logger;

public interface IFileHandler {
	public boolean parseData(Logger logger, String filename, JsonObject json) throws DataParserException;
}
