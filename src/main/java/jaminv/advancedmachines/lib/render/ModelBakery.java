package jaminv.advancedmachines.lib.render;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.model.TRSRTransformation;

public interface ModelBakery {
	public default List<BakedQuad> bakeModel(IBlockState state) {
		return Collections.emptyList();
	}
	
	public default List<BakedQuad> bakeItemModel(ItemStack stack) {
		return Collections.emptyList();
	}
	
	public default TextureAtlasSprite getParticleTexture(String variant) {
		return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
	}
	
	public default Map<TransformType, TRSRTransformation> getTransformationMap() {
		return TransformationMap.DEFAULT_BLOCK;
	}
}
