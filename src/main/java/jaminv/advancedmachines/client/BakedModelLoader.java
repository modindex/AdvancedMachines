package jaminv.advancedmachines.client;

import jaminv.advancedmachines.util.Reference;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

public class BakedModelLoader implements ICustomModelLoader {
	
	public static final ModelMultiblock MODEL_MULTIBLOCK = new ModelMultiblock("expansion", "expansion");
	public static final ModelMultiblock MODEL_MULTIBLOCK_FURNACE = new ModelMultiblock("expansion", "furnace");
	public static final ModelMultiblock MODEL_MULTIBLOCK_ALLOY = new ModelMultiblock("expansion", "alloy");
	public static final ModelMultiblock MODEL_MULTIBLOCK_GRINDER = new ModelMultiblock("expansion", "grinder");
	public static final ModelMultiblock MODEL_MULTIBLOCK_PURIFIER = new ModelMultiblock("expansion", "purifier");
	public static final ModelMultiblock MODEL_MULTIBLOCK_INVENTORY = new ModelMultiblock("expansion", "inventory");
	public static final ModelMultiblock MODEL_MULTIBLOCK_REDSTONE = new ModelMultiblock("expansion", "redstone");
	public static final ModelMultiblock MODEL_MULTIBLOCK_ENERGY = new ModelMultiblock("expansion", "energy");
	public static final ModelMultiblock MODEL_MULTIBLOCK_TANK = new ModelMultiblock("expansion", "expansion");
	
	public static final ModelMultiblock MODEL_MULTIBLOCK_SPEED = new ModelMultiblock("speed");
	public static final ModelMultiblock MODEL_MULTIBLOCK_PRODUCTIVITY = new ModelMultiblock("productivity");

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {	}

	@Override
	public boolean accepts(ResourceLocation modelLocation) {
		return modelLocation.getResourceDomain().equals(Reference.MODID) && 
			modelLocation.getResourcePath().length() >= "bakedmodelmultiblock".length() &&
			("bakedmodelmultiblock".equals(modelLocation.getResourcePath().substring(0, "bakedmodelmultiblock".length())));
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception {
		switch (modelLocation.getResourcePath()) {
		case "bakedmodelmultiblock_furnace":
			return MODEL_MULTIBLOCK_FURNACE;
		case "bakedmodelmultiblock_alloy":
			return MODEL_MULTIBLOCK_ALLOY;
		case "bakedmodelmultiblock_grinder":
			return MODEL_MULTIBLOCK_GRINDER;
		case "bakedmodelmultiblock_purifier":
			return MODEL_MULTIBLOCK_PURIFIER;
		case "bakedmodelmultiblock_inventory":
			return MODEL_MULTIBLOCK_INVENTORY;
		case "bakedmodelmultiblock_redstone":
			return MODEL_MULTIBLOCK_REDSTONE;
		case "bakedmodelmultiblock_energy":
			return MODEL_MULTIBLOCK_ENERGY;
		case "bakedmodelmultiblock_speed":
			return MODEL_MULTIBLOCK_SPEED;
		case "bakedmodelmultiblock_productivity":
			return MODEL_MULTIBLOCK_PRODUCTIVITY;
		case "bakedmodelmultiblock_tank":
			return MODEL_MULTIBLOCK_TANK;
		default:
			return MODEL_MULTIBLOCK;
		}
	}

}