package jaminv.advancedmachines.lib.fluid;

import javax.annotation.Nullable;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class FluidTankProperties implements IFluidTankProperties {
	
	protected final FluidStack fluid;
	protected final int capacity;
	protected final boolean canFill, canDrain;
	public FluidTankProperties(FluidTank tank) {
		this.fluid = tank.getFluid();
		this.capacity = tank.getCapacity();
		this.canFill = tank.canFill();
		this.canDrain = tank.canDrain();
	}
	
	public FluidTankProperties(@Nullable FluidStack fluid, int capacity, boolean canFill, boolean canDrain) {
		this.fluid = fluid;
		this.capacity = capacity;
		this.canFill = canFill;
		this.canDrain = canDrain;
	}
	
	@Override
	public FluidStack getContents() {
		return fluid;
	}
	
	@Override
	public int getCapacity() {
		return capacity;
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
