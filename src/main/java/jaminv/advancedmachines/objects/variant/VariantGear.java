package jaminv.advancedmachines.objects.variant;

public enum VariantGear implements Variant {
	IRON("iron"),
	TITANIUM("titanium");

	protected String name;
	
	private VariantGear(String name) {
		this.name = name;
	} 

	@Override public String getName() { return name; }
}
