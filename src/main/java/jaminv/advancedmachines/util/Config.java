package jaminv.advancedmachines.util;

import org.apache.logging.log4j.Level;

import java.util.HashMap;
import java.util.Map;

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
	public static int processTimeBasic = 100;
	public static int defaultFurnaceEnergy = 1400;
	public static int defaultGrinderEnergy = 2000;
	public static int defaultGrinderOreEnergy = 4000;
	public static int defaultPurifierEnergy = 30000;
	
	public static String[] excludeMaterials = { "example1", "example2" };
	public static String[] excludePure = new String[0];
	public static String[] excludeDust = new String[0];
	public static String[] excludeAlloy = new String[0];
	
	public static Map<String, Boolean> doExclude = new HashMap<String, Boolean>();
	
	public static boolean detectAndRemoveTF = true;
	public static boolean detectAndRemoveAE2 = true;
	
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
		tickUpdate = cfg.getInt("tickUpdate", CATEGORY_GENERAL, tickUpdate, 1, 100, "Machines only run full updates every X ticks");
		processTimeBasic = cfg.getInt("processTimeBasic", CATEGORY_GENERAL, processTimeBasic, 0, 10000, "Processing time for machines with no upgrades");
		defaultFurnaceEnergy = cfg.getInt("defaultFurnaceEnergy", CATEGORY_GENERAL, defaultFurnaceEnergy, 0, 100000, "Default energy for standard furnace recipes");
		defaultGrinderEnergy = cfg.getInt("defaultGrinderEnergy", CATEGORY_GENERAL, defaultGrinderEnergy, 0, 100000, "Default energy for standard grinder recipes");
		defaultGrinderOreEnergy = cfg.getInt("defaultGrinderOreEnergy", CATEGORY_GENERAL, defaultGrinderOreEnergy, 0, 100000, "Default energy for standard grinder ore recipes");
		defaultPurifierEnergy = cfg.getInt("defaultPurifierEnergy", CATEGORY_GENERAL, defaultPurifierEnergy, 0, 100000, "Default energy for standard purifier recipes");
	}
	
	private static void initMaterialConfig(Configuration cfg) {
		cfg.addCustomCategoryComment(CATEGORY_MATERIAL, "Material Configuration\n\n"
			+ "Disabling a material here will prevent creation of all blocks and items associated with the material.\n"
			+ "This includes ores, ingots, blocks, dust, etc. It will also disable world generation for those ores.\n\n"
			+ "This mod will attempt to use the ore dictionary for any recipes that include these materials;\n"
			+ "If no equivilent material is found, the recipe will not be available.\n"
			+ "This may make some items unavailable if a material is removed and these is no ore dictionary equivalent.\n\n"
			+ "This is intended to be used primarily when another mod provides the same material and you don't want duplicates.");
		
		// TODO: Detect and Remove
		detectAndRemoveTF = cfg.getBoolean("detectAndRemoveTF", CATEGORY_MATERIAL, detectAndRemoveTF, "Check for the existence of Thermal Foundation and remove all materials that coincide with it.\nNOT YET WORKING.");
		detectAndRemoveAE2 = cfg.getBoolean("detectAndRemoveAE2", CATEGORY_MATERIAL, detectAndRemoveTF, "Check for the existence of Applied Energestics 2 and remove all materials that coincide with it (basically just ender dust).\nNOT YET WORKING.");
		
		excludeMaterials = cfg.getStringList("excludeMaterials", CATEGORY_MATERIAL, excludeMaterials, "List of materials to exclude.\nValid options are 'titanium', 'copper', 'silver'.\n");
		excludePure = cfg.getStringList("excludePure", CATEGORY_MATERIAL, excludeMaterials, "List of pure ingots and dusts to exclude.\nValid options are 'gold', 'copper', 'silver', 'diamond', 'ender'.\n");
		excludeDust = cfg.getStringList("excludeDust", CATEGORY_MATERIAL, excludeMaterials, "List of dusts to exclude.\nValid options are 'coal', 'iron', 'gold', 'copper', 'silver', 'diamond', 'ender'.\n");
		excludeAlloy = cfg.getStringList("excludeAlloy", CATEGORY_MATERIAL, excludeMaterials, "List of alloys to exclude.\nValid options are 'titanium_carbide', 'titanium_endite'.\n");
		
		for (String mat : excludeMaterials) {
			doExclude.put("material_" + mat, true);
		}
		for (String pure : excludePure) {
			doExclude.put("pure_" + pure, true);
		}
		for (String dust : excludeDust) {
			doExclude.put("dust_" + dust, true);
		}
		for (String alloy : excludeAlloy) {
			doExclude.put("alloy_" + alloy, true);
		}
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
		if (!doInclude("material_titanium")) {
			doGenerateTitanium = false;
		}
		if (!doInclude("material_copper")) {
			doGenerateCopper = false;
		}
	}
	
	public static boolean doInclude(String material) {
		Boolean ex = doExclude.get(material);
		if (ex == null) { return true; }
		if (ex == true) { return false; }
		return true;
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
