package jaminv.advancedmachines.lib.render.quad;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public interface LayeredTexture {
	
	public static LayeredTexture of(TextureAtlasSprite ... sprites) {
		return new LayeredTextureList(sprites);
	}
	
	public List<TextureAtlasSprite> getTextures(EnumFacing side);
	public LayeredTexture copy();
}
