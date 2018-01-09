package jaminv.advancedmachines.objects.items.material;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.text.WordUtils;

import jaminv.advancedmachines.util.Config;
import jaminv.advancedmachines.util.handlers.EnumHandler.EnumMaterial;
import net.minecraft.util.IStringSerializable;

public abstract class MaterialBaseOre extends MaterialBase {
	
	int harvestLevel;
	
	protected MaterialBaseOre(MaterialType type, int meta, String name, int harvestLevel) {
		this(type, meta, name, name, harvestLevel);
	}
	
	protected MaterialBaseOre(MaterialType type, int meta, String name, String unlocalizedName, int harvestLevel) {
		super(type, meta, name, unlocalizedName);
		this.harvestLevel = harvestLevel;
	}
	
	public int getHarvestLevel() {
		return harvestLevel;
	}
}
