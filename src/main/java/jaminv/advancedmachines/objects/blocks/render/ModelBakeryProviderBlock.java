package jaminv.advancedmachines.objects.blocks.render;

import java.util.List;
import java.util.Map;

import jaminv.advancedmachines.client.RawTextures;
import jaminv.advancedmachines.lib.render.ModelBakery;
import jaminv.advancedmachines.lib.render.ModelBakeryProvider;
import jaminv.advancedmachines.lib.render.TransformationMap;
import jaminv.advancedmachines.lib.render.quad.QuadBuilderBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModelBakeryProviderBlock implements ModelBakeryProvider {
	@SideOnly (Side.CLIENT)
	public static class Bakery implements ModelBakery {
		String texture, top, bottom;
		
		public Bakery(String texture) {
			this.texture = this.top = this.bottom;
		}
		
		public Bakery withTop(String top) { this.top = top; return this; }
		public Bakery withBottom(String bottom) { this.bottom = bottom; return this; }
		public Bakery withTopBottom(String texture) { this.top = this.bottom = texture; return this; }

		protected List<BakedQuad> bake() {
			return new QuadBuilderBlock(RawTextures.get(texture)).withTop(RawTextures.get(top)).withBottom(RawTextures.get(bottom)).build();
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
			return RawTextures.get(texture).getSprite();
		}

		@Override
		public Map<TransformType, TRSRTransformation> getTransformationMap() {
			return TransformationMap.DEFAULT_ITEM;
		}
	}
	
	
	String texture, top, bottom;
	public ModelBakeryProviderBlock(String texture) {
		this.texture = texture;		
	}

	public ModelBakeryProviderBlock withTop(String top) { this.top = top; return this; }
	public ModelBakeryProviderBlock withBottom(String bottom) { this.bottom = bottom; return this; }
	public ModelBakeryProviderBlock withTopBottom(String texture) { this.top = this.bottom = texture; return this; }	
	
	@Override
	public ModelBakery getModelBakery() {
		return new Bakery(texture).withTop(top).withBottom(bottom);
	}

}
