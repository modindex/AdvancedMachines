package jaminv.advancedmachines.init;

import java.util.ArrayList;
import java.util.List;

import jaminv.advancedmachines.objects.blocks.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;

public class BlockInit {

	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	public static final Block BLOCK_TITANIUM = new BlockBase("block_titanium", Material.IRON);
}
