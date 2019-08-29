package jaminv.advancedmachines.lib.render.quad;

import java.util.List;

import net.minecraft.util.EnumFacing;

public interface LayeredTexture {
	public List<Texture> getTextures(EnumFacing side);
	public LayeredTexture copy();
}
