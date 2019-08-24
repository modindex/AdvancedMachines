package jaminv.advancedmachines.lib.render;

import java.util.HashSet;

import jaminv.advancedmachines.util.Reference;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

public class BakedModelLoader implements ICustomModelLoader {
	
	private static HashSet<String> resources = new HashSet<String>();
	
	public static void register(String resource) {
		resources.add(resource);
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {	}

	@Override
	public boolean accepts(ResourceLocation modelLocation) {
		if (modelLocation.getResourceDomain().equals(Reference.MODID)) {
			int a = 0;
		}
		return modelLocation.getResourceDomain().equals(Reference.MODID) &&
			resources.contains(modelLocation.getResourcePath());
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception {
		return ModelImpl.INSTANCE;
	}
}