package jaminv.advancedmachines.objects.blocks.render;

import jaminv.advancedmachines.lib.util.blocks.BlockProperties;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLayeredBaked extends Block {
	
	public BlockLayeredBaked(BlockProperties props) {
		super(props.getMaterial());
		this.setSoundType(props.getSoundType());
		props.apply(this);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.TRANSLUCENT;
	}

}
