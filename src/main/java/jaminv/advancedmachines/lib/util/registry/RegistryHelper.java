package jaminv.advancedmachines.lib.util.registry;

import jaminv.advancedmachines.lib.LibReference;
import jaminv.advancedmachines.lib.render.ModelBakeryProvider;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.SidedProxy;

public class RegistryHelper {
	
	@SidedProxy(clientSide=LibReference.CLIENT_REGISTRY, serverSide=LibReference.COMMON_REGISTRY)
	public static CommonRegistry proxy;
	// %1.13%:
	//public static CommonRegistry proxy = DistExecutor.runForDist(() -> ClientRegistry::new, () -> CommonRegistry::new);
	
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		proxy.registerBlocks(event);
	}
	
	public static void registerItems(RegistryEvent.Register<Item> event) {
		proxy.registerItems(event);
	}
	
	public static void registerModels() {
		proxy.registerModels();
	}		
	
	public static void addBlock(Block block, String registryName) {
		proxy.addBlock(block, registryName);
	}
	
	public static void addBlockWithItem(Block block, String registryName) {
		proxy.addBlockWithItem(block, registryName);
	}
	
	public static void addBlockWithItem(Block block, ItemBlock item, String registryName) {
		proxy.addBlockWithItem(block, item, registryName);
	}
	
	public static void addBlockWithBakedModel(Block block, ModelBakeryProvider provider, String registryName) {
		proxy.addBlockWithBakedModel(block, provider, registryName);
	}
	
	public static void addBlockWithBakedModel(Block block, ItemBlock item, ModelBakeryProvider provider, String registryName) {
		proxy.addBlockWithBakedModel(block, item, provider, registryName);
	}
	
	public static <T extends Block & ModelBakeryProvider>void addBlockWithBakedModel(T block, String registryName) {
		proxy.addBlockWithBakedModel(block, block, registryName);	
	}
	
	public static <T extends Block & ModelBakeryProvider>void addBlockWithBakedModel(T block, ItemBlock item, String registryName) {
		proxy.addBlockWithBakedModel(block, item, block, registryName);
	}
	
	public static void addItem(Item item, String registryName) {
		proxy.addItem(item, registryName);
	}
	
	public static void addFluid(Fluid fluid, String registryName, Material material) {
		proxy.addFluid(fluid, registryName, material);
	}
}
