package jaminv.advancedmachines.objects.variant;

import jaminv.advancedmachines.lib.util.Variant;

public enum VariantResource implements Variant {
	TITANIUM("titanium", 2),
	COPPER("copper", 1),
	SILVER("silver", 1);
	
	protected String name;
	protected int harvestLevel;
	
	private VariantResource(String name, int harvestLevel) {
		this.name = name;
		this.harvestLevel = harvestLevel;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public int getHarvestLevel() {
		return harvestLevel;
	}
}
