package jaminv.advancedmachines.lib.fluid;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;

/**
 * Fluid Handler
 * 
 * FluidHandler is an extension of IFluidHandler that allows for transfer
 * limits, input/output locking, and additional methods for bypassing
 * these limitations (for internal transfers).
 * @author jamin
 *
 */
public interface FluidHandler extends IFluidHandler, FluidObservable, INBTSerializable<NBTTagCompound> {
	public int fillInternal(FluidStack resource, boolean doFill);
	public FluidStack drainInternal(FluidStack resource, boolean doDrain);
	public FluidStack drainInternal(int maxDrain, boolean doDrain);
	
	/**
	 * Fill internal tanks only if a matching fluid already exists.
	 * @param resource 
	 * @param doFill If false, fill will be simulated
	 * @return 
	 */
	public int fillSame(FluidStack resource, boolean doFill);
	
	public boolean canFill();
	public boolean canDrain();
	
	/** Return copies of the fluids in tanks */
	public FluidStack[] getStacks();
	/** Returns copies of the tanks. */
	public IFluidTank[] getTanks();	
}
