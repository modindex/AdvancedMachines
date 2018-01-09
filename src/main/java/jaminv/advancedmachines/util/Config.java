package jaminv.advancedmachines.util;

import org.apache.logging.log4j.Level;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.proxy.CommonProxy;
import net.minecraftforge.common.config.Configuration;

public class Config {
	
	private static final String CATEGORY_GENERAL = "general";
	private static final String CATEGORY_MATERIAL = "material";
	private static final String CATEGORY_TOOLS = "tools";
	private static final String CATEGORY_WORLDGEN = "worldgen";
	
	public static boolean doFurnace = true;
	public static int tickUpdate = 5;
	public static int processTimeBasic = 200;
	
	public static boolean doIncludeTitanium = true;
	public static boolean doIncludeCopper = true;
	public static boolean doIncludeSilver = true;
	
	public static boolean doIncludePure = true;
	public static boolean doIncludeVanillaDust = true;
	public static boolean doIncludeDiamondDust = true;
	
	public static boolean doAddTitaniumTools = true;
	public static boolean doAddTitaniumArmor = true;
	public static boolean doAddCopperTools = false;
	public static boolean doAddCopperArmor = false;
	
	public static boolean doGenerateTitanium = true;
	public static int titaniumVeinSize = 4;
	public static int titaniumMinHeight = 0;
	public static int titaniumMaxHeight = 24;
	public static int titaniumChance = 2;
	
	public static boolean doGenerateCopper = false;
	
	public static void readConfig() {
		Configuration cfg = CommonProxy.config;
		try {
			cfg.load();
			initGeneralConfig(cfg);
			initMaterialConfig(cfg);
			initToolConfig(cfg);
			initWorldGenConfig(cfg);
		} catch (Exception e) {
			Main.logger.log(Level.ERROR, "Problem loading config file!",  e);
		} finally {
			if (cfg.hasChanged()) {
				cfg.save();
			}
		}
	}
	
	private static void initGeneralConfig(Configuration cfg) {
		cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General Configuration");
		doFurnace = cfg.getBoolean("doFurnace", CATEGORY_GENERAL, doFurnace, "Include Advanced Furnace Recipes");
		tickUpdate = cfg.getInt("tickUpdate", CATEGORY_GENERAL, 5, 1, 100, "Machines only run full updates every X ticks");
		processTimeBasic = cfg.getInt("processTimeBasic", CATEGORY_GENERAL, 200, 0, 10000, "Processing time for machines with no upgrades");
	}
	
	private static void initMaterialConfig(Configuration cfg) {
		cfg.addCustomCategoryComment(CATEGORY_MATERIAL, "Material Configuration\n\n"
			+ "Disabling a material here will prevent creation of all blocks and items associated with the material.\n"
			+ "This includes ores, ingots, blocks, dust, etc. It will also disable world generation for those ores.\n\n"
			+ "This may prevent certain aspects of the mod from working, unless another mod provides an ore dictionary\n"
			+ "equivalent to the disabled material.");
		doIncludeTitanium = cfg.getBoolean("doIncludeTitanium", CATEGORY_MATERIAL, doIncludeTitanium, "Set to false to prevent this mod from creating any Titanium blocks or items");
		doIncludeCopper = cfg.getBoolean("doIncludeCopper", CATEGORY_MATERIAL, doIncludeCopper, "Set to false to prevent this mod from creating any Copper blocks or items");
		doIncludeSilver = cfg.getBoolean("doIncludeSilver", CATEGORY_MATERIAL, doIncludeSilver, "Set to false to prevent this mod from creating any Silver blocks or items");
		doIncludePure = cfg.getBoolean("doIncludePure", CATEGORY_MATERIAL, doIncludePure, "Set to false to disable purified materials (may make advanced recipies impossible)");
		doIncludeVanillaDust = cfg.getBoolean("doIncludeVanillaDust", CATEGORY_MATERIAL, doIncludeVanillaDust, "Set to false to disable vanilla dust (gold and silver)");
		doIncludeDiamondDust = cfg.getBoolean("doIncludeDiamondDust", CATEGORY_MATERIAL, doIncludeDiamondDust, "Set to false to disable diamond dust (may make advanced recipes impossible)");
	}
	
