package jaminv.advancedmachines.lib.util.registry;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import jaminv.advancedmachines.lib.render.BakedModelLoader;
import jaminv.advancedmachines.lib.render.ModelBakery;
import jaminv.advancedmachines.lib.render.ModelBakeryProvider;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ClientRegistry  extends CommonRegistry {

	protected static List<Item> modelRegistry = new ArrayList<Item>();
	protected static List<Pair<Block, ModelBakeryProvider>> bakedModelRegistry = new ArrayList<>();
	
	@Override
	public void registerModels() {
		// %1.13% Shouldn't be needed
		for (Item item : modelRegistry) {
			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "normal"));
		}
		// END 1.13
		
		for (Pair<Block, ModelBakeryProvider> pair : bakedModelRegistry) {
			BakedModelLoader.register(pair.getLeft().getRegistryName(), pair.getRight().getModelBakery());
		}
	}		
	
	// %1.13% Shouldn't be needed
	public void addItem(Item item, String registryName) {
		super.addItem(item, registryName);
		modelRegistry.add(item);
	}

	@Override
	public void addBlockWithBakedModel(Block block, ModelBakeryProvider provider, String registryName) {
		super.addBlockWithBakedModel(block, provider, registryName);
		bakedModelRegistry.add(Pair.of(block, provider));
	}
	

}
