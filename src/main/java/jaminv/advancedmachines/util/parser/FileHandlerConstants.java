package jaminv.advancedmachines.util.parser;

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

public class FileHandlerConstants extends FileHandlerBase {
	
	@Override
	public boolean parseData(Logger logger, String filename, JsonObject json) throws DataParserException {
		Map<String, JsonObject> constants = getConstants();
		logger = logger.getLogger("constants");
		
		int i = 0, c = 0;
		for (Map.Entry<String,JsonElement> entry : json.entrySet()) {
			String name = entry.getKey();
			try {
				JsonObject constant = JsonUtils.getJsonObject(entry.getValue(), name);
				constants.put(name, constant);
				c++;
			} catch(JsonSyntaxException e) {
				logger.error(e.getMessage());
			}
			i++;
		}
		
		logComplete(logger, c, i, "%d constants added successfully.", "%d constants not added.");
		return true;
	}
}
