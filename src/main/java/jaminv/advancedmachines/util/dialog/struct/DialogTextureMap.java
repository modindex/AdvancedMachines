package jaminv.advancedmachines.util.dialog.struct;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.client.gui.GuiScreen;

public class DialogTextureMap<T> extends DialogArea {
	public static enum TextureDefault { DEFAULT };
	
	public static class DialogTextureMapDefault extends DialogTextureMap<TextureDefault> {
		public DialogTextureMapDefault(int x, int y, int w, int h) {
			super(x, y, w, h);
		}
		public DialogTextureMapDefault(int x, int y, int w, int h, int u, int v) {
			super(x, y, w, h);
			this.addTexture(TextureDefault.DEFAULT, u, v);
		}
	}
	
	public DialogTextureMap(int x, int y, int w, int h) {
		super(x, y, w, h);
	}
	
	protected Map<T, DialogTexture> textures = new HashMap<>();
	
	public DialogTextureMap addTexture(T identifier, int u, int v) {
		textures.put(identifier, new DialogTexture(u, v));
		return this;
	}
	
	public DialogTextureMap addTexture(T identifier, DialogTexture texture) {
		textures.put(identifier, texture);
		return this;
	}
	
	@Nullable
	public DialogTexture getTexture(T identifier) {
		return textures.get(identifier);
	}
	
	public void draw(GuiScreen gui, int drawX, int drawY, T identifier) {
		DialogTexture texture = textures.get(identifier);
		if (texture == null) { return; }
		
		gui.drawTexturedModalRect(drawX, drawY, texture.getU(), texture.getV(), w, h);
	}
}
