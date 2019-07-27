package jaminv.advancedmachines.util.conditions;

import java.util.function.BooleanSupplier;

import com.google.gson.JsonObject;

import jaminv.advancedmachines.util.ModConfig;
import jaminv.advancedmachines.util.Reference;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

public class ConfigConditionFactory implements IConditionFactory {

	@Override
	public BooleanSupplier parse(JsonContext context, JsonObject json) {
		String type = JsonUtils.getString(json, "type");
		
		if (type.equals(Reference.MODID + ":config")) {
			String value = JsonUtils.getString(json, "value");
			switch (value) {
			case "gearIron":
				return () -> ModConfig.crafting.craftIronGear;
			case "gearTitanium":
				return () -> ModConfig.crafting.craftTitaniumGear;
			default:
				break;
			}
		}
		return () -> false;
	}

}
