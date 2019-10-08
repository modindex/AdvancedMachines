package jaminv.advancedmachines.lib.render.quad;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.EnumFacing;

public class QuadBuilderBlock implements QuadBuilder {

	private boolean inverted;
	private Texture texture, top, bottom;
	private EnumFacing facing = EnumFacing.NORTH;
	private Cuboid cuboid = Cuboid.UNIT;
	
	public QuadBuilderBlock(Texture texture) {
		this.texture = this.top = this.bottom = texture;
		facing = EnumFacing.NORTH;
	}
	
	public QuadBuilderBlock withTop(Texture top) { this.top = top; return this; }
	public QuadBuilderBlock withBottom(Texture bottom) { this.bottom = bottom; return this; }
	public QuadBuilderBlock withTopBottom(Texture texture) {
		this.top = this.bottom = texture;
		return this; 
	}
	
	public QuadBuilderBlock withCuboid(Cuboid cuboid) { this.cuboid = cuboid; return this; }
	public Cuboid getCuboid() { return cuboid; }
	
	public QuadBuilderBlock invert() { this.inverted = true; return this; }
	public QuadBuilderBlock invert(boolean invert) { this.inverted = invert; return this; }
	
	@Override
	public List<BakedQuad> build() {
		List<BakedQuad> quads = new LinkedList<BakedQuad>();
		
		for (EnumFacing side : EnumFacing.values()) {
			Texture sprite = texture;
			if (side == EnumFacing.UP) { sprite = top; }
			if (side == EnumFacing.DOWN) { sprite = bottom; }
			
			quads.addAll((new QuadBuilderBlockFace(sprite, side)).withCuboid(cuboid).invert(inverted).build());
		}
		
		return quads;
	}
}
