package jaminv.advancedmachines.client.quads;

import java.util.Collections;
import java.util.List;

import jaminv.advancedmachines.client.BakedModelBase;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

public class ModelQuadBlockFace extends ModelQuadBase {

	TextureAtlasSprite texture;
	EnumFacing side;
	boolean inverted;
	
	float xmin, xmax, ymin, ymax, zmin, zmax;
	
	public static class Unit extends ModelQuadBlockFace {
		public Unit(VertexFormat format, TextureAtlasSprite texture, EnumFacing side, boolean inverted) {
			super(format, 0, 1, 0, 1, 0, 1, texture, side, inverted);
		}		
		
		public Unit(VertexFormat format, TextureAtlasSprite texture, EnumFacing side) {
			super(format, 0, 1, 0, 1, 0, 1, texture, side);
		}
	}
	
	public ModelQuadBlockFace offset(float xoff, float yoff, float zoff) {
		this.xmin += xoff; this.xmax -= xoff;
		this.ymin += yoff; this.ymax -= yoff;
		this.zmin += zoff; this.zmax -= zoff;
		return this;
	}
	
	public ModelQuadBlockFace offset(float xminOff, float xmaxOff, float yminOff, float ymaxOff, float zminOff, float zmaxOff) {
		this.xmin += xminOff; this.xmax -= xmaxOff;
		this.ymin += yminOff; this.ymax -= ymaxOff;
		this.zmin += zminOff; this.zmax -= zmaxOff;
		return this;
	}
	
	public ModelQuadBlockFace(VertexFormat format, float xmin, float xmax, float ymin, float ymax, float zmin, float zmax,
			TextureAtlasSprite texture, EnumFacing side, boolean inverted) {
		super(format);
		this.texture = texture;
		this.side = side;
		this.inverted = inverted;
		
		this.xmin = xmin; this.xmax = xmax;
		this.ymin = ymin; this.ymax = ymax;
		this.zmin = zmin; this.zmax = zmax;
	}
	
	public ModelQuadBlockFace(VertexFormat format, float xmin, float xmax, float ymin, float ymax, float zmin, float zmax, 
			TextureAtlasSprite texture, EnumFacing side) {
		this(format, xmin, xmax, ymin, ymax, zmin, zmax, texture, side, false);
	}

	@Override
	public List<BakedQuad> getQuads() {
		BakedQuad quad;
		
		int xm = Math.round(xmin * 16), xx = Math.round(xmax * 16);
		int ym = Math.round(ymin * 16), yx = Math.round(ymax * 16);
		int zm = Math.round(zmin * 16), zx = Math.round(zmax * 16);
		
		switch (side) {
		
		/*
		 * The UV coordinates may or may not be correct. They have worked so far for my very limited purposes (fluids),
		 * but may need to be corrected for more general-purpose usage.
		 */		
		case NORTH: 
			quad = BakedModelBase.createQuad(getFormat(), new Vec3d(xmax, ymax, zmin), new Vec3d(xmax, ymin, zmin),
				new Vec3d(xmin, ymin, zmin), new Vec3d(xmin, ymax, zmin), texture, xm, xx, ym, yx, inverted);
			break;
		case SOUTH:
			quad = BakedModelBase.createQuad(getFormat(), new Vec3d(xmin, ymax, zmax), new Vec3d(xmin, ymin, zmax),
				new Vec3d(xmax, ymin, zmax), new Vec3d(xmax, ymax, zmax), texture, xm, xx, ym, yx, inverted);
			break;
		case WEST:
			quad = BakedModelBase.createQuad(getFormat(), new Vec3d(xmin, ymax, zmin), new Vec3d(xmin, ymin, zmin),
				new Vec3d(xmin, ymin, zmax), new Vec3d(xmin, ymax, zmax), texture, xm, xx, ym, yx, inverted);
			break;
		case DOWN:
			quad = BakedModelBase.createQuad(getFormat(), new Vec3d(xmin, ymin, zmin), new Vec3d(xmax, ymin, zmin),
				new Vec3d(xmax, ymin, zmax), new Vec3d(xmin, ymin, zmax), texture, xm, xx, zm, zx, inverted);
			break;
		case EAST:
			quad = BakedModelBase.createQuad(getFormat(), new Vec3d(xmax, ymax, zmax), new Vec3d(xmax, ymin, zmax),
				new Vec3d(xmax, ymin, zmin), new Vec3d(xmax, ymax, zmin), texture, xm, xx, ym, yx, inverted);
			break;
		case UP:
			quad = BakedModelBase.createQuad(getFormat(), new Vec3d(xmax, ymax, zmin), new Vec3d(xmin, ymax, zmin),
				new Vec3d(xmin, ymax, zmax), new Vec3d(xmax, ymax, zmax), texture, xm, xx, zm, zx, inverted);
			break;
		default:
			throw new RuntimeException("Code should be unreachable - Unknown EnumFacing in switch statement.");
		}
		return Collections.singletonList(quad);
	}
}
