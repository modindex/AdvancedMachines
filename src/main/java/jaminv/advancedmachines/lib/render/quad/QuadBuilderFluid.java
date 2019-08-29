package jaminv.advancedmachines.lib.render.quad;

import jaminv.advancedmachines.lib.render.TextureHelper;
import net.minecraftforge.fluids.FluidStack;

public class QuadBuilderFluid extends QuadBuilderBlock {
	public QuadBuilderFluid(FluidStack fluid, float percent) {
		super(TextureHelper.getFluidTexture(fluid));
		this.withCuboid(new Cuboid(0f, 0f, 0f, 1f, percent, 1f));
	}
}
