package jaminv.advancedmachines.lib.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import jaminv.advancedmachines.Reference;
import jaminv.advancedmachines.lib.LibReference;
import jaminv.advancedmachines.lib.util.logger.Logger;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ModContainer;

// TODO: Move log creation out of this file - have callers pass a log object.
public class DataParser {
	
    private static Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();	
 	
	public static final String LOG_PATH = "logs/" + LibReference.FILENAME + ".parser.log";
	private static Map<String, JsonObject> constants = null;
	private static Map<String, JsonObject> functions = null;
	private static Map<String, Map<String, Class>> factories = null;
	
	public static JsonObject getConstant(String key) {
		if (constants == null) { return null; }
		return constants.get(key);
	}
	
	public static JsonObject getFunction(String key) {
		if (functions == null) { return null; }
		return functions.get(key);
	}
	
	public static void parseConstants() {
		constants = new HashMap<String, JsonObject>();
		factories = new HashMap<String, Map<String, Class>>();
		
		(new Logger(LOG_PATH, "parser", false)).close();
		
		FileHandlerJsonMap chandler = new FileHandlerJsonMap("constants");
		parseFolder("data", chandler, "_constants.json");
		constants = chandler.getMap();
	}
	
	public static void parseFolder(String path, FileHandler handler) {
		parseFolder(path, handler, null);
	}
	
	public static void parseFolder(String path, FileHandler handler, String findFilename) {
		ModContainer mod = FMLCommonHandler.instance().findContainerFor(Reference.MODID);

		Logger logger = new Logger(LOG_PATH, "parser");
		
		CraftingHelper.findFiles(mod, "assets/" + mod.getModId() + "/" + path, null, (root, file) -> {
			String filename = file.getFileName().toString();
            if ((findFilename != null && !filename.equals(findFilename)) ||
            	!"json".equals(FilenameUtils.getExtension(filename)) || 
            	(findFilename == null && filename.startsWith("_"))) {
            	
                return true;
            }
            
            BufferedReader reader = null;
            try {
                reader = Files.newBufferedReader(file);
                JsonObject json = JsonUtils.fromJson(GSON, reader, JsonObject.class);
                logger.info("Parsing file: " + root.toString() + "\\" + filename);
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
            
            return true;
		}, false, false);
		
		logger.close();
	}
}
