package jaminv.advancedmachines.machine.multiblock.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jaminv.advancedmachines.machine.multiblock.MultiblockBorderType;
import jaminv.advancedmachines.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.objects.blocks.BlockMaterial;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class LayeredTextureMultiblockTransparent extends LayeredTextureMultiblock {

	public LayeredTextureMultiblockTransparent(IBlockState state, MultiblockTextureBase base) {
		super(state, base);
	}

	@Override
	public List<TextureAtlasSprite> getTextures(EnumFacing side) {
		if (borders.get(side) == MultiblockBorderType.SOLID) {
			return super.getTextures(side);
		} else {
			return Collections.emptyList();
		}
	}
}
