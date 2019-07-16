package jaminv.advancedmachines.util.models;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import jaminv.advancedmachines.util.material.MaterialBase;
import jaminv.advancedmachines.util.material.MaterialBase.MaterialType;
import jaminv.advancedmachines.util.models.BlockstateMaker.Properties;
import jaminv.advancedmachines.util.models.BlockstateMaterial.MaterialProperties;
import net.minecraft.util.EnumFacing;

public class BlockstateMaterialFace extends BlockstateMaker implements IItemTextureProvider {

	protected MaterialType type;
	protected String face;
	
	public class MaterialFaceProperties extends Properties {
		MaterialBase variant;
		EnumFacing facing;
		public MaterialFaceProperties(Properties prop, MaterialBase variant, EnumFacing facing) {
			super(prop);
			this.variant = variant;
			this.facing = facing;
		}
	}
	
	public BlockstateMaterialFace(String name, String folder, String face, MaterialType type) {
		super(name, folder);
		this.type = type;
		this.face = face;
	}
	
	@Override
	public void makeItems() throws IOException {
		BlockStateHelper.makeVariantItems(name, type, this);
	}	
	
	@Override
	public String getItemTextures(MaterialBase variant) {
		MaterialFaceProperties prop = new MaterialFaceProperties(new Properties(true, true, true, true, true, true), variant, EnumFacing.NORTH);
		return "        \"all\": \"advmach:blocks/" + getTextureFolder("up", prop) + "all\",\r\n" +
				"        \"north\": \"advmach:blocks/" + getTextureFolder("north", prop) + "all\"\r\n";
	}	
	
	@Override
	protected String makeProperties(Properties prop, boolean first) throws IOException {
		String ret = "";
		
		for (MaterialBase variant : MaterialBase.values(type)) {
			for (EnumFacing facing : EnumFacing.VALUES) {
				MaterialFaceProperties mprop = new MaterialFaceProperties(prop, variant, facing);
				ret += makeEntry(mprop, first);
				first = false;
			}
		}
		return ret;
	}
	
	@Override
	protected String getVariantString(Properties prop) {
		MaterialFaceProperties mprop = (MaterialFaceProperties)prop;
		
		return super.getVariantString(prop) +
			",facing=" + mprop.facing.toString() +
			",variant=" + mprop.variant.getName();
	}

	@Override
	protected String getTextureFolder(String facing, Properties prop) {
		MaterialFaceProperties mprop = (MaterialFaceProperties)prop;
		if (mprop.facing.toString() == facing) {
			return face + mprop.variant.getName() + "/";
		} else {
			return folder + mprop.variant.getName() + "/";
		}
	}

}
