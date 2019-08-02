package jaminv.advancedmachines.client.quads;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;

public class ModelQuadBlock extends ModelQuadBase {

	private EnumFacing facing;
	private TextureAtlasSprite texture, face, top, bottom;
	public ModelQuadBlock(VertexFormat format, EnumFacing facing, TextureAtlasSprite texture, TextureAtlasSprite face, TextureAtlasSprite top, TextureAtlasSprite bottom) {
		super(format);
		this.facing = facing;
		this.texture = texture;
		this.face = face;
		this.top = top;
		this.bottom = bottom;
	}
	
	public ModelQuadBlock(VertexFormat format, EnumFacing facing, TextureAtlasSprite texture, TextureAtlasSprite face) {
		this(format, facing, texture, face, texture, texture);
	}
	
	public ModelQuadBlock(VertexFormat format, TextureAtlasSprite texture) {
		this(format, EnumFacing.NORTH, texture, texture, texture, texture);
	}

	@Override
	public List<BakedQuad> getQuads() {
		List<BakedQuad> quads = new LinkedList<BakedQuad>();
		
		for (EnumFacing side : EnumFacing.values()) {
			TextureAtlasSprite sprite = texture;
			if (side == facing) { sprite = face; }
			if (side == EnumFacing.UP) { sprite = top; }
			if (side == EnumFacing.DOWN) { sprite = bottom; }
			
			quads.addAll((new ModelQuadBlockFace(getFormat(), sprite, side)).getQuads());
		}
		
		return quads;
	}

}
