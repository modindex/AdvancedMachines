package jaminv.advancedmachines.util.parser;

import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import jaminv.advancedmachines.util.logger.Logger;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

public abstract class FileHandlerBase implements IFileHandler {
	
	public static int NO_DEFAULT = Integer.MIN_VALUE;
	
	private Map<String, JsonObject> constants;
	private Map<String, Map<String, Class>> factories;
	public Map<String, JsonObject> getConstants() { return constants; }
	public Map<String, Map<String, Class>> getFactories() {	return factories; }

	@Override
	public void setConstants(Map<String, JsonObject> constants) {
		this.constants = constants;
	}

	@Override
	public void setFactories(Map<String, Map<String, Class>> factories) {
		this.factories = factories;
	}
	
	protected JsonObject parseConstants(JsonObject obj) throws DataParserException {
		String name = JsonUtils.getString(obj, "constant", null);
		if (name == null) { return obj; }
		
		JsonObject constant = constants.get(name);
		if (constant == null) { throw new DataParserException("Constant '" + name + "' not found."); }
		
		for (Map.Entry<String,JsonElement> entry : constant.entrySet()) {
			obj.add(entry.getKey(), entry.getValue());
		}
		obj.remove("constant");
		return obj;
	}
	
	protected JsonObject getJsonObject(JsonObject json, String memberName) throws DataParserException { 
		return parseConstants(JsonUtils.getJsonObject(json, memberName)); 
	}
	protected JsonObject getJsonObject(JsonElement json, String memberName) throws DataParserException {
		return parseConstants(JsonUtils.getJsonObject(json, memberName));
	}
	
	protected ItemStack parseItemStack(JsonElement json, String memberName) throws DataParserException {
		if (json == null) { throw new DataParserException("Missing itemstack element: " + memberName); }
		
		if (JsonUtils.isString(json)) {
			String itemname = JsonUtils.getString(json, memberName);

			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemname));
			if (item == null) {
				throw new DataParserException("Item '" + itemname + "' not found.");
			}
			return new ItemStack(item);
		}
		
		JsonObject obj = parseConstants(getJsonObject(json, memberName));
		
		int meta;
		String metastring = JsonUtils.getString(obj, "data", null); 
		if (metastring != null && metastring.equals("*")) {
			meta = OreDictionary.WILDCARD_VALUE;
		} else { meta = JsonUtils.getInt(obj, "data", 0); }

		int count = JsonUtils.getInt(obj, "count", 1);
		
		String itemname = JsonUtils.getString(obj, "item");

		Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemname));
		if (item == null) { 
			throw new DataParserException("Item '" + itemname + "' not found.");
		}
		
		ItemStack stack = new ItemStack(item, count, meta);		
		
		NBTTagCompound tag = null;
		JsonElement jnbt = obj.get("nbt");
		if (jnbt != null) {
			try {
				stack.setTagCompound(JsonToNBT.getTagFromJson(jnbt.getAsString()));
			} catch(NBTException e) {
				throw new DataParserException("Error parsing item NBT:" + e.getMessage());
			}
		}
		
		return stack;
	}
	
	protected void logComplete(Logger logger, int complete, int total, String complete_message, String incomplete_message) {
		logger.info(String.format(complete_message, complete));
		if (complete < total) {
			logger.warn(String.format(incomplete_message, total - complete));
		}
	}
}
