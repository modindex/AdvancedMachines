package jaminv.advancedmachines.util.models;

import java.io.FileWriter;
import java.io.IOException;

import jaminv.advancedmachines.util.material.MaterialBase;
import jaminv.advancedmachines.util.material.MaterialBase.MaterialType;
import net.minecraft.util.EnumFacing;

public class BlockstateMachine extends BlockstateMaker {

	protected MaterialType type;
	protected String inactive, active;
	
	public class InventoryProperties extends Properties {
		MaterialBase variant;
		EnumFacing facing;
		boolean active;
		public InventoryProperties(Properties prop, MaterialBase variant, EnumFacing facing, boolean active) {
			super(prop);
			this.variant = variant;
			this.facing = facing;
			this.active = active;
		}
	}
	
	public BlockstateMachine(String name, String folder, String inactive, String active, MaterialType type) {
		super(name, folder);
		this.type = type;
		this.inactive = inactive;
		this.active = active;
	}
	
	@Override
	protected String makeProperties(Properties prop, boolean first) throws IOException {
		String ret = "";
		
		for (MaterialBase variant : MaterialBase.values(type)) {
			for (EnumFacing facing : EnumFacing.VALUES) {
				for (int active = 0; active <= 1; active++) {
					InventoryProperties iprop = new InventoryProperties(prop, variant, facing, active == 1);
					ret += makeEntry(iprop, first);
					first = false;
				}
			}
		}
		return ret;
	}
	
	@Override
	protected String getVariantString(Properties prop) {
		InventoryProperties iprop = (InventoryProperties)prop;
		
		return "active=" + (iprop.active ? "true" : "false")  + ","
			+ super.getVariantString(prop) +
			",facing=" + iprop.facing.toString() +
			",variant=" + iprop.variant.getName();
	}

	@Override
	protected String getTextureFolder(String facing, Properties prop) {
		InventoryProperties iprop = (InventoryProperties)prop;
		if (iprop.facing.toString() == facing) {
			if (iprop.active) {
				return active + iprop.variant.getName() + "/";
			} else {
				return inactive + iprop.variant.getName() + "/";
			}
		} else {
			return folder + iprop.variant.getName() + "/";
		}
	}

}
