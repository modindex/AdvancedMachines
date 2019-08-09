package jaminv.advancedmachines.lib.fluid;

import net.minecraftforge.fluids.capability.IFluidHandler;

public interface IFluidTankAdvanced extends IFluidTankInternal, IFluidHandler, IFluidObservable {
	public boolean isEmpty();
	public void setCapacity(int capacity);
}
