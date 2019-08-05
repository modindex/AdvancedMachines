package jaminv.advancedmachines.client.quads;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;

public class ModelQuadLayeredBlock extends ModelQuadBase {
	
	private boolean inverted = false;
	public ModelQuadLayeredBlock invert() { this.inverted = true; return this; }
	
	private float xoff = 0, yoff = 0, zoff = 0;
	public ModelQuadLayeredBlock offset(float xoff, float yoff, float zoff) {
		this.xoff = xoff; this.yoff = yoff; this.zoff = zoff;
		return this;
	}

	private EnumFacing facing;
	private ILayeredTexture texture, face, top, bottom;
	public ModelQuadLayeredBlock(VertexFormat format, EnumFacing facing, ILayeredTexture texture, ILayeredTexture face, ILayeredTexture top, ILayeredTexture bottom) {
		super(format);
		this.facing = facing;
		this.texture = texture;
		this.face = face;
		this.top = top;
		this.bottom = bottom;
	}
	
	public ModelQuadLayeredBlock(VertexFormat format, EnumFacing facing, ILayeredTexture texture, ILayeredTexture face) {
		this(format, facing, texture, face, texture, texture);
	}
	
	public ModelQuadLayeredBlock(VertexFormat format, ILayeredTexture texture) {
		this(format, EnumFacing.NORTH, texture, texture, texture, texture);
	}

	@Override
	public List<BakedQuad> getQuads() {
		List<BakedQuad> quads = new LinkedList<BakedQuad>();
		
		for (EnumFacing side : EnumFacing.values()) {
			ILayeredTexture layers = texture;
			if (side == facing) { layers = face; }
			if (side == EnumFacing.UP) { layers = top; }
			if (side == EnumFacing.DOWN) { layers = bottom; }
			
			List<TextureAtlasSprite> textures = layers.getTextures(side); 
			
			for (TextureAtlasSprite sprite : textures) {
				quads.addAll((new ModelQuadBlockFace.Unit(getFormat(), sprite, side, inverted).offset(xoff, yoff, zoff)).getQuads());
			}
		}
		
		return quads;
	}

}
