package jaminv.advancedmachines.init;

import java.util.ArrayList;
import java.util.List;

import jaminv.advancedmachines.client.BakedModelMultiblock;
import jaminv.advancedmachines.objects.blocks.BlockMaterial;
import jaminv.advancedmachines.objects.blocks.BlockMaterialOre;
import jaminv.advancedmachines.objects.blocks.DirectionalBlock;
import jaminv.advancedmachines.objects.blocks.machine.expansion.BlockMachineExpansion;
import jaminv.advancedmachines.objects.blocks.machine.expansion.energy.BlockMachineEnergy;
import jaminv.advancedmachines.objects.blocks.machine.expansion.inventory.BlockMachineInventory;
import jaminv.advancedmachines.objects.blocks.machine.expansion.prodctivity.BlockMachineProductivity;
import jaminv.advancedmachines.objects.blocks.machine.expansion.redstone.BlockMachineRedstone;
import jaminv.advancedmachines.objects.blocks.machine.expansion.speed.BlockMachineSpeed;
import jaminv.advancedmachines.objects.blocks.machine.instance.alloy.BlockMachineAlloy;
import jaminv.advancedmachines.objects.blocks.machine.instance.furnace.BlockMachineFurnace;
import jaminv.advancedmachines.objects.blocks.machine.instance.grinder.BlockMachineGrinder;
import jaminv.advancedmachines.objects.blocks.machine.instance.purifier.BlockMachinePurifier;
import jaminv.advancedmachines.util.material.MaterialBase;
import jaminv.advancedmachines.util.material.MaterialExpansion;
import jaminv.advancedmachines.util.material.MaterialMod;
import jaminv.advancedmachines.util.material.PropertyMaterial;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockInit {
	// Force these objects to load before referencing them
	private static MaterialMod MOD_BLOCK = MaterialMod.TITANIUM;
	private static MaterialExpansion EXPANSION_BLOCK = MaterialExpansion.BASIC;
	
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	public static final BlockMaterial STORAGE = new BlockMaterial("storage", MaterialBase.MaterialType.MOD, "block", Material.IRON, 5.0f) {
		protected PropertyMaterial getVariant() { return PropertyMaterial.create("variant", MaterialBase.MaterialType.MOD); }
	};
	public static final BlockMaterialOre ORE = new BlockMaterialOre("ore", MaterialBase.MaterialType.MOD, Material.ROCK, 3.0f) {
		protected PropertyMaterial getVariant() { return PropertyMaterial.create("variant", MaterialBase.MaterialType.MOD); }
	};
	
	public static final DirectionalBlock BLOCK_MACHINE = new DirectionalBlock("machine", Material.IRON);
	
	public static final BlockMachineFurnace MACHINE_FURNACE = new BlockMachineFurnace("machine_furnace");
	public static final BlockMachineGrinder MACHINE_GRINDER = new BlockMachineGrinder("machine_grinder");
	public static final BlockMachinePurifier MACHINE_PURIFIER = new BlockMachinePurifier("machine_purifier");
	public static final BlockMachineAlloy MACHINE_ALLOY = new BlockMachineAlloy("machine_alloy");
	
	public static final BlockMachineExpansion MACHINE_EXPANSION = new BlockMachineExpansion("machine_expansion");
	public static final BlockMachineSpeed MACHINE_SPEED = new BlockMachineSpeed("machine_speed");
	public static final BlockMachineProductivity MACHINE_PRODUCTIVITY = new BlockMachineProductivity("machine_productivity");
	public static final BlockMachineInventory MACHINE_INVENTORY = new BlockMachineInventory("machine_inventory");
	public static final BlockMachineEnergy MACHINE_ENERGY = new BlockMachineEnergy("machine_energy");
	public static final BlockMachineRedstone MACHINE_REDSTONE = new BlockMachineRedstone("machine_redstone");
}
