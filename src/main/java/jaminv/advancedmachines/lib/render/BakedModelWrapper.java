package jaminv.advancedmachines.lib.render;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;

public class BakedModelWrapper implements IBakedModel {

	List<BakedQuad> quads;
	Map<TransformType, TRSRTransformation> transformationMap;
	ModelBakery bakery;

	public BakedModelWrapper(@Nonnull List<BakedQuad> quads, ModelBakery bakery) {
		this.quads = quads;
		this.transformationMap = bakery.getTransformationMap();
		this.bakery = bakery;
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		return quads;		
	}
	
	@Override
	public boolean isAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return true;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public ItemOverrideList getOverrides() {
		return new ItemOverrideList(Collections.emptyList()) {
			@Override
			public IBakedModel handleItemState(IBakedModel modelOld, ItemStack stack, World world, EntityLivingBase entity) {
				IBakedModel model = BakedModelCache.getItemModel(stack, bakery);
				if (model == null) { return modelOld; }
				return model;
			}
		};
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return TextureHelper.getMissingTexture();
	}

    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType transformType) {
        TRSRTransformation tr = transformationMap.get(transformType);
        if (tr == null) { return Pair.of(this, null); }
        return Pair.of(this, TRSRTransformation.blockCornerToCenter(tr).getMatrix());
    }
}
