package jaminv.advancedmachines.util.conditions;

import java.util.function.BooleanSupplier;

import com.google.gson.JsonObject;

import jaminv.advancedmachines.ModReference;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidConditionFactory implements IConditionFactory {

	@Override
	public BooleanSupplier parse(JsonContext context, JsonObject json) {
		String type = JsonUtils.getString(json, "type");
		
		if (type.equals(ModReference.MODID + ":fluid")) {
			String prop = JsonUtils.getString(json, "property");
			String key = JsonUtils.getString(json, "key");
			switch (prop) {
			case "exists":
				return () -> FluidRegistry.getFluid(key) != null;
			case "notexists":
				return () -> FluidRegistry.getFluid(key) == null;
			default:
				break;
			}
		}
		return () -> false;
	}

}
