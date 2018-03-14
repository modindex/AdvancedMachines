package jaminv.advancedmachines.init;

import java.util.ArrayList;
import java.util.List;

import jaminv.advancedmachines.objects.blocks.BlockMaterial;
import jaminv.advancedmachines.objects.blocks.BlockMaterialOre;
import jaminv.advancedmachines.objects.blocks.DataBlock;
import jaminv.advancedmachines.objects.blocks.DirectionalBlock;
import jaminv.advancedmachines.objects.blocks.machine.alloy.BlockMachineAlloy;
import jaminv.advancedmachines.objects.blocks.machine.expansion.BlockMachineExpansion;
import jaminv.advancedmachines.objects.blocks.machine.expansion.energy.BlockMachineEnergy;
import jaminv.advancedmachines.objects.blocks.machine.expansion.inventory.BlockMachineInventory;
import jaminv.advancedmachines.objects.blocks.machine.purifier.BlockMachinePurifier;
import jaminv.advancedmachines.util.material.MaterialBase;
import jaminv.advancedmachines.util.material.MaterialExpansion;
import jaminv.advancedmachines.util.material.MaterialMod;
import jaminv.advancedmachines.util.material.PropertyMaterial;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

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
	
	public static final BlockMachinePurifier MACHINE_PURIFIER = new BlockMachinePurifier("machine_purifier");
	public static final BlockMachineAlloy MACHINE_ALLOY = new BlockMachineAlloy("machine_alloy");
	
	public static final BlockMachineExpansion MACHINE_EXPANSION = new BlockMachineExpansion("machine_expansion");
	public static final BlockMachineInventory MACHINE_INVENTORY = new BlockMachineInventory("machine_inventory");
	public static final BlockMachineEnergy MACHINE_ENERGY = new BlockMachineEnergy("machine_energy");
	
	public static final DataBlock DATABLOCK = new DataBlock();
}
