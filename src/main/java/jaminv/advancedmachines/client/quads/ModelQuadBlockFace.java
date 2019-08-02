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
	
	public ModelQuadBlockFace(VertexFormat format, TextureAtlasSprite texture, EnumFacing side, boolean inverted) {
		super(format);
		this.texture = texture;
		this.side = side;
		this.inverted = inverted;
	}
	
	public ModelQuadBlockFace(VertexFormat format, TextureAtlasSprite texture, EnumFacing side) {
		this(format, texture, side, false);
	}

	@Override
	public List<BakedQuad> getQuads() {
		BakedQuad quad;
		
		switch (side) {
		case NORTH: 
			quad = BakedModelBase.createQuad(getFormat(), new Vec3d(1, 1, 0), new Vec3d(1, 0, 0), new Vec3d(0, 0, 0), new Vec3d(0, 1, 0), texture, inverted);
			break;
		case SOUTH:
			quad = BakedModelBase.createQuad(getFormat(), new Vec3d(0, 1, 1), new Vec3d(0, 0, 1), new Vec3d(1, 0, 1), new Vec3d(1, 1, 1), texture, inverted);
			break;
		case WEST:
			quad = BakedModelBase.createQuad(getFormat(), new Vec3d(0, 1, 0), new Vec3d(0, 0, 0), new Vec3d(0, 0, 1), new Vec3d(0, 1, 1), texture, inverted);
			break;
		case DOWN:
			quad = BakedModelBase.createQuad(getFormat(), new Vec3d(0, 0, 0), new Vec3d(1, 0, 0), new Vec3d(1, 0, 1), new Vec3d(0, 0, 1), texture, inverted);
			break;
		case EAST:
			quad = BakedModelBase.createQuad(getFormat(), new Vec3d(1, 1, 1), new Vec3d(1, 0, 1), new Vec3d(1, 0, 0), new Vec3d(1, 1, 0), texture, inverted);
			break;
		case UP:
			quad = BakedModelBase.createQuad(getFormat(), new Vec3d(1, 1, 0), new Vec3d(0, 1, 0), new Vec3d(0, 1, 1), new Vec3d(1, 1, 1), texture, inverted);
			break;
		default:
			throw new RuntimeException("Code should be unreachable - Unknown EnumFacing in switch statement.");
		}
		return Collections.singletonList(quad);
	}
}
