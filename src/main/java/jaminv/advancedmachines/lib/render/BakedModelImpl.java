package jaminv.advancedmachines.lib.render;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public class BakedModelImpl implements IBakedModel {
	
	public static BakedModelImpl INSTANCE = new BakedModelImpl();	
	
	private static final Map<String, IBakedModel> cache = new HashMap<String, IBakedModel>();
	
	public static String buildCacheKey(ItemStack stack) {
		ToStringHelper helper = MoreObjects.toStringHelper(stack);
		helper.add("item", Item.REGISTRY.getNameForObject(stack.getItem()).toString());
		helper.add("meta", Items.DIAMOND.getDamage(stack));
		helper.add("nbt", stack.getTagCompound().toString());
		return helper.toString();
	}
	
	@Nullable
	public static IBakedModel getCachedItemModel(ItemStack stack) {
		IBakedModel model = cache.get(buildCacheKey(stack));
		if (model != null) { return model; }
		return generateItemModel(stack);
	}
	
	@Nullable
	private static IBakedModel generateItemModel(ItemStack stack) {
		Item item = stack.getItem();
		if (item instanceof ItemBlock) {
			Block block = Block.getBlockFromItem(item);
			if (block instanceof ModelBakeryProvider) {
				ModelBakery bakery = ((ModelBakeryProvider)block).getModelBakery();
				return new BakedModelWrapper(bakery.bakeItemModel(stack), null);
			}
		}
		return null;
	}
	
	public static String buildCacheKey(IBlockState state) {
		ToStringHelper helper = MoreObjects.toStringHelper(state);

		helper.add("meta", state.getBlock().getMetaFromState(state)); 
		for (Entry<IProperty<?>, Comparable<?>> entry : state.getProperties().entrySet()) {
			helper.add(entry.getKey().getName(), entry.getValue().toString());
		}
		
		if (state instanceof IExtendedBlockState) {
			IExtendedBlockState ext = (IExtendedBlockState)state;
			for (Entry<IUnlistedProperty<?>, Optional<?>> entry: ext.getUnlistedProperties().entrySet()) {
				if (!entry.getValue().isPresent()) {
					helper.add(entry.getKey().getName(), "<NULL>");
				} else {
					IUnlistedProperty prop = entry.getKey();
					helper.add(prop.getName(), prop.valueToString(ext.getValue(prop)));
				}
			}
		}
		return helper.toString();
	}	
	
	@Nullable
	public static IBakedModel getCachedModel(IBlockState state) {
		IBakedModel model = cache.get(buildCacheKey(state));
		if (model != null) { return model; }
		return generateModel(state);
	}
	
	@Nullable
	private static IBakedModel generateModel(IBlockState state) {
		Block block = state.getBlock();
		if (block instanceof ModelBakeryProvider) {
			ModelBakery bakery = ((ModelBakeryProvider)block).getModelBakery();
			return new BakedModelWrapper(bakery.bakeModel(state), bakery.getParticleTexture(state));
		}
		return null;
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		return getCachedModel(state).getQuads(state, side, rand);
	}

	@Override
	public boolean isAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean isGui3d() {
		return false;
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
				IBakedModel model = BakedModelImpl.getCachedItemModel(stack);
				if (model == null) { return modelOld; }
				return model;
			}
		};
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return TextureHelper.getMissingTexture();
	}
}
