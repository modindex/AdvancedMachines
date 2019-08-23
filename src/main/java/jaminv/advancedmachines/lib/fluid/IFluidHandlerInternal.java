package jaminv.advancedmachines.lib.fluid;

import java.util.List;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;

public interface IFluidHandlerInternal extends IFluidHandler, IFluidObservable {
	public int fillInternal(FluidStack resource, boolean doFill);
	public FluidStack drainInternal(FluidStack resource, boolean doDrain);
	public FluidStack drainInternal(int maxDrain, boolean doDrain);
	
	/** Return copies of the fluids in tanks */
	public FluidStack[] getStacks();
	/** Returns copies of the tanks. */
	public IFluidTankInternal[] getTanks();	
}
