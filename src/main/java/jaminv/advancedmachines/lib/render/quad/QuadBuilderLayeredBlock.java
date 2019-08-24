package jaminv.advancedmachines.lib.render.quad;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;

public class QuadBuilderLayeredBlock implements QuadBuilder {
	
	private boolean inverted = false;
	public QuadBuilderLayeredBlock invert() { this.inverted = true; return this; }
	
	private float xminOff = 0, xmaxOff = 0, yminOff = 0, ymaxOff, zminOff = 0, zmaxOff = 0;
	public QuadBuilderLayeredBlock offset(float xoff, float yoff, float zoff) {
		this.xminOff = xoff; this.xmaxOff = xoff;
		this.yminOff = yoff; this.ymaxOff = yoff;
		this.zminOff = zoff; this.zmaxOff = zoff;
		return this;
	}
	public QuadBuilderLayeredBlock offset(float xminOff, float xmaxOff, float yminOff, float ymaxOff, float zminOff, float zmaxOff) {
		this.xminOff = xminOff; this.xmaxOff = xmaxOff;
		this.yminOff = yminOff; this.ymaxOff = ymaxOff;
		this.zminOff = zminOff; this.zmaxOff = zmaxOff;
		return this;
	}

	private EnumFacing facing;
	private LayeredTexture texture, face, top, bottom;
	public QuadBuilderLayeredBlock(EnumFacing facing, LayeredTexture texture, LayeredTexture face, LayeredTexture top, LayeredTexture bottom) {
		this.facing = facing;
		this.texture = texture;
		this.face = face;
		this.top = top;
		this.bottom = bottom;
	}
	
	public QuadBuilderLayeredBlock(EnumFacing facing, LayeredTexture texture, LayeredTexture face) {
		this(facing, texture, face, texture, texture);
	}
	
	public QuadBuilderLayeredBlock(LayeredTexture texture) {
		this(EnumFacing.NORTH, texture, texture, texture, texture);
	}

	@Override
	public List<BakedQuad> build() {
		List<BakedQuad> quads = new LinkedList<BakedQuad>();
		
		for (EnumFacing side : EnumFacing.values()) {
			LayeredTexture layers = texture;
			if (side == facing) { layers = face; }
			if (side == EnumFacing.UP) { layers = top; }
			if (side == EnumFacing.DOWN) { layers = bottom; }
			
			List<TextureAtlasSprite> textures = layers.getTextures(side); 
			
			for (TextureAtlasSprite sprite : textures) {
				quads.addAll((new QuadBuilderBlockFace.Unit(sprite, side, inverted).offset(xminOff, xmaxOff, yminOff, ymaxOff, zminOff, zmaxOff)).build());
			}
		}
		
		return quads;
	}
}
