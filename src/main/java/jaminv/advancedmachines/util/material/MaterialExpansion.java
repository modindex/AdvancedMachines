package jaminv.advancedmachines.util.material;

import jaminv.advancedmachines.util.Config;
import jaminv.advancedmachines.util.material.PropertyMaterial;

public class MaterialExpansion extends MaterialBase {

	private static MaterialType TYPE = MaterialType.EXPANSION;
	
	public static final MaterialExpansion BASIC = new MaterialExpansion(0, "basic", 1);
	public static final MaterialExpansion COMPRESSED = new MaterialExpansion(1, "compressed", 2);
	public static final MaterialExpansion QUAD = new MaterialExpansion(2, "quad", 4);
	public static final MaterialExpansion IMPOSSIBLE = new MaterialExpansion(3, "impossible", 8);
	
	public static MaterialExpansion[] values() {
		return MaterialBase.values(TYPE, new MaterialExpansion[0]);
	} 
	
	public static MaterialExpansion byMetadata(int meta) {
		return (MaterialExpansion)(MaterialBase.byMetadata(TYPE, meta));
	}
	
	protected final int multiplier;
	
	private MaterialExpansion(int meta, String name, int multiplier) {
		super(TYPE, meta, name);
		this.multiplier = multiplier;
	}

	@Override
	public boolean doInclude(String oredictType) {
		return true;
	}
	
	public int getMultiplier() {
		return this.multiplier;
	}
}
