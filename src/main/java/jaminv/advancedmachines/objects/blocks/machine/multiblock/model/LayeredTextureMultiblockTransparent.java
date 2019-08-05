package jaminv.advancedmachines.objects.blocks.machine.multiblock.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jaminv.advancedmachines.objects.blocks.BlockMaterial;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.MultiblockBorderType;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.MultiblockBorders;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class LayeredTextureMultiblockTransparent extends LayeredTextureMultiblockBase {

	public LayeredTextureMultiblockTransparent(IBlockState state, String base) {
		super(state, base);
	}

	@Override
	public List<TextureAtlasSprite> getTextures(EnumFacing side) {
		MultiblockBorders borders = new MultiblockBorders(getState());

		if (borders.get(side) == MultiblockBorderType.SOLID) {
			return super.getTextures(side);
		} else {
			return Collections.emptyList();
		}
	}
}
