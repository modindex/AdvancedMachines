package jaminv.advancedmachines.client;

import com.google.common.collect.ImmutableMap;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.lib.render.TextureHelper;
import jaminv.advancedmachines.lib.render.quad.Texture;
import jaminv.advancedmachines.lib.util.helper.StringHelper;
import jaminv.advancedmachines.util.Reference;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
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

	protected static ImmutableMap<String, Texture> textures;
	
	public static Texture get(String reference) {
		Texture ret = textures.get(reference);
		if (ret == null) { 
			ret = new Texture(TextureHelper.getMissingTexture());
			Main.logger.error("Error loading sprite: '" + reference + "'.");
		}
		return ret;
	}
	
	public static Texture get(String...strings) {
		return get(StringHelper.buildString(strings));
	}
	
	private static TextureMap textureMap;
	
	public static TextureAtlasSprite register(String sprite) {
		return textureMap.registerSprite(new ResourceLocation(Reference.MODID, sprite));
	}
	
	public static void registerTextures(TextureMap textureMap, String path) {
		RawTextures.textureMap = textureMap;
		
		Main.logger.info("Loading raw textures");
		
		ImmutableMap.Builder<String, Texture> map = ImmutableMap.<String, Texture>builder();
		
		ModContainer mod = FMLCommonHandler.instance().findContainerFor(Reference.MODID);
		
		for (String base : bases) {
			for (String variant : variants) {
				for (String file : files) {
					map.put(StringHelper.buildString(base, variant, file), new Texture(register("blocks/machine/base/" + base + "/" + variant + "_" + file)));
				}
			}
		}
		
		for (String type : border_types) {
			for (String dir : borders) {
				map.put("border." + type + "." + dir, new Texture(register("blocks/machine/borders/" + type + "/" + dir)));
			}
		}
		
		map.put("inventory.input", new Texture(register("blocks/machine/expansion/inventory_input")));
		map.put("inventory.output", new Texture(register("blocks/machine/expansion/inventory_output")));
		map.put("energy", new Texture(register("blocks/machine/expansion/energy")));
		map.put("redstone.active", new Texture(register("blocks/machine/expansion/redstone_active")));
		map.put("redstone.inactive", new Texture(register("blocks/machine/expansion/redstone_inactive")));
		
		
		for (String machine : machines) {
			for (String state : states) {
				TextureAtlasSprite sprite = register("blocks/machine/instance/" + machine + "/" + state);
				map.put(machine + "." + state, new Texture(sprite));
				
				TextureAtlasSprite sprite3 = sprite;
				for (int x = 0; x < 3; x++) {
					for (int y = 0; y < 3; y++) {
						map.put(machine + "." + state + ".3x3[" + Integer.toString(x) + "][" + Integer.toString(y) + "]",
							new Texture(sprite3).withUV(16*x/3f, 16*y/3f, 16*(x+1)/3f, 16*(y+1)/3f));
					}
				}
				
				TextureAtlasSprite sprite2 = sprite;
				for (int x = 0; x < 2; x++) {
					for (int y = 0; y < 2; y++) {
						map.put(machine + "." + state + ".2x2[" + Integer.toString(x) + "][" + Integer.toString(y) + "]",
							new Texture(sprite2).withUV(16*x/2f,  16*y/2f, 16*(x+1)/2f, 16*(y+1)/2f));
					}
				}
			}
		}
		
		
		TextureAtlasSprite sprite = register("blocks/machine/instance/alloy/active3x3");
		sprite.setIconHeight(48);
		sprite.setIconWidth(48);
		sprite.initSprite(16, 16, 16, 16, false);
		map.put("alloy.active.3x3", new Texture(sprite).withUV(16/3f, 16/3f, 32/3f, 32/3f));

		textures = map.build();

		Main.logger.info("Completed - Loading raw textures");
	}
}
