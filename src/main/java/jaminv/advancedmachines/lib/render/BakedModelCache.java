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
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public class BakedModelCache {
	private static final Map<String, List<BakedQuad>> blockCache = new HashMap<String, List<BakedQuad>>();
	private static final Map<String, IBakedModel> itemCache = new HashMap<String, IBakedModel>();
	
	public static String buildCacheKey(ItemStack stack) {
		ToStringHelper helper = MoreObjects.toStringHelper(stack);
		helper.add("item", Item.REGISTRY.getNameForObject(stack.getItem()).toString());
		helper.add("meta", Items.DIAMOND.getDamage(stack));
		if (stack.getTagCompound() != null) { helper.add("nbt", stack.getTagCompound().toString()); }
		return helper.toString();
	}
	
	@Nullable
	public static IBakedModel getItemModel(ItemStack stack) {
		String key = buildCacheKey(stack);
		IBakedModel model = itemCache.get(key);
		if (model != null) { return model; }
		return itemCache.put(key, generateItemModel(stack));
	}
	
	@Nullable
	private static IBakedModel generateItemModel(ItemStack stack) {
		Item item = stack.getItem();
		if (item instanceof ItemBlock) {
			Block block = Block.getBlockFromItem(item);
			if (block instanceof ModelBakeryProvider) {
				ModelBakery bakery = ((ModelBakeryProvider)block).getModelBakery();
				return new BakedModelWrapper(bakery.bakeItemModel(stack), bakery.getTransformationMap());
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
	public static List<BakedQuad> getBlockModel(IBlockState state) {
		String key = buildCacheKey(state);
		List<BakedQuad> quads = blockCache.get(key);
		if (quads != null) { return quads; }
		return blockCache.put(key, generateBlockModel(state));
	}
	
	@Nullable
	private static List<BakedQuad> generateBlockModel(IBlockState state) {
		Block block = state.getBlock();
		if (block instanceof ModelBakeryProvider) {
			ModelBakery bakery = ((ModelBakeryProvider)block).getModelBakery();
			return bakery.bakeModel(state);
		}
		return Collections.emptyList();
	}
}
