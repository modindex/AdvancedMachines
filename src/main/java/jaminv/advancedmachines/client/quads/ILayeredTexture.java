package jaminv.advancedmachines.client.quads;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public interface ILayeredTexture {
	
	public List<TextureAtlasSprite> getTextures(EnumFacing side); 

}
