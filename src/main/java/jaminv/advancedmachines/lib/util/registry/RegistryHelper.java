package jaminv.advancedmachines.lib.util.registry;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import jaminv.advancedmachines.lib.render.BakedModelLoader;
import jaminv.advancedmachines.lib.render.ModelBakery;
import jaminv.advancedmachines.lib.render.ModelBakeryProvider;
import jaminv.advancedmachines.proxy.handlers.RegistryHandler.CustomStateMapper;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;

public class RegistryHelper {
	
	protected static List<Block> blockRegistry = new ArrayList<Block>();
	protected static List<Item> itemRegistry = new ArrayList<Item>();
	protected static List<ModelRegistryData> modelRegistry = new ArrayList<ModelRegistryData>();
	protected static List<BakedModelRegistryData> bakedModelRegistry = new ArrayList<BakedModelRegistryData>();
	
	protected static class ModelRegistryData {
		final Item item;
		final int meta;
		final ModelResourceLocation resource;
		
		public ModelRegistryData(Item item, int meta, ModelResourceLocation loc) {
			this.item = item;
			this.meta = meta;
			this.resource = loc;
		}
	}
	
	protected static class BakedModelRegistryData {
		final Block block;
		final IStateMapper stateMapper;
		final ModelResourceLocation resource;
		final ModelBakery bakery;
		
		public BakedModelRegistryData(Block block, IStateMapper stateMapper, ModelResourceLocation loc, ModelBakery bakery) {
			this.block = block;
			this.stateMapper = stateMapper;
			this.resource = loc;
			this.bakery = bakery;
		}
	}
	
	/*
	 * Block Registry
	 */
	
	public static void registerBlock(Block block, String registryName) {
		block.setRegistryName(registryName);
		block.setUnlocalizedName(block.getRegistryName().toString());
		blockRegistry.add(block);
	}
	
	public static void registerBlockWithItem(Block block, String registryName) {
		registerBlock(block, registryName);
		registerItem(new ItemBlock(block), registryName);
	}
	
	public static void registryBlockWithVariantItem(Block block, ItemBlock item, String registryName) {
		registerBlock(block, registryName);
		registerItemWithoutModel(item, registryName);
		
		StateMapperBase mapper = new DefaultStateMapper();
		BlockStateContainer stateCont = block.getBlockState();
		ImmutableList<IBlockState> validStates = stateCont.getValidStates();
		
		for (IBlockState state : validStates) {
			String variant = mapper.getPropertyString(state.getProperties());
			registerItemModel(item, block.getMetaFromState(state), variant);
		}
	}
	
	public static <T extends Block & ModelBakeryProvider> void registerBlockWithBakedModel(T block, String registryName, ModelBakery bakery) {
		registerBlockWithItem(block, registryName);
		
		bakedModelRegistry.add(new BakedModelRegistryData(block, new BypassStateMapper(resource), ))
		ModelResourceLocation resource = new ModelResourceLocation(block.getRegistryName(), "normal");
		ModelLoader.setCustomStateMapper(block, );
		BakedModelLoader.register(resource, ((ModelBakeryProvider)block).getModelBakery());				
	}
	
	/*
	 * Item Registry
	 */
	
	public static void registerItemWithoutModel(Item item, String registryName) {
		item.setRegistryName(registryName);
		item.setUnlocalizedName(item.getRegistryName().toString());
		itemRegistry.add(item);
	}
	
	public static void registerItem(Item item, String registryName) {
		registerItemWithoutModel(item, registryName);
		registerItemModel(item);
	}

	public static <T extends MetaVariant> void registerItemWithVariant(Item item, String registryName, T variant) {
		registerItemWithoutModel(item, registryName);
		String variantName = variant.getId();
		NonNullList<ItemStack> subItems = NonNullList.create();
		item.getSubItems(CreativeTabs.SEARCH, subItems);
		for (ItemStack stack : subItems) {
			registerItemModel(item, stack.getMetadata(), variantName + "=" + variant.byMetadata(stack.getMetadata()).getName());
		}
	}
	
	/*
	 * Model Registry
	 */

	public static void registerItemModel(Item item) {
		registerItemModel(item, 0, "normal");
	}
	
	public static void registerItemModel(Item item, int meta, String variant) {
		modelRegistry.add(new ModelRegistryData(item, meta, new ModelResourceLocation(item.getRegistryName(), variant)));
	}		
}
