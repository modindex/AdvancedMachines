package jaminv.advancedmachines.util.models;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import jaminv.advancedmachines.util.material.MaterialBase;
import jaminv.advancedmachines.util.material.MaterialBase.MaterialType;
import jaminv.advancedmachines.util.models.BlockstateMaker.Properties;

public class BlockStateHelper {
	public static void makeVariantItems(String name, MaterialType type, IItemTextureProvider callback) throws IOException {
		for (MaterialBase variant : MaterialBase.values(type)) {
			File file = new File("..\\src\\main\\resources\\assets\\advmach\\models\\item\\" + name + "_" + variant.getUnlocalizedName() + ".json");
			file.createNewFile();
			FileWriter writer = new FileWriter(file, false);
		
			writer.write("{\r\n" +
				"    \"parent\": \"advmach:block/" + name + "\",\r\n" +
				"    \"textures\": {\r\n"
			);
			writer.write(callback.getItemTextures(variant));
			writer.write("    }\r\n}");
			writer.close();
		}
	}
}
