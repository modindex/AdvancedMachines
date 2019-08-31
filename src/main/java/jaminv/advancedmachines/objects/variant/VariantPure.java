package jaminv.advancedmachines.objects.variant;

public enum VariantPure implements Variant {
	GOLD("gold"),
	COPPER("copper"),
	SILVER("silver"),
	DIAMOND("diamond");

	String name;
	
	private VariantPure(String name) {
		this.name = name;
	}

	@Override public String getName() { return name; }
}
