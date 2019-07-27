package jaminv.advancedmachines.util.conditions;

import java.util.function.BooleanSupplier;

import com.google.gson.JsonObject;

import jaminv.advancedmachines.util.ModConfig;
import jaminv.advancedmachines.util.Reference;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaryConditionFactory implements IConditionFactory {

	@Override
	public BooleanSupplier parse(JsonContext context, JsonObject json) {
		String type = JsonUtils.getString(json, "type");
		
		if (type.equals(Reference.MODID + ":oredictionary")) {
			String prop = JsonUtils.getString(json, "property");
			String key = JsonUtils.getString(json, "key");
			switch (prop) {
			case "exists":
				return () -> OreDictionary.doesOreNameExist(key);
			case "notexists":
				return () -> !OreDictionary.doesOreNameExist(key);
			default:
				break;
			}
		}
		return () -> false;
	}

}
