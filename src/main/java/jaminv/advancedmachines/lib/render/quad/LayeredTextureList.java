package jaminv.advancedmachines.lib.render.quad;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.EnumFacing;
import scala.actors.threadpool.Arrays;

public class LayeredTextureList implements LayeredTexture {

	protected List<Texture> list = new ArrayList<>();

	public LayeredTextureList() {}
	public LayeredTextureList(List<Texture> list) {
		this.list.addAll(list);
	}
	public LayeredTextureList(Texture ... textures) {
		this.list.addAll(Arrays.asList(textures));	
	}
	
	public void add(Texture sprite) {
		list.add(sprite);
	}
	
	@Override
	public LayeredTexture copy() {
		return new LayeredTextureList(list);
	}
	@Override
	public List<Texture> getTextures(EnumFacing side) {
		return list;
	}
}
