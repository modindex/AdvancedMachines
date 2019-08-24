package jaminv.advancedmachines.lib.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fluids.FluidStack;

public class TextureHelper {
	public static TextureAtlasSprite getMissingTexture() {
		return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
	}	
	
	public static TextureAtlasSprite getFluidTexture(FluidStack fluid) {
		if (fluid == null) { return getMissingTexture(); }
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluid.getFluid().getStill(fluid).toString());	
	}	
}
