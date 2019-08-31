package jaminv.advancedmachines.objects.variant;

public enum VariantCircuit implements Variant {
	PCB("pcb"),
	BASIC("basic"),
	ADVANCED("advanced"),
	ENDER("ender"),
	IMPROBABLE("improbable");

	protected String name;
	
	private VariantCircuit(String name) {
		this.name = name;
	} 

	@Override public String getName() { return name; }
}
