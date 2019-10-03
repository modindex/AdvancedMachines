package jaminv.advancedmachines.objects.variant;

import jaminv.advancedmachines.lib.util.Variant;

public enum VariantPure implements Variant {
	GOLD("gold"),
	COPPER("copper"),
	SILVER("silver"),
	DIAMOND("diamond"),
	ENDER("ender");

	String name;
	
	private VariantPure(String name) {
		this.name = name;
	}

	@Override public String getName() { return name; }
}
