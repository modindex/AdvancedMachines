package jaminv.advancedmachines.objects.items.material;

import jaminv.advancedmachines.objects.items.material.PropertyMaterial;
import jaminv.advancedmachines.util.Config;

public class MaterialMod extends MaterialBaseOre {

	private static MaterialType TYPE = MaterialType.MOD;
	
	public static final MaterialMod TITANIUM = new MaterialMod(0, "titanium", 2);
	public static final MaterialMod COPPER = new MaterialMod(1, "copper", 1);
	public static final MaterialMod SILVER = new MaterialMod(2, "silver", 1);
	
	public static MaterialBaseOre[] values() {
		return MaterialBase.values(TYPE, new MaterialBaseOre[0]);
	} 
	
	public static MaterialBaseOre byMetadata(int meta) {
		return (MaterialBaseOre)(MaterialBase.byMetadata(TYPE, meta));
	}
	
	protected int harvestLevel;
	
	private MaterialMod(int meta, String name, int harvestLevel) {
		super(TYPE, meta, name, harvestLevel);
	}

	@Override
	public boolean doInclude(String oredictType) {
		return Config.doInclude(getName());
	}
}
