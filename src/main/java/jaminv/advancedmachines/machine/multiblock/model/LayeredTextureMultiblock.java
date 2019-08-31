package jaminv.advancedmachines.machine.multiblock.model;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nullable;

import jaminv.advancedmachines.lib.render.quad.LayeredTexture;
import jaminv.advancedmachines.lib.render.quad.Texture;
import jaminv.advancedmachines.machine.multiblock.MultiblockBorderType;
import jaminv.advancedmachines.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.objects.blocks.Properties;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;

public class LayeredTextureMultiblock implements LayeredTexture {
	
	protected VariantExpansion variant;
	protected MultiblockBorders borders;
	protected MultiblockTextureBase base;
	protected Texture face = null;
	protected TextureSide side = TextureSide.SIDE;
	
	public LayeredTextureMultiblock(IBlockState state, MultiblockTextureBase base) {
		variant = ((IExtendedBlockState)state).getValue(Properties.EXPANSION_VARIANT);
		borders = new MultiblockBorders((IExtendedBlockState)state);
		this.base = base;
	}
	
	private LayeredTextureMultiblock(VariantExpansion variant, MultiblockBorders borders, 
			MultiblockTextureBase base, Texture face, TextureSide side) {
		this.variant = variant;
		this.borders = borders;
		this.base = base;
		this.face = face;
		this.side = side;
	}
	
	public LayeredTextureMultiblock copy() { return new LayeredTextureMultiblock(variant, borders, base, face, side); }
	
	public LayeredTextureMultiblock withFace(@Nullable Texture face) { this.face = face; return this; }
	public LayeredTextureMultiblock withSided(TextureSide side) { this.side = side; return this; }
	
	protected void setBorder(List<Texture> list, VariantExpansion variant, MultiblockBorderType border, String edge) {
		Texture texture = base.getBorder(variant, side, border, edge);
		if (texture != null) { list.add(texture); }
	}

	@Override
	public List<Texture> getTextures(EnumFacing facing) {
		LinkedList<Texture> textures = new LinkedList<Texture>();
		textures.add(base.getTexture(variant, side));
		if (face != null) { textures.add(face); }
		
		switch (facing) {
		case NORTH:
			setBorder(textures, variant, borders.getTop(), "top");
			setBorder(textures, variant, borders.getBottom(), "bottom");
			setBorder(textures, variant, borders.getEast(), "left");
			setBorder(textures, variant, borders.getWest(), "right");
			break;
		case SOUTH:
			setBorder(textures, variant, borders.getTop(), "top");
			setBorder(textures, variant, borders.getBottom(), "bottom");
			setBorder(textures, variant, borders.getWest(), "left");
			setBorder(textures, variant, borders.getEast(), "right");
			break;
		case EAST:
			setBorder(textures, variant, borders.getTop(), "top");
			setBorder(textures, variant, borders.getBottom(), "bottom");
			setBorder(textures, variant, borders.getSouth(), "left");
			setBorder(textures, variant, borders.getNorth(), "right");
			break;
		case WEST:
			setBorder(textures, variant, borders.getTop(), "top");
			setBorder(textures, variant, borders.getBottom(), "bottom");
			setBorder(textures, variant, borders.getNorth(), "left");
			setBorder(textures, variant, borders.getSouth(), "right");
			break;
		case UP:
			setBorder(textures, variant, borders.getEast(), "top");
			setBorder(textures, variant, borders.getWest(), "bottom");
			setBorder(textures, variant, borders.getNorth(), "left");
			setBorder(textures, variant, borders.getSouth(), "right");
			break;
		case DOWN:
			setBorder(textures, variant, borders.getWest(), "top");
			setBorder(textures, variant, borders.getEast(), "bottom");
			setBorder(textures, variant, borders.getNorth(), "left");
			setBorder(textures, variant, borders.getSouth(), "right");
			break;
		default:
			break;
		}

		return textures;
	}
}