	private static void initToolConfig(Configuration cfg) {
		cfg.addCustomCategoryComment(CATEGORY_TOOLS, "Tool Configuration\n\n"
			+ "Disable these options to prevent mod from making various tools and armor available.\n\n"
			+ "This is seperate from the material config above.  It is impossible to include tools and armor, but\n"
			+ "not the materials for those tools.  The recipes will not be possible without these materials, however,\n"
			+ "unless another mod provides an ore dictionary equivalent.");
		doAddTitaniumTools = cfg.getBoolean("doAddTitaniumTools", CATEGORY_TOOLS, doAddTitaniumTools, "Set to false to disable Titanium tools");
		doAddTitaniumArmor = cfg.getBoolean("doAddTitaniumArmor", CATEGORY_TOOLS, doAddTitaniumArmor, "Set to false to disable Titanium armor");
		doAddCopperTools = cfg.getBoolean("doAddCopperTools", CATEGORY_TOOLS, doAddCopperTools, "Set to false to disable Copper tools");
		doAddCopperArmor = cfg.getBoolean("doAddCopperArmor", CATEGORY_TOOLS, doAddCopperArmor, "Set to false to disable Copper armor");
	}
	
	private static void initWorldGenConfig(Configuration cfg) {
		cfg.addCustomCategoryComment(CATEGORY_WORLDGEN, "World Gen Configuration\n\n"
			+ "Disable or tweak world ore generation settings for this mod.\n\n"
			+ "Disabling ore generation for a material may prevent certain aspects of the mod from working, unless\n"
			+ "another mod provides ore generation for an ore dictionary equivalent to that material.");
		doGenerateTitanium = cfg.getBoolean("doGenerateTitanium", CATEGORY_WORLDGEN, doGenerateTitanium, "Set to false to prevent Titanium Ore generation in the world");
		titaniumVeinSize = cfg.getInt("titaniumVeinSize", CATEGORY_WORLDGEN, titaniumVeinSize, 0, 64, "Maximum number of Titanium Ore in a vein");
		titaniumMinHeight = cfg.getInt("titaniumStartY", CATEGORY_WORLDGEN, titaniumMinHeight, 0, 256, "Bottom Y-level for Titanium Ore generation");
		titaniumMaxHeight = cfg.getInt("titaniumEndY", CATEGORY_WORLDGEN, titaniumMaxHeight, 0, 256, "Top Y-level for Titanium Ore generation");
		titaniumChance = cfg.getInt("titaniumChance", CATEGORY_WORLDGEN, titaniumChance, 0, 400, "Number of times per chunk that it will attempt to generate Titanium Ore");
	}
	
	private static void initDependentConfigs() {
		if (doIncludeTitanium == false) {
			doGenerateTitanium = false;
		}
		if (doIncludeCopper == false) {
			doGenerateCopper = false;
		}
	}
	
	public static boolean doInclude(String material) {
		switch (material) {
		case "titanium": return doIncludeTitanium;
		case "copper": return doIncludeCopper;
		case "silver": return doIncludeSilver;
		case "pure": return doIncludePure;
		case "vanillaDust": return doIncludeVanillaDust;
		case "diamondDust": return doIncludeDiamondDust;
		}
		return false;
	}
	
	public static boolean doGenerate(String material) {
		switch (material) {
		case "titanium": return doGenerateTitanium;
		case "copper": return doGenerateCopper;
		}
		return false;
	}

	public static boolean doAddTools(String material) {
		switch (material) {
		case "titanium": return doAddTitaniumTools;
		case "copper": return doAddCopperTools;
		}
		return false;
	}
	
	public static boolean doAddArmor(String material) {
		switch (material) {
		case "titanium": return doAddTitaniumArmor;
		case "copper": return doAddCopperArmor;
		}
		return false;
	}
}
