package jaminv.advancedmachines.util.material;

import jaminv.advancedmachines.util.ModConfig;

public class MaterialAlloy extends MaterialBase {

	private static MaterialType TYPE = MaterialType.ALLOY;
	
	public static final MaterialAlloy TITANIUM_CARBIDE = new MaterialAlloy(0, "titanium_carbide");
	public static final MaterialAlloy TITANIUM_ENDITE = new MaterialAlloy(1, "titanium_endite");
		
	
	public static MaterialBase[] values() {
		return MaterialBase.values(TYPE);
	}
	
	public static MaterialBase byMetadata(int meta) {
		return MaterialBase.byMetadata(TYPE, meta);
	}
	
	private MaterialAlloy(int meta, String name) {
		super(TYPE, meta, name);
	} 

	@Override
	public boolean doInclude(String oredictType) {
		return ModConfig.material.doInclude("alloy_" + getName());
	}
}
