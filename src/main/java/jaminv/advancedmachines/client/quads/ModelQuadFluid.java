package jaminv.advancedmachines.client.quads;

import java.util.Collections;
import java.util.List;

import jaminv.advancedmachines.client.RawTextures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;

public class ModelQuadFluid extends ModelQuadBlock {
	public ModelQuadFluid(VertexFormat format, FluidStack fluid, float percent) {
		super(format, 0, 1, 0, percent, 0, 1, RawTextures.getFluidTexture(fluid));
	}
}
