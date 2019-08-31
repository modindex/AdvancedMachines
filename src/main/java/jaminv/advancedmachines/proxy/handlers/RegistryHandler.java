package jaminv.advancedmachines.proxy.handlers;

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
import jaminv.advancedmachines.objects.variant.VariantAlloy;
import jaminv.advancedmachines.objects.variant.VariantCircuit;
import jaminv.advancedmachines.objects.variant.VariantDust;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import jaminv.advancedmachines.objects.variant.VariantGear;
import jaminv.advancedmachines.objects.variant.VariantIngredient;
import jaminv.advancedmachines.objects.variant.VariantPure;
import jaminv.advancedmachines.objects.variant.VariantResource;
import jaminv.advancedmachines.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@EventBusSubscriber(modid = Reference.MODID)
public class RegistryHandler {
	
	public static Map<String, BlockMachineFurnace> FURNACE = new HashMap<>();
	public static Map<String, BlockMachineGrinder> GRINDER = new HashMap<>();
	public static Map<String, BlockMachineAlloy> ALLOY = new HashMap<>();
	public static Map<String, BlockMachinePurifier> PURIFIER = new HashMap<>();
	public static Map<String, BlockMachineMelter> MELTER = new HashMap<>();
	public static Map<String, BlockMachineStabilizer> STABILIZER = new HashMap<>();
	public static Map<String, BlockMachineInjector> INJECTOR = new HashMap<>();
	
	@FunctionalInterface
	protected static interface VariantIterator<T extends Variant> {
		void iterate(T var);
	}
	
	protected static <T extends Variant> void forVariant(T[] values, VariantIterator<T> iterator) {
		for (T var : values) { iterator.iterate(var); }
	}
	
	protected static String getVariantName(String name, Variant variant) { return name + "_" + variant.getName(); }

	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {
		for (VariantResource var : VariantResource.values()) {
			RegistryHelper.addItem(new Item().setCreativeTab(CreativeTabs.MATERIALS), getVariantName("ingot", var));
		}
		for (VariantPure var : VariantPure.values()) {
			if (var == VariantPure.DIAMOND) { continue; }
			RegistryHelper.addItem(new Item().setCreativeTab(CreativeTabs.MATERIALS), getVariantName("ingot_pure", var));
		}
		for (VariantAlloy var : VariantAlloy.values()) {
			RegistryHelper.addItem(new Item().setCreativeTab(CreativeTabs.MATERIALS), getVariantName("alloy", var));
		}

		for (VariantDust var : VariantDust.values()) {
			RegistryHelper.addItem(new Item().setCreativeTab(CreativeTabs.MATERIALS), getVariantName("dust", var));
		}
		for (VariantPure var : VariantPure.values()) {
			RegistryHelper.addItem(new Item().setCreativeTab(CreativeTabs.MATERIALS), getVariantName("dust_pure", var));
		}
		for (VariantAlloy var : VariantAlloy.values()) {
			//RegistryHelper.addItem(new Item().setCreativeTab(CreativeTabs.MATERIALS), getVariantName("alloy_dust", var));
		}
		
		for (VariantGear var : VariantGear.values()) {
			RegistryHelper.addItem(new Item().setCreativeTab(CreativeTabs.MATERIALS), getVariantName("gear", var));
		}
		for (VariantCircuit var : VariantCircuit.values()) {
			RegistryHelper.addItem(new Item().setCreativeTab(CreativeTabs.MATERIALS), getVariantName("circuit", var));
		}
		for (VariantIngredient var : VariantIngredient.values()) {
			RegistryHelper.addItem(new Item().setCreativeTab(CreativeTabs.MATERIALS), var.getName());
		}
		
		RegistryHelper.registerItems(event);
	}
	
	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event) {
		forVariant(VariantResource.values(), var -> {
			BlockProperties props = BlockProperties.ORE.withHarvestLevel("pickaxe", var.getHarvestLevel());
			RegistryHelper.addBlockWithItem(new BlockProperties.Block(props), getVariantName("ore", var));
		});
		
		forVariant(VariantResource.values(), var -> {
			RegistryHelper.addBlockWithItem(new BlockProperties.Block(BlockProperties.STORAGE), getVariantName("storage", var));
		});
		
		forVariant(VariantExpansion.values(), var -> {
			RegistryHelper.addBlockWithBakedModel(new BlockLayeredBaked(BlockPropertiesMod.MACHINE),
				new ModelBakeryProviderMachineFrame(var), getVariantName("machine_frame", var));
		});	
		
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
				
				FURNACE.put(name, new BlockMachineFurnace(var));
				GRINDER.put(name, new BlockMachineGrinder(var));
				ALLOY.put(name, new BlockMachineAlloy(var));
				PURIFIER.put(name, new BlockMachinePurifier(var));
				MELTER.put(name, new BlockMachineMelter(var));
				STABILIZER.put(name, new BlockMachineStabilizer(var));
				INJECTOR.put(name, new BlockMachineInjector(var));
				
				RegistryHelper.addBlockWithBakedModel(FURNACE.get(name), "machine_furnace_" + name);
				RegistryHelper.addBlockWithBakedModel(GRINDER.get(name), "machine_grinder_" + name);
				RegistryHelper.addBlockWithBakedModel(ALLOY.get(name), "machine_alloy_" + name);
				RegistryHelper.addBlockWithBakedModel(PURIFIER.get(name), "machine_purifier_" + name);
				RegistryHelper.addBlockWithBakedModel(MELTER.get(name), "machine_melter_" + name);
				RegistryHelper.addBlockWithBakedModel(STABILIZER.get(name), "machine_stabilizer_" + name);
				RegistryHelper.addBlockWithBakedModel(INJECTOR.get(name), "machine_injector_" + name);
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
		
		RegistryHelper.addFluid(new Fluid("coal_tar",
				new ResourceLocation(Reference.MODID, "fluids/coal_tar_still"),
				new ResourceLocation(Reference.MODID, "fluids/coal_tar_flow")
			).setDensity(1300).setGaseous(false).setViscosity(30000).setTemperature(290),
			"coal_tar", new MaterialLiquid(MapColor.BLACK));
		
		RegistryHelper.addFluid(new Fluid("tree_resin",
				new ResourceLocation(Reference.MODID, "fluids/resin_still"),
				new ResourceLocation(Reference.MODID, "fluids/resin_flow")
			).setDensity(2000).setGaseous(false).setViscosity(20000).setTemperature(300),
			"tree_resin", new MaterialLiquid(MapColor.BROWN));
		
		RegistryHelper.registerBlocks(event);
	}
}
