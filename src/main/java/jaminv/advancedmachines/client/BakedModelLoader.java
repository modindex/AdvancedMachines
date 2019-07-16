package jaminv.advancedmachines.client;

import jaminv.advancedmachines.util.Reference;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

public class BakedModelLoader implements ICustomModelLoader {
	
	public static final ModelMultiblock MODEL_MULTIBLOCK = new ModelMultiblock();

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {	}

	@Override
	public boolean accepts(ResourceLocation modelLocation) {
		return modelLocation.getResourceDomain().equals(Reference.MODID) && "bakedmodelmultiblock".equals(modelLocation.getResourcePath());
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception {
		return MODEL_MULTIBLOCK;
	}

}
