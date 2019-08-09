package jaminv.advancedmachines.lib.fluid;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

public interface IFluidTankInternal extends IFluidTank {
	public int fillInternal(FluidStack resource, boolean doFill);
	public FluidStack drainInternal(FluidStack resource, boolean doDrain);
	public FluidStack drainInternal(int maxDrain, boolean doDrain);
}
