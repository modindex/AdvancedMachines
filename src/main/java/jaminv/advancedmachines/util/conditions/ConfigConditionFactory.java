package jaminv.advancedmachines.util.conditions;

import java.util.function.BooleanSupplier;

import com.google.gson.JsonObject;

import jaminv.advancedmachines.ModConfig;
import jaminv.advancedmachines.ModReference;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

public class ConfigConditionFactory implements IConditionFactory {

	@Override
	public BooleanSupplier parse(JsonContext context, JsonObject json) {
		String type = JsonUtils.getString(json, "type");
		
		if (type.equals(ModReference.MODID + ":config")) {
			String value = JsonUtils.getString(json, "value");
			switch (value) {
			case "gearIron":
				return () -> ModConfig.crafting.craftIronGear;
			case "gearTitanium":
				return () -> ModConfig.crafting.craftTitaniumGear;
			case "blockTitanium":
				return () -> ModConfig.crafting.craftTitaniumBlock;
			case "blockCopper":
				return () -> ModConfig.crafting.craftCopperBlock;
			case "blockSilver":
				return () -> ModConfig.crafting.craftSilverBlock;
			case "toolTitanium":
				return () -> ModConfig.crafting.craftTitaniumTools;
			case "armorTitanium":
				return () -> ModConfig.crafting.craftTitaniumArmor;
			case "toolCopper":
				return () -> ModConfig.crafting.craftCopperTools;
			case "armorCopper":
				return () -> ModConfig.crafting.craftCopperArmor;
			case "toolSilver":
				return () -> ModConfig.crafting.craftSilverTools;
			case "armorSilver":
				return () -> ModConfig.crafting.craftSilverArmor;
			case "ae2Press":
				return () -> ModConfig.recipe.allowPressAE2;
			default:
				break;
			}
		}
		return () -> false;
	}
}