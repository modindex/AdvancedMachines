package jaminv.advancedmachines.lib.fluid;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

public interface IFluidHandlerMachine extends IFluidHandlerInternal, IFluidObservable, INBTSerializable<NBTTagCompound> {
	/** Return copies of the fluids in input tanks */
	public FluidStack[] getInput();
	/** Returns COPIES of the output tanks. */
	public IFluidTankInternal[] getOutput();
	
	public void setCapacity(int capacity);
}
