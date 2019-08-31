package jaminv.advancedmachines.lib.render.quad;

import java.util.Collections;
import java.util.List;

import jaminv.advancedmachines.lib.render.ModelBakeryHelper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

public class QuadBuilderBlockFace implements QuadBuilder {

	protected Texture texture;
	protected EnumFacing side;
	protected boolean inverted = false;
	
	Cuboid cuboid = Cuboid.UNIT;
	
	public QuadBuilderBlockFace(Texture texture, EnumFacing side) {
		this.texture = texture;
		this.side = side;
	}
	
	public QuadBuilderBlockFace withCuboid(Cuboid cuboid) {
		this.cuboid = cuboid; return this;
	}
	public Cuboid getCuboid() { return cuboid; }
	
	public QuadBuilderBlockFace invert() { this.inverted = true; return this; }
	public QuadBuilderBlockFace invert(boolean invert) { this.inverted = invert; return this; }	

	@Override
	public List<BakedQuad> build() {
		BakedQuad quad;
		VertexFormat format = DefaultVertexFormats.ITEM;
		float xmin = cuboid.getXMin(), xmax = cuboid.getXMax();
		float ymin = cuboid.getYMin(), ymax = cuboid.getYMax();
		float zmin = cuboid.getZMin(), zmax = cuboid.getZMax();
		
		/*
		 * The UV coordinates may or may not be correct. They have worked so far for my very limited purposes (fluids & large textures),
		 * but may need to be corrected for more general-purpose usage. (Top and Bottom UV coordinated may not work correctly).
		 */		
        float uf = (texture.getUMax() - texture.getUMin());
        float vf = (texture.getVMax() - texture.getVMin());
        float umin = texture.getUMin();
        float vmin = texture.getVMin();

        float xm = umin + uf * xmin, xx = umin + uf * xmax;
		float ym = vmin + vf * ymin, yx = vmin + vf * ymax;
		float zm = vmin + vf * zmin, zx = vmin + vf * zmax;
		
		switch (side) {
		case NORTH: 
			quad = ModelBakeryHelper.createQuad(format, new Vec3d(xmax, ymax, zmin), new Vec3d(xmax, ymin, zmin),
				new Vec3d(xmin, ymin, zmin), new Vec3d(xmin, ymax, zmin), texture.getSprite(), xm, xx, ym, yx, inverted);
			break;
		case SOUTH:
			quad = ModelBakeryHelper.createQuad(format, new Vec3d(xmin, ymax, zmax), new Vec3d(xmin, ymin, zmax),
				new Vec3d(xmax, ymin, zmax), new Vec3d(xmax, ymax, zmax), texture.getSprite(), xm, xx, ym, yx, inverted);
			break;
		case WEST:
			quad = ModelBakeryHelper.createQuad(format, new Vec3d(xmin, ymax, zmin), new Vec3d(xmin, ymin, zmin),
				new Vec3d(xmin, ymin, zmax), new Vec3d(xmin, ymax, zmax), texture.getSprite(), xm, xx, ym, yx, inverted);
			break;
		case DOWN:
			quad = ModelBakeryHelper.createQuad(format, new Vec3d(xmin, ymin, zmin), new Vec3d(xmax, ymin, zmin),
				new Vec3d(xmax, ymin, zmax), new Vec3d(xmin, ymin, zmax), texture.getSprite(), xm, xx, zm, zx, inverted);
			break;
		case EAST:
			quad = ModelBakeryHelper.createQuad(format, new Vec3d(xmax, ymax, zmax), new Vec3d(xmax, ymin, zmax),
				new Vec3d(xmax, ymin, zmin), new Vec3d(xmax, ymax, zmin), texture.getSprite(), xm, xx, ym, yx, inverted);
			break;
		case UP:
			quad = ModelBakeryHelper.createQuad(format, new Vec3d(xmax, ymax, zmin), new Vec3d(xmin, ymax, zmin),
				new Vec3d(xmin, ymax, zmax), new Vec3d(xmax, ymax, zmax), texture.getSprite(), xm, xx, zm, zx, inverted);
			break;
		default:
			throw new RuntimeException("Code should be unreachable - Unknown EnumFacing in switch statement.");
		}
		return Collections.singletonList(quad);
	}
}
