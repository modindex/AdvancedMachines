package jaminv.advancedmachines.util.handlers;

import jaminv.advancedmachines.util.Config;
import jaminv.advancedmachines.util.interfaces.IEnumType;
import net.minecraft.util.IStringSerializable;

public class EnumHandler {
	
	public static enum EnumGui {
		PURIFIER(1),
		ALLOY(2);
		
		private final int id;
		private EnumGui(int id) {
			this.id = id;
		}
		
		public int getId() {
			return id;
		}
	}	

	public static enum EnumMaterial implements IStringSerializable, IEnumType {
		TITANIUM(0, "titanium", 2),
		COPPER(1, "copper", 1),
		SILVER(2, "silver", 1);
		
		private static final EnumMaterial[] META_LOOKUP = new EnumMaterial[values().length];
		private final int meta;
		private final String name, unlocalizedName;
		private int harvestLevel;
		
		private EnumMaterial(int meta, String name, int harvestLevel) {
			this(meta, name, name, harvestLevel);
		}
		
		private EnumMaterial(int meta, String name, String unlocalizedName, int harvestLevel) {
			this.meta = meta;
			this.name = name;
			this.unlocalizedName = unlocalizedName;
			this.harvestLevel = harvestLevel;
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
		
		public static EnumMaterial byMetadata(int meta) {
			return META_LOOKUP[meta];
		}
		
		static {
			for (EnumMaterial enumtype : values()) {
				META_LOOKUP[enumtype.getMeta()] = enumtype;
			}
		}
		
		public int getHarvestLevel() {
			return harvestLevel;
		}
		
		public boolean doInclude() {
			return Config.doInclude(this.name);
		}
	}
	
	public static enum EnumMaterialPure implements IStringSerializable, IEnumType {
		GOLD(0, "gold"),
		COPPER(1, "copper"),
		SILVER(2, "silver");
		
		private static final EnumMaterialPure[] META_LOOKUP = new EnumMaterialPure[values().length];
		private final int meta;
		private final String name, unlocalizedName;
		
		private EnumMaterialPure(int meta, String name) {
			this(meta, name, name);
		}
		
		private EnumMaterialPure(int meta, String name, String unlocalizedName) {
			this.meta = meta;
			this.name = name;
			this.unlocalizedName = unlocalizedName;
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
		
		public static EnumMaterialPure byMetadata(int meta) {
			return META_LOOKUP[meta];
		}
		
		static {
			for (EnumMaterialPure enumtype : values()) {
				META_LOOKUP[enumtype.getMeta()] = enumtype;
			}
		}
		
		public boolean doInclude() {
			return Config.doInclude("pure");
		}
	}
	
	public static enum EnumDust implements IStringSerializable, IEnumType {
		IRON(0, "iron"),
		GOLD(1, "gold"),
		TITANIUM(2, "titanium"),
		COPPER(2, "copper"),
		SILVER(3, "silver");
		
		private static final EnumDust[] META_LOOKUP = new EnumDust[values().length];
		private final int meta;
		private final String name, unlocalizedName;
		
		private EnumDust(int meta, String name) {
			this(meta, name, name);
		}
		
		private EnumDust(int meta, String name, String unlocalizedName) {
			this.meta = meta;
			this.name = name;
			this.unlocalizedName = unlocalizedName;
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
		
		public static EnumDust byMetadata(int meta) {
			return META_LOOKUP[meta];
		}
		
		static {
			for (EnumDust enumtype : values()) {
				META_LOOKUP[enumtype.getMeta()] = enumtype;
			}
		}
		
		public boolean doInclude() {
			return true;
/*			switch (name) {
			case "gold":
			case "iron":
				return Config.doInclude("vanillaDust");
			default:
				return Config.doInclude(name);
			} */
		}
	}	
}
