package jaminv.advancedmachines.lib.fluid;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public interface IFluidTankAdvanced extends IFluidTankInternal, IFluidHandler, IFluidObservable, INBTSerializable<NBTTagCompound> {
	public boolean isEmpty();
	public void setFluid(FluidStack fluid);
	public void setCapacity(int capacity);
	public IFluidTankAdvanced copy();
}
