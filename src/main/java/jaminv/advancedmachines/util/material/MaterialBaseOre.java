package jaminv.advancedmachines.util.material;

public abstract class MaterialBaseOre extends MaterialBase {
	
	int harvestLevel;
	
	protected MaterialBaseOre(MaterialType type, int meta, String name, int harvestLevel) {
		this(type, meta, name, name, harvestLevel);
	}
	
	protected MaterialBaseOre(MaterialType type, int meta, String name, String unlocalizedName, int harvestLevel) {
		super(type, meta, name, unlocalizedName);
		this.harvestLevel = harvestLevel;
	}
	
	public int getHarvestLevel() {
		return harvestLevel;
	}
}
