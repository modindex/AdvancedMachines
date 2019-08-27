package jaminv.advancedmachines.lib.render;

import java.util.HashMap;
import java.util.HashSet;

import jaminv.advancedmachines.util.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

public class BakedModelLoader implements ICustomModelLoader {
	
	private static HashMap<String, ModelBakery> resources = new HashMap<String, ModelBakery>();
	
	public static void register(ResourceLocation modelLocation, ModelBakery bakery) {
		resources.put(modelLocation.getResourcePath(), bakery);
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {	}

	@Override
	public boolean accepts(ResourceLocation modelLocation) {
		// FIXME: Present for debugging only, remove
		if (modelLocation.getResourceDomain().equals(Reference.MODID)) {
			int a = 0;
		}
		// End FIXME
		return modelLocation.getResourceDomain().equals(Reference.MODID) &&
			resources.containsKey(modelLocation.getResourcePath());
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception {
		return new ModelImpl(resources.get(modelLocation.getResourcePath()));
	}
}