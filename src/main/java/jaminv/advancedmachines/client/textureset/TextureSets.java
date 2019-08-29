package jaminv.advancedmachines.client.textureset;

import java.util.HashMap;
import java.util.Map;

import jaminv.advancedmachines.client.RawTextures;
import jaminv.advancedmachines.lib.render.TextureHelper;
import jaminv.advancedmachines.lib.render.quad.Texture;
import jaminv.advancedmachines.lib.util.helper.StringHelper;

public class TextureSets {
	private static Map<String, Texture> set = new HashMap<String, Texture>();
	
	public static void put(String key, Texture sprite) {
		set.put(key, sprite);
	}
	
	public static void put(String key, String reference) {
		set.put(key, RawTextures.get(reference));
	}
	
	public static Texture get(String key) {
		Texture sprite = set.get(key);
		if (sprite == null) { return new Texture(TextureHelper.getMissingTexture()); }
		return sprite;
	}
	
	public static Texture get(String set, String key) {
		return get(set + "." + key);
	}
	
	public static Texture get(String...strings) {
		return get(StringHelper.buildString(strings));
	}

}
