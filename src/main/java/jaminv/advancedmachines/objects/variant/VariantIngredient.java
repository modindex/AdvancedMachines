package jaminv.advancedmachines.objects.variant;

import jaminv.advancedmachines.lib.util.helper.Variant;

public enum VariantIngredient implements Variant {
	COPPER_WIRE("copper_wire"),
	SILVER_WIRE("silver_wire"),
	GOLD_WIRE("gold_wire"),
	PLASTIC("plastic"),
	TAR("tar"),
	ROSIN("rosin");

	protected String name;
	
	private VariantIngredient(String name) {
		this.name = name;
	} 

	@Override public String getName() { return name; }
}
