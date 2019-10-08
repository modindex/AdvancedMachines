package jaminv.advancedmachines.lib.fluid;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

/**
 * FluidTank
 * 
 * FluidTank is an extension of IFluidTank with additional handling
 * for input/output limitations, as well as additional methods to
 * allow bypassing these limitations (for internal transfers).
 * @author jamin
 *
 */
public interface FluidTank extends IFluidTank, FluidObservable, INBTSerializable<NBTTagCompound> {
	public int fillInternal(FluidStack resource, boolean doFill);
	public FluidStack drainInternal(FluidStack resource, boolean doDrain);
	public FluidStack drainInternal(int maxDrain, boolean doDrain);
	
	public boolean canFill();
	public boolean canDrain();
	
	public boolean isEmpty();
	public FluidTank copy();	
}
