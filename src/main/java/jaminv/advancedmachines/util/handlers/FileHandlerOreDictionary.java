package jaminv.advancedmachines.util.handlers;

import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import jaminv.advancedmachines.util.logger.Logger;
import jaminv.advancedmachines.util.parser.DataParserException;
import jaminv.advancedmachines.util.parser.FileHandlerBase;
import jaminv.advancedmachines.util.recipe.RecipeInput;
import jaminv.advancedmachines.util.recipe.RecipeOutput;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import scala.Int;

public class FileHandlerOreDictionary extends FileHandlerBase {

	@Override
	public boolean parseData(Logger logger, String filename, JsonObject json) throws DataParserException {
		logger = logger.getLogger("ore_dictionary");
		
		int i = 0, c = 0;
		for (Map.Entry<String,JsonElement> entry : json.entrySet()) {
			String name = entry.getKey();
			JsonArray list = assertArray(name, entry.getValue());
			
			for (JsonElement ore : list) {
				String path = name + "." + Integer.toString(i);
				
				try {
					if (parseOre(logger, path, assertObject(path, ore))) { c++; }
				} catch(DataParserException e) {
					logger.error(e.getMessage());
				}
				i++;
			}
		}
		
		logComplete(logger, c, i, "info.parser.ore_dictionary.complete", "info.parser.ore_dictionary.incomplete");
	
		return true;
	}
	
	protected boolean parseOre(Logger logger, String path, JsonObject ore) throws DataParserException {
		ItemStack item = parseItemStack(logger, path + ".item", getElement(path, ore, "item", true));
		if (item == null) { return false; }
		String orename = getString(path, ore, "ore", true);
		
		OreDictionary.registerOre(orename, item);
		return true;
	}
}
