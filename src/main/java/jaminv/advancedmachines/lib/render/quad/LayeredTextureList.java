package jaminv.advancedmachines.lib.render.quad;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import scala.actors.threadpool.Arrays;

public class LayeredTextureList implements LayeredTexture {
	
	protected List<TextureAtlasSprite> list = new ArrayList<>();

	public LayeredTextureList() {}
	public LayeredTextureList(List<TextureAtlasSprite> list) {
		this.list.addAll(list);
	}
	public LayeredTextureList(TextureAtlasSprite ... sprites) {
		this.list.addAll(Arrays.asList(sprites));	
	}
	
	public void add(TextureAtlasSprite sprite) {
		list.add(sprite);
	}
	
	@Override
	public LayeredTexture copy() {
		return new LayeredTextureList(list);
	}
	@Override
	public List<TextureAtlasSprite> getTextures(EnumFacing side) {
		return list;
	}
}
