package jaminv.advancedmachines.client.textureset;

import java.util.HashMap;
import java.util.Map;

import jaminv.advancedmachines.client.RawTextures;
import jaminv.advancedmachines.lib.render.TextureHelper;
import jaminv.advancedmachines.lib.util.helper.StringHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class TextureSets {
	private static Map<String, TextureAtlasSprite> set = new HashMap<String, TextureAtlasSprite>();
	
	public static void put(String key, TextureAtlasSprite sprite) {
		set.put(key, sprite);
	}
	
	public static void put(String key, String reference) {
		set.put(key, RawTextures.get(reference));
	}
	
	public static TextureAtlasSprite get(String key) {
		TextureAtlasSprite sprite = set.get(key);
		if (sprite == null) { return TextureHelper.getMissingTexture(); }
		return sprite;
	}
	
	public static TextureAtlasSprite get(String set, String key) {
		return get(set + "." + key);
	}
	
	public static TextureAtlasSprite get(String...strings) {
		return get(StringHelper.buildString(strings));
	}

}
