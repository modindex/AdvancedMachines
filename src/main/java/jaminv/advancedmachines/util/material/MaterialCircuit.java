package jaminv.advancedmachines.util.material;

import jaminv.advancedmachines.util.ModConfig;

public class MaterialCircuit extends MaterialBase {

	private static MaterialType TYPE = MaterialType.CIRCUIT;
	
	public static final MaterialCircuit BASIC = new MaterialCircuit(0, "basic");
	public static final MaterialCircuit ADVANCED = new MaterialCircuit(1, "advanced");
	
	public static MaterialBase[] values() {
		return MaterialBase.values(TYPE);
	}
	
	public static MaterialBase byMetadata(int meta) {
		return MaterialBase.byMetadata(TYPE, meta);
	}
	
	private MaterialCircuit(int meta, String name) {
		super(TYPE, meta, name);
	} 

	@Override
	public boolean doInclude(String oredictType) {
		return true;
//		return ModConfig.material.doInclude("process_" + getName());
	}
}
