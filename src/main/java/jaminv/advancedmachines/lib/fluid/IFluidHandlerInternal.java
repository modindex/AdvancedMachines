package jaminv.advancedmachines.lib.fluid;

import java.util.List;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;

public interface IFluidHandlerInternal extends IFluidHandler {
	public int fillInternal(FluidStack resource, boolean doFill);
	public FluidStack drainInternal(FluidStack resource, boolean doDrain);
	public FluidStack drainInternal(int maxDrain, boolean doDrain);
	
	// TODO: Remove these
	public int getTankCount();
	public IFluidTank getTank(int index);	
}
