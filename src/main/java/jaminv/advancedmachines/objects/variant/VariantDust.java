package jaminv.advancedmachines.objects.variant;

import jaminv.advancedmachines.lib.util.Variant;
import jaminv.advancedmachines.objects.ItemGlint;

public enum VariantDust implements Variant, ItemGlint.CanGlint {
	COAL("coal"),
	IRON("iron"),
	GOLD("gold"),
	TITANIUM("titanium"),
	COPPER("copper"),
	SILVER("silver"),
	DIAMOND("diamond"),
	ENDER("ender"),
	GLASS("glass"),
	NETHER_STAR("nether_star", true);
	
	protected String name;
	boolean glint;
	
	private VariantDust(String name, boolean glint) {
		this.name = name;
		this.glint = glint;
	}
	
	private VariantDust(String name) {
		this(name, false);
	}

	@Override
	public String getName() { return this.name; }
	
	public boolean hasGlint() { return glint; }
}
