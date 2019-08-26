package jaminv.advancedmachines.lib.util.registry;

public interface MetaVariant {
	public String getId();
	public MetaVariant byMetadata(int meta);
	
	public int getMeta();
	public String getName();
}
