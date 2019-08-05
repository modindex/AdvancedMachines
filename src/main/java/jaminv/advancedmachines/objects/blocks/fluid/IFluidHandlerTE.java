package jaminv.advancedmachines.objects.blocks.fluid;

import net.minecraftforge.fluids.FluidStack;

public interface IFluidHandlerTE {
	public abstract int getFluidCapacity();
	public FluidStack getFluid();
	public int getFluidAmount();

}
