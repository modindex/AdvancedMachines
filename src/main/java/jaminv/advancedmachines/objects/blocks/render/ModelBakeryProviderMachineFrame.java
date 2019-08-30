package jaminv.advancedmachines.objects.blocks.render;

import java.util.List;
import java.util.Map;

import jaminv.advancedmachines.client.RawTextures;
import jaminv.advancedmachines.lib.render.ModelBakery;
import jaminv.advancedmachines.lib.render.ModelBakeryProvider;
import jaminv.advancedmachines.lib.render.TransformationMap;
import jaminv.advancedmachines.lib.render.quad.LayeredTextureList;
import jaminv.advancedmachines.lib.render.quad.QuadBuilderLayeredBlock;
import jaminv.advancedmachines.machine.multiblock.model.MultiblockTextureBase;
import jaminv.advancedmachines.machine.multiblock.model.TextureSide;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModelBakeryProviderMachineFrame implements ModelBakeryProvider {	
	@SideOnly (Side.CLIENT)
	public static class ModelBakeryMachineFrame implements ModelBakery {
		VariantExpansion variant;
		
		public ModelBakeryMachineFrame(VariantExpansion variant) {
			this.variant = variant;
		}
		
		public List<BakedQuad> bake() {
			return new QuadBuilderLayeredBlock(new LayeredTextureList(
					MultiblockTextureBase.MULTIPLY.getItemTexture(variant, TextureSide.SIDE),
					RawTextures.get("machine_frame")))
				.withTopBottom(new LayeredTextureList(
					MultiblockTextureBase.MULTIPLY.getItemTexture(variant, TextureSide.SIDE),
					RawTextures.get("machine_frame")))
				.build();					
		}
		
		@Override
		public List<BakedQuad> bakeModel(IBlockState state) {
			return bake();
		}

		@Override
		public List<BakedQuad> bakeItemModel(ItemStack stack) {
			return bake();
		}

		@Override
		public TextureAtlasSprite getParticleTexture() {
			return RawTextures.get("machine_frame").getSprite();
		}

		@Override
		public Map<TransformType, TRSRTransformation> getTransformationMap() {
			return TransformationMap.DEFAULT_BLOCK;
		}
	}
	
	VariantExpansion variant;

	public ModelBakeryProviderMachineFrame(VariantExpansion variant) {
		this.variant = variant;
	}

	@Override
	public ModelBakery getModelBakery() {
		return new ModelBakeryMachineFrame(variant);
	}
}
