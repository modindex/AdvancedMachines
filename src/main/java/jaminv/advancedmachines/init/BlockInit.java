package jaminv.advancedmachines.init;

import java.util.ArrayList;
import java.util.List;

import jaminv.advancedmachines.init.property.PropertyMaterial;
import jaminv.advancedmachines.machine.expansion.BlockMachineProductivity;
import jaminv.advancedmachines.machine.expansion.BlockMachineSpeed;
import jaminv.advancedmachines.machine.expansion.energy.BlockMachineEnergy;
import jaminv.advancedmachines.machine.expansion.inventory.BlockMachineInventory;
import jaminv.advancedmachines.machine.expansion.multiply.BlockMachineMultiply;
import jaminv.advancedmachines.machine.expansion.redstone.BlockMachineRedstone;
import jaminv.advancedmachines.machine.expansion.tank.BlockMachineTank;
import jaminv.advancedmachines.machine.instance.alloy.BlockMachineAlloy;
import jaminv.advancedmachines.machine.instance.furnace.BlockMachineFurnace;
import jaminv.advancedmachines.machine.instance.grinder.BlockMachineGrinder;
import jaminv.advancedmachines.machine.instance.injector.BlockMachineInjector;
import jaminv.advancedmachines.machine.instance.melter.BlockMachineMelter;
import jaminv.advancedmachines.machine.instance.purifier.BlockMachinePurifier;
import jaminv.advancedmachines.machine.instance.stabilizer.BlockMachineStabilizer;
import jaminv.advancedmachines.objects.blocks.BlockMaterial;
import jaminv.advancedmachines.objects.blocks.BlockMaterialOre;
import jaminv.advancedmachines.objects.fluids.BlockFluidClassicBase;
import jaminv.advancedmachines.objects.variant.MaterialBase;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import jaminv.advancedmachines.objects.variant.MaterialMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;

public class BlockInit {
	// Force these objects to load before referencing them
	private static MaterialMod MOD_BLOCK = MaterialMod.TITANIUM;
	private static VariantExpansion EXPANSION_BLOCK = VariantExpansion.BASIC;
	
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	public static final BlockMaterial STORAGE = new BlockMaterial("storage", MaterialBase.MaterialType.MOD, "block", Material.IRON, 5.0f) {
		protected PropertyMaterial getVariant() { return PropertyMaterial.create("variant", MaterialBase.MaterialType.MOD); }
	};
	public static final BlockMaterialOre ORE = new BlockMaterialOre("ore", MaterialBase.MaterialType.MOD, Material.ROCK, 3.0f) {
		protected PropertyMaterial getVariant() { return PropertyMaterial.create("variant", MaterialBase.MaterialType.MOD); }
	};
	
	public static final BlockMaterial BLOCK_MATERIAL = new BlockMaterial("machine_frame", MaterialBase.MaterialType.EXPANSION, Material.IRON, 5.0f) {
		protected PropertyMaterial getVariant() { return PropertyMaterial.create("variant", MaterialBase.MaterialType.EXPANSION); }
	};
	
	public static final BlockMachineFurnace MACHINE_FURNACE = new BlockMachineFurnace("machine_furnace");
	public static final BlockMachineGrinder MACHINE_GRINDER = new BlockMachineGrinder("machine_grinder");
	public static final BlockMachinePurifier MACHINE_PURIFIER = new BlockMachinePurifier("machine_purifier");
	public static final BlockMachineAlloy MACHINE_ALLOY = new BlockMachineAlloy("machine_alloy");
	public static final BlockMachineMelter MACHINE_MELTER = new BlockMachineMelter("machine_melter");
	public static final BlockMachineStabilizer MACHINE_STABILIZER = new BlockMachineStabilizer("machine_stabilizer");
	public static final BlockMachineInjector MACHINE_INJECTOR = new BlockMachineInjector("machine_injector");
	
	public static final BlockMachineMultiply MACHINE_EXPANSION = new BlockMachineMultiply("machine_expansion");
	public static final BlockMachineSpeed MACHINE_SPEED = new BlockMachineSpeed("machine_speed");
	public static final BlockMachineProductivity MACHINE_PRODUCTIVITY = new BlockMachineProductivity("machine_productivity");
	public static final BlockMachineInventory MACHINE_INVENTORY = new BlockMachineInventory("machine_inventory");
	public static final BlockMachineEnergy MACHINE_ENERGY = new BlockMachineEnergy("machine_energy");
	public static final BlockMachineRedstone MACHINE_REDSTONE = new BlockMachineRedstone("machine_redstone");
	public static final BlockMachineTank MACHINE_TANK = new BlockMachineTank("machine_tank");
	
	public static final BlockFluidClassicBase FLUID_COAL_TAR = new BlockFluidClassicBase("coal_tar", FluidInit.COAL_TAR, new MaterialLiquid(MapColor.BLACK));
	public static final BlockFluidClassicBase FLUID_RESIN = new BlockFluidClassicBase("resin", FluidInit.RESIN, new MaterialLiquid(MapColor.BROWN));
}
