package jaminv.advancedmachines.objects.variant;

import java.util.HashMap;
import java.util.Map;

import jaminv.advancedmachines.lib.util.Variant;

public enum VariantExpansion implements Variant {
	BASIC("basic", 1),
	COMPRESSED("compressed", 2),
	QUAD("quad", 4),
	IMPROBABLE("improbable", 8);
	
	public static interface HasVariant {
		public VariantExpansion getVariant();
	}
	
	protected static Map<String, VariantExpansion> nameLookup = new HashMap<>();	
	
	public static final int maxMultiplier = 64;

	public static VariantExpansion lookup(String name) {
		return nameLookup.get(name);
	}
	
	protected final String name;
	protected final int multiplier;
	
	private VariantExpansion(String name, int multiplier) {
		this.name = name;
		this.multiplier = multiplier;
	}
	
	static {
		for (VariantExpansion variant : values()) {
			nameLookup.put(variant.name, variant);
		}
	}
	
	public int getMultiplier() {
		return this.multiplier;
	}

	@Override
	public String getName() {
		return name;
	}
}
