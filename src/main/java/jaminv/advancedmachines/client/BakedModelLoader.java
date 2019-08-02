package jaminv.advancedmachines.client;

import java.util.HashMap;

import jaminv.advancedmachines.util.Reference;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

public class BakedModelLoader implements ICustomModelLoader {
	
	private static HashMap<String, Class<? extends BakedModelBase>> models = new HashMap<String, Class<? extends BakedModelBase>>();
	
	public static void register(String resource, Class<? extends BakedModelBase> model) {
		models.put(resource, model);
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {	}

	@Override
	public boolean accepts(ResourceLocation modelLocation) {
		return modelLocation.getResourceDomain().equals(Reference.MODID) &&
			models.containsKey(modelLocation.getResourcePath());
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception {
		return new ModelBase(models.get(modelLocation.getResourcePath()));
	}

}