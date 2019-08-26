package jaminv.advancedmachines.util.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

import jaminv.advancedmachines.lib.recipe.RecipeInput;
import jaminv.advancedmachines.lib.recipe.RecipeOutput;
import jaminv.advancedmachines.util.Reference;
import jaminv.advancedmachines.util.logger.Logger;
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

public class FileHandlerJsonMap implements FileHandler {
	
	protected Map<String, JsonObject> map = new HashMap<String, JsonObject>();
	public Map<String, JsonObject> getMap() { return ImmutableMap.copyOf(map); }
	
	String logName;
	
	public FileHandlerJsonMap(String logName) {
		this.logName = logName;		
	}
	
	@Override
	public boolean parseData(Logger logger, String filename, JsonObject json) throws DataParserException {
		logger = logger.getLogger("constants");
		
		int i = 0, c = 0;
		for (Map.Entry<String,JsonElement> entry : json.entrySet()) {
			String name = entry.getKey();
			try {
				JsonObject constant = JsonUtils.getJsonObject(entry.getValue(), name);
				map.put(name, constant);
				c++;
			} catch(JsonSyntaxException e) {
				logger.error(e.getMessage());
			}
			i++;
		}
		
		ParseUtils.logComplete(logger, c, i, "%d constants added successfully.", "%d constants not added.");
		return true;
	}
}
