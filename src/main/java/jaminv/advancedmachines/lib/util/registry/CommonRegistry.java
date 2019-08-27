package jaminv.advancedmachines.lib.util.registry;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import jaminv.advancedmachines.init.BlockInit;
import jaminv.advancedmachines.init.FluidInit;
import jaminv.advancedmachines.init.ItemInit;
import jaminv.advancedmachines.lib.render.ModelBakery;
import jaminv.advancedmachines.lib.render.ModelBakeryProvider;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.RegistryEvent;

public class CommonRegistry {
	
	protected static List<Block> blockRegistry = new ArrayList<Block>();
	protected static List<Item> itemRegistry = new ArrayList<Item>();
	
	public void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(blockRegistry.toArray(new Block[0]));
	}
	
	public void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(itemRegistry.toArray(new Item[0]));
	}
	
	public void registerModels() {}	
	
	public void addBlock(Block block, String registryName) {
		block.setRegistryName(registryName);
		block.setUnlocalizedName(registryName);
		blockRegistry.add(block);
	}
	
	public void addBlockWithItem(Block block, String registryName) {
		addBlock(block, registryName);
		addItem(new ItemBlock(block), registryName);
	}
		
	public void addBlockWithBakedModel(Block block, ModelBakeryProvider provider, String registryName) {
		addBlockWithItem(block, registryName);
	}

	public void addItem(Item item, String registryName) {
		item.setRegistryName(registryName);
		item.setUnlocalizedName(registryName);
		itemRegistry.add(item);
	}
}
