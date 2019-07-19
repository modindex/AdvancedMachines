package jaminv.advancedmachines.util;

import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Reference.MODID, name = "advancedmachines/advancedmachines")
@Config.LangKey("advmach.config.title")
public final class ModConfig {
	
	@Config.Comment("General Configuration")
	public static General general = new General();
	public static class General {
		@Config.Comment("Machines only run full updates every X ticks")
		@RangeInt(min = 1, max = 100)
		public int tickUpdate = 5;
		
		@Config.Comment("Processing time for machines with no upgrages")
		@RangeInt(min = 1)
		public int processTimeBasic = 100;
		
		@Config.Comment("Default energy for standard furnace recipes")
		@RangeInt(min = 0)
		public int defaultFurnaceEnergy = 1400;
		
		@Config.Comment("Default energy for standard grinder recipes")
		@RangeInt(min = 0)
		public int defaultGrinderEnergy = 2000;
		
		@Config.Comment("Default energy for standard purifier recipes")
		@RangeInt(min = 0)
		public int defaultPurifierEnergy = 30000;
		
		@Config.Comment("Default energy for standard alloy furnace recipes")
		@RangeInt(min = 0)
		public int defaultAlloyEnergy = 8000;
	}
	
	@Config.Comment({ "Material Configuration", "",
		"Disabling a material here will prevent creation of all blocks and items associated with the material.",
		"This mod will attempt to use the ore dictionary for any recipes that include these materials;",
		"If no equivilent material is found, the recipe will not be available.",
		"This may make some items unavailable if a material is removed and these is no ore dictionary equivalent.",
		"This is intended to be used primarily when another mod provides the same material and you don't want duplicates."		
	})
	@Config.RequiresMcRestart
	public static Material material = new Material();
	public static class Material {
		@Config.Comment({
			"Check for the existence of Thermal Foundation and remove all materials that coincide with it.", "NOT YET WORKING."
		})
		public boolean detectAndRemoveTF = true;
		
		@Config.Comment({
			"Check for the existence of Applied Energestics 2 and remove all materials that coincide with it (basically just ender dust).", "NOT YET WORKING."
		})
		public boolean detectAndRemoveAE2 = true;

		@Config.Comment({ "List of mod materials to exclude", "Valid options are 'titanium', 'copper', 'silver'", "This will also disable world generation for these ores." })
		public String[] excludeMaterials = {};
		
		@Config.Comment({ "List of pure ingots and dusts to exclude.", "Valid options are 'gold', 'copper', 'silver', 'diamond', 'ender'." })
		public String[] excludePure = {};
		
		@Config.Comment({ "List of dusts to exclude.", "Valid options are 'coal', 'iron', 'gold', 'copper', 'silver', 'diamond', 'ender'." })
		public String[] excludeDust = {};
		
		@Config.Comment({ "List of alloys to exclude.", "Valid options are 'titanium_carbide', 'titanium_endite'." })
		public String[] excludeAlloy = {};
		
		public static boolean doInclude(String material) {
			return true;
		}
		
/*		protected static Map<String, Boolean> exclude = new HashMap<String, Boolean>;
		public static boolean doInclude(String material) {			
			if (exclude.isEmpty()) {
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
		} */
		
	}
	
	@Config.Comment({ "Recipe Configuration" })
	public static Recipe recipe = new Recipe();
	public static class Recipe {
		// TODO: ModTweaker support
		
		@Config.Comment("Enable to show recipe ids in JEI. This can be used to find ids for disabling recipes.")
		public boolean showRecipeIds = false;
		
		@Config.Comment({ 
			"Scan the ore dictionary for all ore/dust/gem/ingot items and create appropriate grinder recipes for them.",
			"This helps provide the widest cross-mod capabilities even for unknown mods."
		})
		public boolean scanGrinderOre = true;
		
		@Config.Comment("Exclude specific grinder recipes by recipe id.")
		public String[] excludeGrinderRecipes = {};
		
		/*
		for (String item : excludeGrinderRecipes) {
			doExcludeGrinder.put(item, true);
		}*/			
	}
	
	@Config.Comment("Tool Configuration")
	@Config.RequiresMcRestart
	public static Tool tool = new Tool();
	public static class Tool {
		@Config.Comment("Set to false to disable Titanium tools")
		public boolean doAddTitaniumTools = true;
		
		@Config.Comment("Set to false to disable Titanium armor")
		public boolean doAddTitaniumArmor = true;
		
		@Config.Comment("Set to false to disable Copper tools")
		public boolean doAddCopperTools = true;
		
		@Config.Comment("Set to false to disable Copper armor")
		public boolean doAddCopperArmor = true;
	}
	
	@Config.Comment({ "World Gen Configution", "",
		"Disabling ore generation for a material may prevent certain aspects of the mod from working",
		"unless another mod provides ore generation for an ore dictionary equivalent to that material."		
	})
	public static WorldGen worldgen = new WorldGen();
	public static class WorldGen {
		@Config.Comment("Set to false to prevent Titanium Ore generation in the world")
		public boolean doGenerateTitanium = true;
				
		@Config.Comment("Maximum number of Titanium Ore in a vein")
		@RangeInt(min = 0, max = 64)
		public int titaniumVeinSize = 4;
		
		@Config.Comment("Bottom Y-level for Titanium Ore generation")
		@RangeInt(min = 0, max = 256)
		public int titaniumMinHeight = 0;
		
		@Config.Comment("Top Y-level for Titanium Ore generation")
		public int titaniumMaxHeight = 24;
		
		@Config.Comment("Number of times per chunk that it will attempt to generate Titanium Ore")
		public int titaniumChance = 2; 
	}
	
	@Mod.EventBusSubscriber(modid = Reference.MODID)
	private static class EventHandler {
		
		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(Reference.MODID)) {
				ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
			}
		}
	}	
	
/*	
	private static void initDependentConfigs() {
		if (!doInclude("material_titanium")) {
			doGenerateTitanium = false;
		}
		if (!doInclude("material_copper")) {
			doGenerateCopper = false;
		}
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
	} */
}
