package jaminv.advancedmachines.util.parser;

import org.apache.logging.log4j.Level;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import jaminv.advancedmachines.util.logger.Logger;
import jaminv.advancedmachines.util.recipe.RecipeInput;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

public abstract class FileHandlerBase implements IFileHandler {
	
	public static int NO_DEFAULT = Integer.MIN_VALUE;

	protected JsonElement getElement(String path, JsonObject parent, String key, boolean required) throws DataParserException {
		JsonElement element = parent.get(key);
		if (element == null) {
			if (required) { throw new DataParserException(I18n.format("error.parser.required", path, key)); }
			return null; 
		}
		return element;
	}
	
	protected JsonObject getObject(String path, JsonObject parent, String key, boolean required) throws DataParserException {
		JsonElement element = getElement(path, parent, key, required);
		if (element == null) { return null; }
		return assertObject(path + "." + key, element);
	}
	
	protected JsonObject assertObject(String path, JsonElement element) throws DataParserException {
		if (!element.isJsonObject()) { throw new DataParserException(I18n.format("error.parser.not_object", path)); }
		return (JsonObject)element;
	}

	protected JsonArray getArray(String path, JsonObject parent, String key, boolean required) throws DataParserException {
		JsonElement element = getElement(path, parent, key, required);
		if (element == null) { return null; }
		return assertArray(path + "." + key, element);
	}
	
	protected JsonArray assertArray(String path, JsonElement element) throws DataParserException {
		if (!element.isJsonArray()) { throw new DataParserException(I18n.format("error.parser.not_array", path)); }
		return (JsonArray)element;		
	}
		
	protected String getString(String path, JsonObject parent, String key, boolean required) throws DataParserException {
		JsonElement element = getElement(path, parent, key, required);
		if (element == null) { return null; }
		return assertString(path + "." + key, element);
	}
	
	protected String assertString(String path, JsonElement element) throws DataParserException {
		if (!element.isJsonPrimitive()) { throw new DataParserException(I18n.format("error.parser.not_string", path)); }
		return ((JsonPrimitive)element).getAsString();
	}
	
	protected int getInt(String path, JsonObject parent, String key, int def) throws DataParserException {
		int ret = def;
		JsonElement je = parent.get(key);
		if (je != null) {
			if (!je.isJsonPrimitive()) { throw new DataParserException(I18n.format("error.parser.not_integer", path, key)); }
			JsonPrimitive jp = (JsonPrimitive)je;
			try {
				ret = jp.getAsInt();
			} catch(NumberFormatException e) {
				throw new DataParserException(I18n.format("error.parser.not_integer", path, key));
			}
		} else if (def == NO_DEFAULT) {
			throw new DataParserException(I18n.format("error.parser.required", path, key));
		}
		return ret;
	}
	
	protected ItemStack parseItemStack(Logger logger, String path, JsonElement element) throws DataParserException {
		String itemname;
		Item item;
	
		if (element.isJsonPrimitive()) {
			itemname = element.getAsString();

			item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemname));
			if (item == null) {
				logger.warn(I18n.format("error.parser.recipe.item_not_found", path, itemname));
				return null; 
			}
			return new ItemStack(item);
		}
		
		JsonObject obj = assertObject(path, element);
		
		int meta = 0;
		String metastring = getString(path, obj, "data", false); 
		if (metastring != null && metastring.equals("*")) {
			meta = OreDictionary.WILDCARD_VALUE;
		} else { meta = getInt(path, obj, "data", 0); }
		int count = getInt(path, obj, "count", 1);
		
		itemname = getString(path, obj, "item", false);
		if (itemname == null) { return null; }

		item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemname));
		if (item == null) { 
			logger.warn(I18n.format("error.parser.recipe.item_not_found", path, itemname));
			return null; 
		}
		
		ItemStack stack = new ItemStack(item, count, meta);		
		
		NBTTagCompound tag = null;
		JsonElement jnbt = obj.get("nbt");
		if (jnbt != null) {
			try {
				stack.setTagCompound(JsonToNBT.getTagFromJson(jnbt.getAsString()));
			} catch(NBTException e) {
				logger.error(I18n.format("error.parser.recipe.invalid_nbt", path, e.getMessage()));
				return null;
			}
		}
		
		return stack;
	}
	
	protected void logComplete(Logger logger, int complete, int total, String complete_message, String incomplete_message) {
		logger.info(I18n.format(complete_message, complete));
		if (complete < total) {
			logger.warn(I18n.format(incomplete_message, total - complete));
		}
	}
}
