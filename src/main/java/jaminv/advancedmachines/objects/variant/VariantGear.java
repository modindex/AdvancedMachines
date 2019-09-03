package jaminv.advancedmachines.objects.variant;

import jaminv.advancedmachines.lib.util.helper.Variant;

public enum VariantGear implements Variant {
	IRON("iron"),
	TITANIUM("titanium");

	protected String name;
	
	private VariantGear(String name) {
		this.name = name;
	} 

	@Override public String getName() { return name; }
}
