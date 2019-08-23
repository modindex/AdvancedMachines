package jaminv.advancedmachines.lib.fluid;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

public interface IFluidHandlerAdvanced extends IFluidHandlerInternal, INBTSerializable<NBTTagCompound> {
	/** Set the capacities of all tanks */
	public void setFluidCapacity(int capacity);
	
	int getTankCount();
	
	/** Allows direct modification of the tanks. */
	IFluidTankInternal getTank(int index);
}
