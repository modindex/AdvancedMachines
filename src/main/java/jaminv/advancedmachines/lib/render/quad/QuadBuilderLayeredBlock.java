package jaminv.advancedmachines.lib.render.quad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.EnumFacing;

public class QuadBuilderLayeredBlock implements QuadBuilder {
	
	private boolean inverted = false;
	protected Cuboid cuboid = Cuboid.UNIT;
	private EnumFacing facing = EnumFacing.NORTH;
	private LayeredTexture texture, top, bottom; //, back, left, right; // Possibly Later
	private LayeredTexture face = new LayeredTextureList(Collections.emptyList());
	
	public QuadBuilderLayeredBlock(LayeredTexture texture) {
		this.texture = this.face = this.top = this.bottom = texture;
	}
	
	public QuadBuilderLayeredBlock withFace(EnumFacing facing, LayeredTexture face) {
		this.facing = facing;
		this.face = face;
		return this;
	}
	
	public QuadBuilderLayeredBlock withTop(LayeredTexture top) { this.top = top; return this; }
	public QuadBuilderLayeredBlock withBottom(LayeredTexture bottom) { this.bottom = bottom; return this; }
	public QuadBuilderLayeredBlock withTopBottom(LayeredTexture texture) {
		this.top = this.bottom = texture;
		return this; 
	}

	public QuadBuilderLayeredBlock invert() { this.inverted = true; return this; }
	public QuadBuilderLayeredBlock invert(boolean invert) { this.inverted = invert; return this; }
	
	public QuadBuilderLayeredBlock withCuboid(Cuboid cuboid) { this.cuboid = cuboid; return this; }
	public Cuboid getCuboid() { return cuboid; }
	
	@Override
	public List<BakedQuad> build() {
		List<BakedQuad> quads = new LinkedList<BakedQuad>();
		
		for (EnumFacing side : EnumFacing.values()) {
			LayeredTexture layers = texture;
			if (side == EnumFacing.UP) { layers = top; }
			if (side == EnumFacing.DOWN) { layers = bottom; }
			
			List<Texture> textures = new ArrayList<Texture>();
			textures.addAll(layers.getTextures(side));
			if (side == facing) { textures.addAll(face.getTextures(side)); }
			
			for (Texture sprite : textures) {
				quads.addAll(new QuadBuilderBlockFace(sprite, side).withCuboid(cuboid).invert(inverted).build());
			}
		}
		
		return quads;
	}
}
