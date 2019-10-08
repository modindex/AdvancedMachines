package jaminv.advancedmachines.objects.variant;

import jaminv.advancedmachines.lib.util.Variant;
import jaminv.advancedmachines.objects.ItemGlint;

public enum VariantCircuit implements Variant, ItemGlint.CanGlint {
	PCB("pcb"),
	BASIC("basic"),
	ADVANCED("advanced"),
	ENDER("ender"),
	IMPROBABLE("improbable", true);

	protected String name;
	protected boolean hasGlint;
	
	private VariantCircuit(String name, boolean hasGlint) {
		this.name = name;
		this.hasGlint = hasGlint;
	}
	
	private VariantCircuit(String name) {
		this(name, false);
	}

	@Override public String getName() { return name; }

	@Override
	public boolean hasGlint() {
		return hasGlint;
	}
}
