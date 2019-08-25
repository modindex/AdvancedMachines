package jaminv.advancedmachines.lib.render.quad;

import java.util.Collections;
import java.util.List;

import jaminv.advancedmachines.client.RawTextures;
import jaminv.advancedmachines.lib.render.TextureHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;

public class QuadBuilderFluid extends QuadBuilderBlock {
	public QuadBuilderFluid(FluidStack fluid, float percent) {
		super(0, 1, 0, percent, 0, 1, TextureHelper.getFluidTexture(fluid));
	}
}
