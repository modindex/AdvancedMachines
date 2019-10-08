package jaminv.advancedmachines.lib.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import jaminv.advancedmachines.lib.util.logger.Logger;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ModContainer;

public class DataParser {
	
    private static Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();	
 	
	public static String getLogPath(String modId) { return "logs/" + modId + ".parser.log"; }
	public static String getConfigPath(String modId, String path) { return "config/" + modId + "/" + path; }
	private static Map<String, JsonObject> constants = null;
	private static Map<String, IConditionFactory> conditions = null;
	
	public static JsonObject getConstant(String key) {
		if (constants == null) { return null; }
		return constants.get(key);
	}
	
	public static IConditionFactory getCondition(String key) {
		if (conditions == null) { return null; }
		return conditions.get(key);
	}
	
	public static void parseConstants(String modId, String path) {
		constants = new HashMap<String, JsonObject>();
		conditions = new HashMap<String, IConditionFactory>();
		
		(new Logger(getLogPath(modId), "parser", false)).close();
		
		FileHandlerJsonMap chandler = new FileHandlerJsonMap("constants");
		parseJarFolder(modId, path, chandler, "_constants.json");
		constants = chandler.getMap();
		
		FileHandlerFactories fhandler = new FileHandlerFactories(modId);
		parseJarFolder(modId, path, fhandler, "_factories.json");
		conditions = fhandler.getConditions();
	}
	
	public static void parseJarFolder(String modId, String path, FileHandler handler) {
		parseJarFolder(modId, path, handler, null);
	}
	
	public static void parseJarFolder(String modId, String path, FileHandler handler, String findFilename) {
		ModContainer mod = FMLCommonHandler.instance().findContainerFor(modId);

		Logger logger = new Logger(getLogPath(modId), "parser");
		
		CraftingHelper.findFiles(mod, "assets/" + mod.getModId() + "/" + path, null, (root, file) -> {
			String filename = file.getFileName().toString();
            if ((findFilename != null && !filename.equals(findFilename)) ||
            	!"json".equals(FilenameUtils.getExtension(filename)) || 
            	(findFilename == null && filename.startsWith("_"))) {
            	
                return true;
            }
            
            parseFile(file, logger, handler);
            
            return true;
		}, false, false);
		
		logger.close();
	}
	
	public static void parseConfigFolder(String modId, String path, FileHandler handler) {
		parseConfigFolder(modId, path, handler, null);
	}
	
	public static void parseConfigFolder(String modId, String path, FileHandler handler, String findFilename) {
		Logger logger = new Logger(getLogPath(modId), "config");
		
		File folder = new File(getConfigPath(modId, path));
		parseConfigFolder(folder, logger, handler, findFilename);
		
		logger.close();
	}
	
	protected static void parseConfigFolder(File folder, Logger logger, FileHandler handler, String findFilename) {
		File[] filelist = folder.listFiles((file, name) -> 
			name != null && (
    			(findFilename != null && name.equals(findFilename))
    			|| (findFilename != null && name.endsWith(".json") || !name.startsWith("_"))
    			|| file.isDirectory()
    		)
		);
		if (filelist == null || filelist.length == 0) { return; }
		
		for (File file : filelist) {
			if (file.isDirectory()) { 
				parseConfigFolder(file, logger, handler, findFilename);
				continue;
			}			
			parseFile(file.toPath(), logger, handler);		
		}
	}
	
	protected static void parseFile(Path file, Logger logger, FileHandler handler) {
		String filename = file.getFileName().toString();

		BufferedReader reader = null;
	    try {
	        reader = Files.newBufferedReader(file);
	        JsonObject json = JsonUtils.fromJson(GSON, reader, JsonObject.class);
	        logger.info("Parsing file: " + file.toString());
	        if (handler.parseData(logger, FilenameUtils.getBaseName(filename), json)) {
	        	logger.info("File '" + filename + "' parsed successfully."); 
	        } else {
	        	logger.error("Unhandled error parsing file: " + filename);
	        }
	    }
	    catch (DataParserException | JsonParseException | IOException e) {
	    	logger.error("Error parsing file '" + filename + "': " + e.toString());
	    } finally {
	    	logger.logBlank();
	        IOUtils.closeQuietly(reader); 
	    }
	}
}
