package jaminv.advancedmachines.objects.variant;

import jaminv.advancedmachines.lib.util.Variant;

public enum VariantDust implements Variant {
	COAL("coal"),
	IRON("iron"),
	GOLD("gold"),
	TITANIUM("titanium"),
	COPPER("copper"),
	SILVER("silver"),
	DIAMOND("diamond"),
	ENDER("ender"),
	GLASS("glass");
	
	protected String name;
	
	private VariantDust(String name) {
		this.name = name;
	}

	@Override
	public String getName() { return this.name; }
}
