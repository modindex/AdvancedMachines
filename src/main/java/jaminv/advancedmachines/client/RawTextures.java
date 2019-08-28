package jaminv.advancedmachines.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.lib.render.TextureHelper;
import jaminv.advancedmachines.lib.util.helper.StringHelper;
import jaminv.advancedmachines.util.Reference;
import jaminv.advancedmachines.util.logger.Logger;
import jaminv.advancedmachines.util.parser.DataParserException;
import jaminv.advancedmachines.util.parser.FileHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ModContainer;

public class RawTextures {
	
	public final static String[] bases = { "multiply", "speed", "productivity", "tank" };
	public final static String[] variants = { "basic", "compressed", "quad", "improbable" };
	public final static String[] files = { "base", "all" };

	public final static String[] machines = { "alloy", "furnace", "grinder", "purifier", "melter", "stabilizer", "injector" };
	public final static String[] states = { "active", "inactive" };
	
	public final static String[] border_types = { "single", "solid_dark", "solid_gray" };
	public final static String[] borders = { "top", "left", "right", "bottom" };

	protected static ImmutableMap<String, TextureAtlasSprite> textures;
	
	public static TextureAtlasSprite get(String reference) {
		TextureAtlasSprite ret = textures.get(reference);
		if (ret == null) { 
			ret = TextureHelper.getMissingTexture();
			Main.logger.error("Error loading sprite: '" + reference + "'.");
		}
		return ret;
	}
	
	public static TextureAtlasSprite get(String...strings) {
		return get(StringHelper.buildString(strings));
	}
	
	private static TextureMap textureMap;
	
	public static TextureAtlasSprite register(String sprite) {
		return textureMap.registerSprite(new ResourceLocation(Reference.MODID, sprite));
	}
	
	public static void registerTextures(TextureMap textureMap, String path) {
		RawTextures.textureMap = textureMap;
		
		Main.logger.info("Loading raw textures");
		
		ImmutableMap.Builder<String, TextureAtlasSprite> map = ImmutableMap.<String, TextureAtlasSprite>builder();
		ImmutableSet.Builder<ResourceLocation> set = ImmutableSet.<ResourceLocation>builder();
		
		ModContainer mod = FMLCommonHandler.instance().findContainerFor(Reference.MODID);
		
		for (String base : bases) {
			for (String variant : variants) {
				for (String file : files) {
					map.put(StringHelper.buildString(base, variant, file), register("blocks/machine/base/" + base + "/" + variant + "_" + file));
				}
			}
		}
		
		for (String type : border_types) {
			for (String dir : borders) {
				map.put("border." + type + "." + dir, register("blocks/machine/borders/" + type + "/" + dir));
			}
		}
		
		map.put("inventory.input", register("blocks/machine/expansion/inventory_input"));
		map.put("inventory.output", register("blocks/machine/expansion/inventory_output"));
		map.put("energy", register("blocks/machine/expansion/energy"));
		map.put("redstone.active", register("blocks/machine/expansion/redstone_active"));
		map.put("redstone.inactive", register("blocks/machine/expansion/redstone_inactive"));
		
		
		for (String machine : machines) {
			for (String state : states) {
				map.put(machine + "." + state, register("blocks/machine/instance/" + machine + "/" + state));

/*					for (int x = 0; x < 3; x++) {
						for (int y = 0; y < 3; y++) {
							map.put(machine + "." + state + "." + material + "." + "f3x3p" + Integer.toString(x) + Integer.toString(y),
								register("blocks/machine/instance/" + machine + "/" + state + "/" + material + "/f3x3p" + x + y));
						}
					}
					
					for (int x = 0; x < 2; x++) {
						for (int y = 0; y < 2; y++) {
							map.put(machine + "." + state + "." + material + "." + "f2x2p" + Integer.toString(x) + Integer.toString(y),
								register("blocks/machine/instance/" + machine + "/" + state + "/" + material + "/f2x2p" + x + y));
				}
			*/
			}
		}

		textures = map.build();

		Main.logger.info("Completed - Loading raw textures");
	}
}
