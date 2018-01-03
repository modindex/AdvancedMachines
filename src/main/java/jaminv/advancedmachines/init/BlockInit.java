package jaminv.advancedmachines.init;

import java.util.ArrayList;
import java.util.List;

import jaminv.advancedmachines.objects.blocks.BlockMaterial;
import jaminv.advancedmachines.objects.blocks.DataBlock;
import jaminv.advancedmachines.objects.blocks.DirectionalBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockInit {

	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	public static final Block BLOCK_TITANIUM = new BlockMaterial("storage", "block", Material.IRON);
	public static final Block ORE = new BlockMaterial("ore", Material.ROCK);
	
	public static final DirectionalBlock BLOCK_MACHINE = new DirectionalBlock("machine", Material.IRON);
	
	public static final DataBlock DATABLOCK = new DataBlock();
}
