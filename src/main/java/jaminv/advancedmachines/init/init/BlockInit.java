package jaminv.advancedmachines.init.init;

import java.util.HashMap;
import java.util.Map;

import jaminv.advancedmachines.lib.util.blocks.BlockProperties;
import jaminv.advancedmachines.lib.util.registry.RegistryHelper;
import jaminv.advancedmachines.machine.expansion.BlockMachineProductivity;
import jaminv.advancedmachines.machine.expansion.BlockMachineSpeed;
import jaminv.advancedmachines.machine.expansion.TileMachineExpansion;
import jaminv.advancedmachines.machine.expansion.energy.BlockMachineEnergy;
import jaminv.advancedmachines.machine.expansion.energy.TileMachineEnergy;
import jaminv.advancedmachines.machine.expansion.inventory.BlockMachineInventory;
import jaminv.advancedmachines.machine.expansion.inventory.TileMachineInventory;
import jaminv.advancedmachines.machine.expansion.multiply.BlockMachineMultiply;
import jaminv.advancedmachines.machine.expansion.multiply.TileMachineMultiply;
import jaminv.advancedmachines.machine.expansion.redstone.BlockMachineRedstone;
import jaminv.advancedmachines.machine.expansion.redstone.TileMachineRedstone;
import jaminv.advancedmachines.machine.expansion.tank.BlockMachineTank;
import jaminv.advancedmachines.machine.expansion.tank.TileMachineTank;
import jaminv.advancedmachines.machine.instance.alloy.BlockMachineAlloy;
import jaminv.advancedmachines.machine.instance.alloy.TileMachineAlloy;
import jaminv.advancedmachines.machine.instance.furnace.BlockMachineFurnace;
import jaminv.advancedmachines.machine.instance.furnace.TileMachineFurnace;
import jaminv.advancedmachines.machine.instance.grinder.BlockMachineGrinder;
import jaminv.advancedmachines.machine.instance.grinder.TileMachineGrinder;
import jaminv.advancedmachines.machine.instance.injector.BlockMachineInjector;
import jaminv.advancedmachines.machine.instance.injector.TileMachineInjector;
import jaminv.advancedmachines.machine.instance.melter.BlockMachineMelter;
import jaminv.advancedmachines.machine.instance.melter.TileMachineMelter;
import jaminv.advancedmachines.machine.instance.purifier.BlockMachinePurifier;
import jaminv.advancedmachines.machine.instance.purifier.TileMachinePurifier;
import jaminv.advancedmachines.machine.instance.stabilizer.BlockMachineStabilizer;
import jaminv.advancedmachines.machine.instance.stabilizer.TileMachineStabilizer;
import jaminv.advancedmachines.objects.blocks.BlockPropertiesMod;
import jaminv.advancedmachines.objects.blocks.render.BlockLayeredBaked;
import jaminv.advancedmachines.objects.blocks.render.ModelBakeryProviderMachineFrame;
import jaminv.advancedmachines.objects.variant.Variant;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import jaminv.advancedmachines.objects.variant.VariantResource;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockInit {
	
	public static Map<VariantResource, Block> ORE = new HashMap<>();
	
	public static Map<VariantExpansion, BlockMachineFurnace> FURNACE = new HashMap<>();
	public static Map<VariantExpansion, BlockMachineGrinder> GRINDER = new HashMap<>();
	public static Map<VariantExpansion, BlockMachineAlloy> ALLOY = new HashMap<>();
	public static Map<VariantExpansion, BlockMachinePurifier> PURIFIER = new HashMap<>();
	public static Map<VariantExpansion, BlockMachineMelter> MELTER = new HashMap<>();
	public static Map<VariantExpansion, BlockMachineStabilizer> STABILIZER = new HashMap<>();
	public static Map<VariantExpansion, BlockMachineInjector> INJECTOR = new HashMap<>();
	
	protected static String getVariantName(String name, Variant variant) { return name + "_" + variant.getName(); }	

	public static void init() {
		for (VariantResource var : VariantResource.values()) {
			Block ore = new BlockProperties.Block(BlockProperties.ORE.withHarvestLevel("pickaxe", var.getHarvestLevel()));
			ORE.put(var, ore);
			RegistryHelper.addBlockWithItem(ore, getVariantName("ore", var));
		}
		
		for (VariantResource var : VariantResource.values()) {
			RegistryHelper.addBlockWithItem(new BlockProperties.Block(BlockProperties.STORAGE), getVariantName("storage", var));
		}
		
		for (VariantExpansion var : VariantExpansion.values()) {
			RegistryHelper.addBlockWithBakedModel(new BlockLayeredBaked(BlockPropertiesMod.MACHINE),
				new ModelBakeryProviderMachineFrame(var), getVariantName("machine_frame", var));
		}	
		
		for (VariantExpansion var : VariantExpansion.values()) {
			String name = var.getName();
			
			if (var != VariantExpansion.IMPROBABLE) {
				RegistryHelper.addBlockWithBakedModel(new BlockMachineMultiply(var), getVariantName("machine_expansion", var));
			}
			RegistryHelper.addBlockWithBakedModel(new BlockMachineSpeed(var), "machine_speed_" + name);
			RegistryHelper.addBlockWithBakedModel(new BlockMachineProductivity(var), "machine_productivity_" + name);
			
			if (var != VariantExpansion.IMPROBABLE) {
				RegistryHelper.addBlockWithBakedModel(new BlockMachineInventory(var), "machine_inventory_" + name);
				RegistryHelper.addBlockWithBakedModel(new BlockMachineEnergy(var), "machine_energy_" + name);
				RegistryHelper.addBlockWithBakedModel(new BlockMachineRedstone(var), "machine_redstone_" + name);
				RegistryHelper.addBlockWithBakedModel(new BlockMachineTank(var), "machine_tank_" + name);
				
				BlockMachineFurnace furnace = new BlockMachineFurnace(var); FURNACE.put(var, furnace);
				BlockMachineGrinder grinder = new BlockMachineGrinder(var); GRINDER.put(var, grinder);
				BlockMachineAlloy alloy = new BlockMachineAlloy(var); ALLOY.put(var, alloy);
				BlockMachinePurifier purifier = new BlockMachinePurifier(var); PURIFIER.put(var, purifier);
				BlockMachineMelter melter = new BlockMachineMelter(var); MELTER.put(var, melter);
				BlockMachineStabilizer stabilizer = new BlockMachineStabilizer(var); STABILIZER.put(var, stabilizer);
				BlockMachineInjector injector = new BlockMachineInjector(var); INJECTOR.put(var, injector);
				
				RegistryHelper.addBlockWithBakedModel(furnace, "machine_furnace_" + name);
				RegistryHelper.addBlockWithBakedModel(grinder, "machine_grinder_" + name);
				RegistryHelper.addBlockWithBakedModel(alloy, "machine_alloy_" + name);
				RegistryHelper.addBlockWithBakedModel(purifier, "machine_purifier_" + name);
				RegistryHelper.addBlockWithBakedModel(melter, "machine_melter_" + name);
				RegistryHelper.addBlockWithBakedModel(stabilizer, "machine_stabilizer_" + name);
				RegistryHelper.addBlockWithBakedModel(injector, "machine_injector_" + name);
			}
		}
		GameRegistry.registerTileEntity(TileMachineMultiply.class, new ResourceLocation("tile_machine_expansion"));
		GameRegistry.registerTileEntity(TileMachineExpansion.class, new ResourceLocation("tile_machine_expansion_type"));
		GameRegistry.registerTileEntity(TileMachineInventory.class, new ResourceLocation("tile_machine_inventory"));
		GameRegistry.registerTileEntity(TileMachineEnergy.class, new ResourceLocation("tile_machine_energy"));
		GameRegistry.registerTileEntity(TileMachineRedstone.class, new ResourceLocation("tile_machine_redstone"));
		GameRegistry.registerTileEntity(TileMachineTank.class, new ResourceLocation("tile_machine_tank"));
		
		GameRegistry.registerTileEntity(TileMachineFurnace.class, new ResourceLocation("tile_machine_furnace"));
		GameRegistry.registerTileEntity(TileMachineGrinder.class, new ResourceLocation("tile_machine_grinder"));
		GameRegistry.registerTileEntity(TileMachineAlloy.class, new ResourceLocation("tile_machine_alloy"));
		GameRegistry.registerTileEntity(TileMachinePurifier.class, new ResourceLocation("tile_machine_purifier"));
		GameRegistry.registerTileEntity(TileMachineMelter.class, new ResourceLocation("tile_machine_melter"));
		GameRegistry.registerTileEntity(TileMachineStabilizer.class, new ResourceLocation("tile_machine_stabilizer"));
		GameRegistry.registerTileEntity(TileMachineInjector.class, new ResourceLocation("tile_machine_injector"));		
	}
}
