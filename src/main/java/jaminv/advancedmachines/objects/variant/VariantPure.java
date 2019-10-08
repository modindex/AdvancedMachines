package jaminv.advancedmachines.objects.variant;

import jaminv.advancedmachines.lib.util.Variant;
import jaminv.advancedmachines.objects.ItemGlint;

public enum VariantPure implements Variant, ItemGlint.CanGlint {
	GOLD("gold", true),
	COPPER("copper", true),
	SILVER("silver", true),
	DIAMOND("diamond", false),
	ENDER("ender", false, true),
	NETHER_STAR("nether_star", false, true);

	String name;
	boolean hasIngot;
	boolean glint;
	
	private VariantPure(String name, boolean hasIngot, boolean glint) {
		this.name = name;
		this.hasIngot = hasIngot;
		this.glint = glint;
	}
	
	private VariantPure(String name, boolean hasIngot) {
		this(name, hasIngot, false);
	}

	@Override public String getName() { return name; }
	
	public boolean hasIngot() { return hasIngot; }
	public boolean hasGlint() { return glint; }
}
