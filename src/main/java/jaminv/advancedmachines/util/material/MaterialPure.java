package jaminv.advancedmachines.util.material;

import jaminv.advancedmachines.util.Config;

public class MaterialPure extends MaterialBase {

	private static MaterialType TYPE = MaterialType.PURE; 
	
	public static final MaterialPure GOLD = new MaterialPure(0, "gold");
	public static final MaterialPure COPPER = new MaterialPure(1, "copper");
	public static final MaterialPure SILVER = new MaterialPure(2, "silver");
	public static final MaterialPure DIAMOND = new MaterialPure(3, "diamond");
	
	public static MaterialBase[] values() {
		return MaterialBase.values(TYPE);
	}
	
	public static MaterialBase byMetadata(int meta) {
		return MaterialBase.byMetadata(TYPE, meta);
	}
	
	private MaterialPure(int meta, String name) {
		super(TYPE, meta, name);
	}

	@Override
	public boolean doInclude(String oreDictType) {
		if (oreDictType == "ingot" && getName() == "diamond") { return false; }
		return Config.doInclude("pure_" + getName());		
	}		
}
