package jaminv.advancedmachines.lib.render.quad;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class QuadBuilderBlock implements QuadBuilder {

	private TextureAtlasSprite texture, face, top, bottom;
	private EnumFacing facing = EnumFacing.NORTH;
	private Cuboid cuboid = Cuboid.UNIT;
	
	public QuadBuilderBlock(TextureAtlasSprite texture) {
		this.texture = this.face = this.top = this.bottom = texture;
		facing = EnumFacing.NORTH;
	}
			
	public QuadBuilderBlock withFace(EnumFacing facing, TextureAtlasSprite face) {
		this.facing = facing;
		this.face = face;
		return this;
	}
	
	public QuadBuilderBlock withTop(TextureAtlasSprite top) { this.top = top; return this; }
	public QuadBuilderBlock withBottom(TextureAtlasSprite bottom) { this.bottom = bottom; return this; }
	public QuadBuilderBlock withTopBottom(TextureAtlasSprite texture) {
		this.top = this.bottom = texture;
		return this; 
	}
	
	public QuadBuilderBlock withCuboid(Cuboid cuboid) { this.cuboid = cuboid; return this; }
	public Cuboid getCuboid() { return cuboid; }
	
	@Override
	public List<BakedQuad> build() {
		List<BakedQuad> quads = new LinkedList<BakedQuad>();
		
		for (EnumFacing side : EnumFacing.values()) {
			TextureAtlasSprite sprite = texture;
			if (side == facing) { sprite = face; }
			if (side == EnumFacing.UP) { sprite = top; }
			if (side == EnumFacing.DOWN) { sprite = bottom; }
			
			quads.addAll((new QuadBuilderBlockFace(sprite, side)).withCuboid(cuboid).build());
		}
		
		return quads;
	}
}
