package jaminv.advancedmachines.lib.render.quad;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class Texture {

	private TextureAtlasSprite sprite;
	private float umin = 0f, umax = 16f, vmin = 0f, vmax = 16f;
	
	public Texture(TextureAtlasSprite sprite) {
		this.sprite = sprite;
	}
	
	public Texture withUV(float umin, float vmin, float umax, float vmax) {
		this.umin = umin; this.umax = umax;
		this.vmin = vmin; this.vmax = vmax;
		return this;
	}
	
	public TextureAtlasSprite getSprite() { return sprite; }
	public float getUMin() { return umin; }
	public float getUMax() { return umax; }
	public float getVMin() { return vmin; }
	public float getVMax() { return vmax; }
}
