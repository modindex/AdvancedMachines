package jaminv.advancedmachines.init;

import java.util.ArrayList;
import java.util.List;

import jaminv.advancedmachines.objects.blocks.BlockMaterial;
import jaminv.advancedmachines.objects.blocks.BlockMaterialOre;
import jaminv.advancedmachines.objects.blocks.DataBlock;
import jaminv.advancedmachines.objects.blocks.DirectionalBlock;
import jaminv.advancedmachines.objects.blocks.machine.alloy.BlockMachineAlloy;
import jaminv.advancedmachines.objects.blocks.machine.purifier.BlockMachinePurifier;
import jaminv.advancedmachines.objects.items.material.MaterialBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockInit {

	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	public static final BlockMaterial STORAGE = new BlockMaterial("storage", MaterialBase.MaterialType.MOD, "block", Material.IRON, 5.0f);
	//public static final BlockMaterialOre ORE = new BlockMaterialOre("ore", MaterialBase.MaterialType.MOD, Material.ROCK, 3.0f);
	
	public static final DirectionalBlock BLOCK_MACHINE = new DirectionalBlock("machine", Material.IRON);
	
	public static final BlockMachinePurifier MACHINE_PURIFIER = new BlockMachinePurifier("machine_purifier");
	public static final BlockMachineAlloy MACHINE_ALLOY = new BlockMachineAlloy("machine_alloy");
	
	public static final DataBlock DATABLOCK = new DataBlock();
}
