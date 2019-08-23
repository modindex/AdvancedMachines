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
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
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
	
	/**
	 * Parse an ItemStack from Json
	 * @param json JsonElement, can be a string (item name), or an object with multiple elements that describe the item.
	 * @param memberName Used for error logging
	 * @return ItemStack
	 * @throws DataParserException
	 */
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
		
		ItemStack stack = new ItemStack(item, count, meta, parseNbt(obj,"nbt"));		
		return stack;
	}
	
	protected FluidStack parseFluidStack(JsonElement json, String memberName) throws DataParserException {
		if (json == null) { throw new DataParserException("Missing fluidstack element: " + memberName); }
		JsonObject obj = parseConstants(getJsonObject(json, memberName));
		
		int amount = JsonUtils.getInt(obj, "amount");
		String fluidname = JsonUtils.getString(obj, "fluid");

		Fluid fluid = FluidRegistry.getFluid(fluidname);
		if (fluid == null) { 
			throw new DataParserException("Fluid '" + fluidname + "' not found.");
		}
		
		FluidStack stack = new FluidStack(fluid, amount, parseNbt(obj, "nbt"));		
		return stack;
	}
	
	protected NBTTagCompound parseNbt(JsonObject json, String memberName) throws DataParserException {
		JsonElement jnbt = json.get(memberName);
		if (jnbt != null) {
			try {
				return JsonToNBT.getTagFromJson(jnbt.getAsString());
			} catch(NBTException e) {
				throw new DataParserException("Error parsing NBT:" + e.getMessage());
			}
		}
		return null;
	}
	
	protected void logComplete(Logger logger, int complete, int total, String complete_message, String incomplete_message) {
		logger.info(String.format(complete_message, complete));
		if (complete < total) {
			logger.warn(String.format(incomplete_message, total - complete));
		}
	}
}
