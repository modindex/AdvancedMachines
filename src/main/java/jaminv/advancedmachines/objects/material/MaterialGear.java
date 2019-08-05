package jaminv.advancedmachines.objects.material;

import jaminv.advancedmachines.util.ModConfig;

public class MaterialGear extends MaterialBase {

	private static MaterialType TYPE = MaterialType.GEAR;
	
	public static final MaterialGear IRON = new MaterialGear(0, "iron");
	public static final MaterialGear TITANIUM = new MaterialGear(1, "titanium");
	
	public static MaterialBase[] values() {
		return MaterialBase.values(TYPE);
	}
	
	public static MaterialBase byMetadata(int meta) {
		return MaterialBase.byMetadata(TYPE, meta);
	}
	
	private MaterialGear(int meta, String name) {
		super(TYPE, meta, name);
	} 

	@Override
	public boolean doInclude(String oredictType) {
		return ModConfig.material.doInclude("gear_" + getName());
	}
}
