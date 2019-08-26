package jaminv.advancedmachines.machine.multiblock.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jaminv.advancedmachines.client.RawTextures;
import jaminv.advancedmachines.client.textureset.TextureSets;
import jaminv.advancedmachines.init.property.Properties;
import jaminv.advancedmachines.init.property.UnlistedBoolean;
import jaminv.advancedmachines.lib.render.quad.LayeredTexture;
import jaminv.advancedmachines.machine.multiblock.MultiblockBorderType;
import jaminv.advancedmachines.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.machine.multiblock.face.SidedTexture;
import jaminv.advancedmachines.objects.blocks.BlockMaterial;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;

public class LayeredTextureMultiblock implements LayeredTexture {
	
	private String texture, base;
	SidedTexture blockSide;
	private IExtendedBlockState state;
	
	public String getTexture() { return texture; }
	public String getBase() { return base; }
	public IExtendedBlockState getState() { return state; }

	/**
	 * 
	 * @param state Block state
	 * @param texture Main texture
	 * @param base Base texture - used for borders. Often the same as `texture`, but not always. 
	 * @param blockSide Currently accepted values are "top" and "side"
	 */
	public LayeredTextureMultiblock(IBlockState state, String texture) {
		this.state = (IExtendedBlockState)state;
		this.texture = texture;
		this.base = texture;
		this.blockSide = SidedTexture.SIDE;
	}
	
	public LayeredTextureMultiblock withBase(String base) { this.base = base; return this; }
	public LayeredTextureMultiblock withSided(SidedTexture blockSide) { this.blockSide = blockSide; return this; }
	
	protected void setBorder(List<TextureAtlasSprite> list, String variant, String side, MultiblockBorderType border) {
		if (border != MultiblockBorderType.NONE) {
			list.add(TextureSets.get(base, variant, blockSide.getName(), "borders", border.getName(), side));
		}
	}
	
	protected TextureAtlasSprite getBaseTexture(String variant) {
		return TextureSets.get(texture, variant, blockSide.getName(), "base");
	}

	@Override
	public List<TextureAtlasSprite> getTextures(EnumFacing side) {
		
		LinkedList<TextureAtlasSprite> textures = new LinkedList<TextureAtlasSprite>();
		
		String variant = state.getValue(BlockMaterial.EXPANSION_VARIANT).getName();
		textures.add(getBaseTexture(variant));
		
		MultiblockBorders borders = new MultiblockBorders(state);
		
		switch (side) {
		case NORTH:
			setBorder(textures, variant, "top", borders.getTop());
			setBorder(textures, variant, "bottom", borders.getBottom());
			setBorder(textures, variant, "left", borders.getEast());
			setBorder(textures, variant, "right", borders.getWest());
			break;
		case SOUTH:
			setBorder(textures, variant, "top", borders.getTop());
			setBorder(textures, variant, "bottom", borders.getBottom());
			setBorder(textures, variant, "left", borders.getWest());
			setBorder(textures, variant, "right", borders.getEast());
			break;
		case EAST:
			setBorder(textures, variant, "top", borders.getTop());
			setBorder(textures, variant, "bottom", borders.getBottom());
			setBorder(textures, variant, "left", borders.getSouth());
			setBorder(textures, variant, "right", borders.getNorth());
			break;
		case WEST:
			setBorder(textures, variant, "top", borders.getTop());
			setBorder(textures, variant, "bottom", borders.getBottom());
			setBorder(textures, variant, "left", borders.getNorth());
			setBorder(textures, variant, "right", borders.getSouth());
			break;
		case UP:
			setBorder(textures, variant, "top", borders.getEast());
			setBorder(textures, variant, "bottom", borders.getWest());
			setBorder(textures, variant, "left", borders.getNorth());
			setBorder(textures, variant, "right", borders.getSouth());
			break;
		case DOWN:
			setBorder(textures, variant, "top", borders.getWest());
			setBorder(textures, variant, "bottom", borders.getEast());
			setBorder(textures, variant, "left", borders.getNorth());
			setBorder(textures, variant, "right", borders.getSouth());
			break;
		default:
			break;
		}

		return textures;
	}

}
