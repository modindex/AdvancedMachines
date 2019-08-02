package jaminv.advancedmachines.client.textureset;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

import jaminv.advancedmachines.util.Reference;
import jaminv.advancedmachines.util.logger.Logger;
import jaminv.advancedmachines.util.parser.DataParser;
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
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class FileHandlerTextureSet extends FileHandlerBase {

	@Override
	public boolean parseData(Logger logger, String filename, JsonObject json) {
		Map<String, JsonObject> constants = getConstants();
		logger = logger.getLogger("texturesets");
		
		int i = 0, c = 0;
		for (Map.Entry<String,JsonElement> entry : json.entrySet()) {
			String name = entry.getKey();
			try {
				String path = entry.getKey();
				if (getTextureSet(entry.getKey(), getJsonObject(entry.getValue(), entry.getKey()), logger)) { c++; }
			} catch(DataParserException | JsonSyntaxException e) {
				logger.error(e.getMessage());
			}
			i++;
		}
		
		logComplete(logger, c, i, "info.parser.constant.complete", "info.parser.constant.incomplete");
		return true;
	}
	
	protected boolean getTextureSet(String path, JsonObject json, Logger logger) {
		for(Map.Entry<String,JsonElement> entry : json.entrySet()) {
			String newpath = path + "." + entry.getKey();
			
			try {
				if (JsonUtils.isString(json, entry.getKey())) {
					TextureSets.put(newpath, JsonUtils.getString(json, entry.getKey()));
				} else {
					getTextureSet(newpath, getJsonObject(entry.getValue(), entry.getKey()), logger);
				}
			} catch(DataParserException | JsonSyntaxException e) {
				logger.error(e.getMessage());
			}
		}	
		
		return true;
	}
}
