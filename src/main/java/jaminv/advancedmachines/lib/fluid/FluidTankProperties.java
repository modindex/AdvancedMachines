package jaminv.advancedmachines.lib.fluid;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class FluidTankProperties implements IFluidTankProperties {
	
	protected final IFluidTank tank;
	protected final boolean canFill, canDrain;
	public FluidTankProperties(IFluidTank tank, boolean canFill, boolean canDrain) {
		this.tank = tank;
		this.canFill = canFill;
		this.canDrain = canDrain;
	}
	
	@Override
	public FluidStack getContents() {
		return tank.getFluid();
	}
	
	@Override
	public int getCapacity() {
		return tank.getCapacity();
	}
	
	@Override
	public boolean canFill() {
		return this.canFill;
	}
	
	@Override
	public boolean canDrain() {
		return this.canDrain;
	}
	
	@Override
	public boolean canFillFluidType(FluidStack fluidStack) {
		return this.canFill;
	}
	
	@Override
	public boolean canDrainFluidType(FluidStack fluidStack) {
		return this.canDrain;
	}
}
