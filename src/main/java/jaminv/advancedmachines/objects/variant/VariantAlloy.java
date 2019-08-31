package jaminv.advancedmachines.objects.variant;

public enum VariantAlloy implements Variant {
	TITANIUM_CARBIDE("titanium_carbide"),
	TITANIUM_ENDITE("titanium_endite");
		
	protected String name;
	
	private VariantAlloy(String name) {
		this.name = name;
	} 

	@Override public String getName() { return name; }
}
