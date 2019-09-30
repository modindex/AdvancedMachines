package jaminv.advancedmachines.lib.util.registry;

import java.util.ArrayList;
import java.util.List;

import jaminv.advancedmachines.lib.render.ModelBakeryProvider;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class CommonRegistry {
	
	protected static List<Block> blockRegistry = new ArrayList<Block>();
	protected static List<Item> itemRegistry = new ArrayList<Item>();
	protected static List<FluidData> fluidRegistry = new ArrayList<>();
	
	protected static class FluidData {
		Fluid fluid;
		String registryName;
		Material material;
		
		public FluidData(Fluid fluid, String registryName, Material material) {
			this.fluid = fluid; this.registryName = registryName; this.material = material;
		}
	}
	
	public void registerBlocks(RegistryEvent.Register<Block> event) {
		for (FluidData data : fluidRegistry) {
			FluidRegistry.registerFluid(data.fluid);
			FluidRegistry.addBucketForFluid(data.fluid);
			
			BlockFluidClassic blockFluid = new BlockFluidClassic(data.fluid, data.material);
			blockFluid.setRegistryName(data.registryName);
			blockFluid.setUnlocalizedName(data.registryName);
			blockRegistry.add(blockFluid);
			registerFluidModel(blockFluid);
		}

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
	
	public void addBlockWithItem(Block block, ItemBlock item, String registryName) {
		addBlock(block, registryName);
		addItem(item, registryName);
	}
		
	public void addBlockWithBakedModel(Block block, ModelBakeryProvider provider, String registryName) {
		addBlockWithItem(block, registryName);
	}
	
	public void addBlockWithBakedModel(Block block, ItemBlock item, ModelBakeryProvider provider, String registryName) {
		addBlockWithItem(block, item, registryName);
	}

	public void addItem(Item item, String registryName) {
		item.setRegistryName(registryName);
		item.setUnlocalizedName(registryName);
		itemRegistry.add(item);
	}
	
	public void addFluid(Fluid fluid, String registryName, Material material) {
		fluidRegistry.add(new FluidData(fluid, registryName, material));
	}
	
	protected void registerFluidModel(BlockFluidClassic blockFluid) {}
}
