package jaminv.advancedmachines.util.material;

import jaminv.advancedmachines.util.ModConfig;

public class MaterialCircuit extends MaterialBase {

	private static MaterialType TYPE = MaterialType.CIRCUIT;
	
	public static final MaterialCircuit PCB = new MaterialCircuit(0, "pcb");
	public static final MaterialCircuit BASIC = new MaterialCircuit(1, "basic");
	public static final MaterialCircuit ADVANCED = new MaterialCircuit(2, "advanced");
	public static final MaterialCircuit ENDER = new MaterialCircuit(3, "ender");
	public static final MaterialCircuit IMPROBABLE = new MaterialCircuit(4, "improbable");
	
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
