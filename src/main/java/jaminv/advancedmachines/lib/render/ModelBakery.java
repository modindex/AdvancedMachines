package jaminv.advancedmachines.lib.render;

import java.util.Collections;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

public interface ModelBakery {
	public default List<BakedQuad> bakeModel(IBlockState state) {
		return Collections.emptyList();
	}
	
	public default List<BakedQuad> bakeItemModel(ItemStack stack) {
		return Collections.emptyList();
	}
	
	public default TextureAtlasSprite getParticleTexture(IBlockState state) {
		return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
	}
}
