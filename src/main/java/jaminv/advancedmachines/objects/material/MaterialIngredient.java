package jaminv.advancedmachines.objects.material;

import jaminv.advancedmachines.util.ModConfig;

public class MaterialIngredient extends MaterialBase {

	private static MaterialType TYPE = MaterialType.INGREDIENT;
	
	public static final MaterialIngredient COPPER_WIRE = new MaterialIngredient(0, "copper_wire");
	public static final MaterialIngredient SILVER_WIRE = new MaterialIngredient(1, "silver_wire");
	public static final MaterialIngredient GOLD_WIRE = new MaterialIngredient(2, "gold_wire");
	public static final MaterialIngredient PLASTIC = new MaterialIngredient(3, "plastic");
	public static final MaterialIngredient TAR = new MaterialIngredient(4, "tar");
	public static final MaterialIngredient ROSIN = new MaterialIngredient(5, "rosin");
	
	public static MaterialBase[] values() {
		return MaterialBase.values(TYPE);
	}
	
	public static MaterialBase byMetadata(int meta) {
		return MaterialBase.byMetadata(TYPE, meta);
	}
	
	private MaterialIngredient(int meta, String name) {
		super(TYPE, meta, name);
	} 

	@Override
	public boolean doInclude(String oredictType) {
		return true;
	}
}
