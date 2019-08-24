package jaminv.advancedmachines.lib.render.quad;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public interface LayeredTexture {
	
	public List<TextureAtlasSprite> getTextures(EnumFacing side); 

}
