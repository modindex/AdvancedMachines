package jaminv.advancedmachines.util.handlers;

import net.minecraft.util.IStringSerializable;

public class EnumHandler {
	public static enum EnumMaterial implements IStringSerializable {
		TITANIUM(0, "titanium"),
		COPPER(1, "copper");
		
		private static final EnumMaterial[] META_LOOKUP = new EnumMaterial[values().length];
		private final int meta;
		private final String name, unlocalizedName;
		
		private EnumMaterial(int meta, String name) {
			this(meta, name, name);
		}
		
		private EnumMaterial(int meta, String name, String unlocalizedName) {
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
		
		public static EnumMaterial byMetadata(int meta) {
			return META_LOOKUP[meta];
		}
		
		static {
			for (EnumMaterial enumtype : values()) {
				META_LOOKUP[enumtype.getMeta()] = enumtype;
			}
		}
	}
}
