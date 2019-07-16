package jaminv.advancedmachines.util.models;

import jaminv.advancedmachines.util.material.MaterialBase;
import jaminv.advancedmachines.util.models.BlockstateMaker.Properties;
import jaminv.advancedmachines.util.models.BlockstateMaterial.MaterialProperties;

public interface IItemTextureProvider {
	public String getItemTextures(MaterialBase variant);
}
