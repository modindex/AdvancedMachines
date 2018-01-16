package jaminv.advancedmachines.objects.items.material;

import jaminv.advancedmachines.util.Config;

public class MaterialDust extends MaterialBase {

	private static MaterialType TYPE = MaterialType.DUST;
	
	public static final MaterialDust COAL = new MaterialDust(0, "coal");
	public static final MaterialDust IRON = new MaterialDust(1, "iron");
	public static final MaterialDust GOLD = new MaterialDust(2, "gold");
	public static final MaterialDust TITANIUM = new MaterialDust(3, "titanium");
	public static final MaterialDust COPPER = new MaterialDust(4, "copper");
	public static final MaterialDust SILVER = new MaterialDust(5, "silver");
	public static final MaterialDust DIAMOND = new MaterialDust(6, "diamond");
	
	public static MaterialBase[] values() {
		return MaterialBase.values(TYPE);
	}
	
	public static MaterialBase byMetadata(int meta) {
		return MaterialBase.byMetadata(TYPE, meta);
	}
	
	private MaterialDust(int meta, String name) {
		super(TYPE, meta, name);
	} 

	@Override
	public boolean doInclude(String oredictType) {
		switch (getName()) {
		case "gold":
		case "iron":
		case "coal":
			return Config.doInclude("vanillaDust");
		case "diamond":
			return Config.doInclude("diamondDust");
		default:
			return Config.doInclude(getName());
		}
	}
}
