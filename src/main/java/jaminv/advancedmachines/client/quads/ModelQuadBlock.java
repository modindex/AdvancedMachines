package jaminv.advancedmachines.client.quads;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;

public class ModelQuadBlock extends ModelQuadBase {
	
	public static class Unit extends ModelQuadBlock {
		public Unit(VertexFormat format, EnumFacing facing, TextureAtlasSprite texture, TextureAtlasSprite face,
				TextureAtlasSprite top, TextureAtlasSprite bottom) {
			super(format, 0, 1, 0, 1, 0, 1, facing, texture, face, top, bottom);
		}
		public Unit(VertexFormat format, EnumFacing facing, TextureAtlasSprite texture, TextureAtlasSprite face) {
			super(format, 0, 1, 0, 1, 0, 1, facing, texture, face);
		}
		public Unit(VertexFormat format, TextureAtlasSprite texture) {
			super(format, 0, 1, 0, 1, 0, 1, texture);
		}
	}

	private EnumFacing facing;
	private TextureAtlasSprite texture, face, top, bottom;
	
	public ModelQuadBlock offset(float xoff, float yoff, float zoff) {
		this.xmin += xoff; this.xmax -= xoff;
		this.ymin += yoff; this.ymax -= yoff;
		this.zmin += zoff; this.zmax -= zoff;
		return this;
	}
	
	float xmin, xmax, ymin, ymax, zmin, zmax;
	
	public ModelQuadBlock(VertexFormat format, float xmin, float xmax, float ymin, float ymax, float zmin, float zmax,
			EnumFacing facing, TextureAtlasSprite texture, TextureAtlasSprite face, TextureAtlasSprite top, TextureAtlasSprite bottom) {
		super(format);
		this.facing = facing;
		this.texture = texture;
		this.face = face;
		this.top = top;
		this.bottom = bottom;

		this.xmin = xmin; this.xmax = xmax;
		this.ymin = ymin; this.ymax = ymax;
		this.zmin = zmin; this.zmax = zmax;	
	}
	
	public ModelQuadBlock(VertexFormat format, float xmin, float xmax, float ymin, float ymax, float zmin, float zmax,
			EnumFacing facing, TextureAtlasSprite texture, TextureAtlasSprite face) {
		this(format, xmin, xmax, ymin, ymax, zmin, zmax, facing, texture, face, texture, texture);
	}
	
	public ModelQuadBlock(VertexFormat format, float xmin, float xmax, float ymin, float ymax, float zmin, float zmax,
			TextureAtlasSprite texture) {
		this(format, xmin, xmax, ymin, ymax, zmin, zmax, EnumFacing.NORTH, texture, texture, texture, texture);
	}

	@Override
	public List<BakedQuad> getQuads() {
		List<BakedQuad> quads = new LinkedList<BakedQuad>();
		
		for (EnumFacing side : EnumFacing.values()) {
			TextureAtlasSprite sprite = texture;
			if (side == facing) { sprite = face; }
			if (side == EnumFacing.UP) { sprite = top; }
			if (side == EnumFacing.DOWN) { sprite = bottom; }
			
			quads.addAll((new ModelQuadBlockFace(getFormat(), xmin, xmax, ymin, ymax, zmin, zmax, sprite, side)).getQuads());
		}
		
		return quads;
	}

}
