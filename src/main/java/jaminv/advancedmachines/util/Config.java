package jaminv.advancedmachines.util;

import org.apache.logging.log4j.Level;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.proxy.CommonProxy;
import net.minecraftforge.common.config.Configuration;

public class Config {
	
	private static final String CATEGORY_GENERAL = "general";
	private static final String CATEGORY_WORLDGEN = "worldgen";
	
	public static boolean doFurnace = true;
	
	public static int titaniumStartY = 3;
	public static int titaniumEndY = 11;
	public static int titaniumChance = 10;
	
	public static void readConfig() {
		Configuration cfg = CommonProxy.config;
		try {
			cfg.load();
			initGeneralConfig(cfg);
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
	}
	
	private static void initWorldGenConfig(Configuration cfg) {
		cfg.addCustomCategoryComment(CATEGORY_WORLDGEN, "World Gen Configuration");
		titaniumStartY = cfg.getInt("titaniumStartY", CATEGORY_WORLDGEN, titaniumStartY, 0, 256, "Bottom Y-level for Titanium generation");
		titaniumEndY = cfg.getInt("titaniumEndY", CATEGORY_WORLDGEN, titaniumEndY, 0, 256, "Top Y-level for Titanium generation");
		titaniumChance = cfg.getInt("titaniumChance", CATEGORY_WORLDGEN, titaniumChance, 0, 400, "Chance of titanium generation (per chunk)");
	}
}
