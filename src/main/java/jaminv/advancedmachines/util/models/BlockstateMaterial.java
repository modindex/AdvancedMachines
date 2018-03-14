package jaminv.advancedmachines.util.models;

import java.io.FileWriter;
import java.io.IOException;

import jaminv.advancedmachines.util.material.MaterialBase;
import jaminv.advancedmachines.util.material.MaterialBase.MaterialType;

public class BlockstateMaterial extends BlockstateMaker {

	protected MaterialType type;
	
	public class MaterialProperties extends Properties {
		MaterialBase variant;
		public MaterialProperties(Properties prop, MaterialBase variant) {
			super(prop);
			this.variant = variant;
		}
	}
	
	public BlockstateMaterial(String name, String folder, MaterialType type) {
		super(name, folder);
		this.type = type;
	}
	
	@Override
	protected String makeProperties(Properties prop, boolean first) throws IOException {
		String ret = "";
		
		for (MaterialBase variant : MaterialBase.values(type)) {
			MaterialProperties mprop = new MaterialProperties(prop, variant);
			ret += makeEntry(mprop, first);
			first = false;
		}
		return ret;
	}
	
	@Override
	protected String getVariantString(Properties prop) {
		MaterialProperties mprop = (MaterialProperties)prop;
		
		return super.getVariantString(prop) +
			",variant=" + mprop.variant.getName();
	}

	@Override
	protected String getTextureFolder(String facing, Properties prop) {
		MaterialProperties mprop = (MaterialProperties)prop;
		return folder + mprop.variant.getName() + "/";
	}

}
