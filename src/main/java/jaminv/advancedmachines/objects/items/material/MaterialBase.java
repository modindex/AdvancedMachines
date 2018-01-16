package jaminv.advancedmachines.objects.items.material;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.google.common.base.Optional;

import net.minecraft.block.properties.IProperty;
import net.minecraft.util.IStringSerializable;
import scala.actors.threadpool.Arrays;

public abstract class MaterialBase implements Comparable<MaterialBase>, IStringSerializable {
	
	public static enum MaterialType {
		MOD, PURE, DUST, MACHINE
	}
	
	public static class MaterialRegistry {
		private static Map<MaterialType, SortedMap<Integer, MaterialBase>> lookup = new HashMap<MaterialType, SortedMap<Integer, MaterialBase>>();
		private static Map<MaterialType, Map<String, MaterialBase>> nameLookup = new HashMap<MaterialType, Map<String, MaterialBase>>();
		
		private static void registerMaterial(MaterialType type, String name, int meta, MaterialBase mat) {
			SortedMap<Integer, MaterialBase> map = lookup.get(type);
			if (map == null) {
				map = new TreeMap<Integer, MaterialBase>();
				lookup.put(type, map);
			}
			map.put(meta, mat);

			Map<String, MaterialBase> map2 = nameLookup.get(type);
			if (map2 == null) {
				map2 = new HashMap<String, MaterialBase>();
				nameLookup.put(type, map2);
			}
			map2.put(name, mat);					
		}
		
		public static MaterialBase lookupMeta(MaterialType type, int meta) {
			return lookup.get(type).get(meta);
		}
		public static MaterialBase lookupName(MaterialType type, String name) {
			return nameLookup.get(type).get(name);
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

	protected final MaterialType type;
	private final int meta;
	private final String name, unlocalizedName;
	
	protected MaterialBase(MaterialType type, int meta, String name) {
		this(type, meta, name, name);
	}
	
	protected MaterialBase(MaterialType type, int meta, String name, String unlocalizedName) {
		this.type = type;
		this.meta = meta;
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		
		MaterialRegistry.registerMaterial(type, name, meta, this);
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
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof MaterialBase && ((MaterialBase)obj).type == this.type && ((MaterialBase)obj).meta == this.meta;
	}	
	
	@Override
	public int compareTo(MaterialBase o) {
		if (this.type != o.type) {
			return this.type.ordinal() - o.type.ordinal();
		}
		return this.meta - o.meta;
	}
}
