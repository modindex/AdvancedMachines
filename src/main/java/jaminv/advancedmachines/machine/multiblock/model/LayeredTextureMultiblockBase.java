package jaminv.advancedmachines.machine.multiblock.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jaminv.advancedmachines.client.RawTextures;
import jaminv.advancedmachines.client.quads.ILayeredTexture;
import jaminv.advancedmachines.client.textureset.TextureSets;
import jaminv.advancedmachines.init.property.Properties;
import jaminv.advancedmachines.init.property.UnlistedBoolean;
import jaminv.advancedmachines.machine.multiblock.MultiblockBorderType;
import jaminv.advancedmachines.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.objects.blocks.BlockMaterial;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;

public class LayeredTextureMultiblockBase implements ILayeredTexture {
	
	private String base;
	private IExtendedBlockState state;
	
	public String getBase() { return base; }
	public IExtendedBlockState getState() { return state; }

	public LayeredTextureMultiblockBase(IBlockState state, String base) {
		this.state = (IExtendedBlockState)state;
		this.base = base;		
	}
	
	protected void setBorder(List<TextureAtlasSprite> list, String variant, String side, MultiblockBorderType border) {
		if (border != MultiblockBorderType.NONE) {
			list.add(TextureSets.get("variant", variant, "borders", border.getName(), side));
		}
	}
	
	protected TextureAtlasSprite getBaseTexture(String variant) {
		return RawTextures.get(base, variant, "base");
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
