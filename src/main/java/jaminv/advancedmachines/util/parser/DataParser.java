package jaminv.advancedmachines.util.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.util.Reference;
import jaminv.advancedmachines.util.logger.Logger;
import jaminv.advancedmachines.util.recipe.parser.RecipeFile;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class DataParser {
	
    private static Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();	
	private static Map<String, IFileHandler> datafolder = new HashMap<String, IFileHandler>();
	
	public static void addFolder(String path, IFileHandler handler) {
		datafolder.put(path, handler);
	}
	
	public static void parse() {
		ModContainer mod = FMLCommonHandler.instance().findContainerFor(Reference.MODID);
		Logger logger = new Logger("logs/" + Reference.FILENAME + ".parser.log", "parser");
		
		for (Map.Entry<String, IFileHandler> entry : datafolder.entrySet()) {
		
			CraftingHelper.findFiles(mod, "assets/" + mod.getModId() + "/" + entry.getKey(), null, (root, file) -> {
				String filename = file.getFileName().toString();
	            if (!"json".equals(FilenameUtils.getExtension(filename)) || filename.startsWith("_")) {
	                return true;
	            }
	            
	            BufferedReader reader = null;
	            try {
	                reader = Files.newBufferedReader(file);
	                JsonObject json = JsonUtils.fromJson(GSON, reader, JsonObject.class);
	                logger.log(Level.INFO, I18n.format("info.parser.parsing_file", root.toString() + "/" + filename));
	                if (entry.getValue().parseData(logger, FilenameUtils.getBaseName(filename), json)) {
	                	logger.log(Level.INFO, I18n.format("info.parser.file_parsed", filename)); 
	                } else {
	                	logger.log(Level.ERROR, I18n.format("error.parser.cannot_parse_file", filename, "error_unhandled"));
	                }
	            }
	            catch (DataParserException e) {
	            	logger.log(Level.ERROR, I18n.format("error.parser.cannot_parse_file", filename, e.toString()));
	            }
	            catch (JsonParseException e) {
	            	logger.log(Level.FATAL, I18n.format("error.parser.error_loading_file", filename, e.toString()));
	            }
	            catch (IOException e) {
	            	logger.log(Level.FATAL, I18n.format("error.parser.error_reading_file", filename, e.toString()));
	            } finally {
	            	logger.logBlank();
	                IOUtils.closeQuietly(reader); 
	            }
	            
	            return true;
			}, false, false);
		}
		
		logger.close();
	}
}
