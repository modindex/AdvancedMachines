package jaminv.advancedmachines.objects.items.material;

import jaminv.advancedmachines.objects.items.material.PropertyMaterial;
import jaminv.advancedmachines.util.Config;

public class MaterialExpansion extends MaterialBase {

	private static MaterialType TYPE = MaterialType.EXPANSION;
	
	public static final MaterialExpansion BASIC = new MaterialExpansion(0, "basic");
	public static final MaterialExpansion COMPRESSED = new MaterialExpansion(1, "compressed");
	public static final MaterialExpansion QUAD = new MaterialExpansion(2, "quad");
	public static final MaterialExpansion IMPOSSIBLE = new MaterialExpansion(3, "impossible");
	
	public static MaterialExpansion[] values() {
		return MaterialBase.values(TYPE, new MaterialExpansion[0]);
	} 
	
	public static MaterialBaseOre byMetadata(int meta) {
		return (MaterialBaseOre)(MaterialBase.byMetadata(TYPE, meta));
	}
	
	protected int harvestLevel;
	
	private MaterialExpansion(int meta, String name) {
		super(TYPE, meta, name);
	}

	@Override
	public boolean doInclude(String oredictType) {
		return true;
	}
}
