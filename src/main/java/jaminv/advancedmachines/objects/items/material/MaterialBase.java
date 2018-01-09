package jaminv.advancedmachines.objects.items.material;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.text.WordUtils;

import jaminv.advancedmachines.util.Config;
import jaminv.advancedmachines.util.handlers.EnumHandler.EnumMaterial;
import net.minecraft.util.IStringSerializable;

public abstract class MaterialBase implements IStringSerializable {
	
	public static enum MaterialType {
		MOD, PURE, DUST
	}
	
	public static class MaterialRegistry {
		private static Map<MaterialType, SortedMap<Integer, MaterialBase>> lookup = new HashMap<MaterialType, SortedMap<Integer, MaterialBase>>();
		
		private static void registerMaterial(MaterialType type, int meta, MaterialBase mat) {
			SortedMap<Integer, MaterialBase> map = lookup.get(type);
			if (map == null) {
				map = new TreeMap<Integer, MaterialBase>();
				lookup.put(type, map);
			}
			
			map.put(meta, mat);
		}
	}
	
	
	public static <T> T[] values(MaterialType type, T[] a) {
		SortedMap<Integer, MaterialBase> map = MaterialRegistry.lookup.get(type);
		if (map == null) { return null; }
		
		return map.values().toArray(a);
	}
	
	public static MaterialBase[] values(MaterialType type) {
		return MaterialBase.values(type, new MaterialBase[0]);
	}
	
	public static MaterialBase byMetadata(MaterialType type, int meta) {
		SortedMap<Integer, MaterialBase> map = MaterialRegistry.lookup.get(type);
		if (map == null) { return null; }
		return map.get(meta);
	}

	private final int meta;
	private final String name, unlocalizedName;
	
	protected MaterialBase(MaterialType type, int meta, String name) {
		this(type, meta, name, name);
	}
	
	protected MaterialBase(MaterialType type, int meta, String name, String unlocalizedName) {
		this.meta = meta;
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		
		MaterialRegistry.registerMaterial(type, meta, this);
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getMeta() {
		return this.meta;
	}
	
	public String getUnlocalizedName() {
		return this.unlocalizedName;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public abstract boolean doInclude(String oredictType);
}
