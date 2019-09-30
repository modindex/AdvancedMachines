package jaminv.advancedmachines.lib.fluid;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public interface FluidContainerItem {
    /**
     * Returns an array of objects which represent the internal tanks.
     * These objects cannot be used to manipulate the internal tanks.
     *
     * @param stack ItemStack of item container
     * @return Properties for the relevant internal tanks.
     */	
	public IFluidTankProperties[] getTankProperties(ItemStack stack);
	
    /**
    * @param stack ItemStack of item container
    * @param resource FluidStack attempting to fill the tank.
    * @param doFill If false, the fill will only be simulated.
    * @return Amount of fluid that was accepted by the tank.
    */
   int fill(ItemStack stack, FluidStack resource, boolean doFill);

   /**
    * @param stack ItemStack of item container
    * @param resource Fluid and amount to be removed from the container.
    * @param doDrain If false, the drain will only be simulated.
    * @return Amount of fluid that was removed from the tank.
    */
   @Nullable
   FluidStack drain(ItemStack stack, FluidStack resource, boolean doDrain);
   
   /**
    * @param stack ItemStack of item container
    * @param maxDrain Maximum amount of fluid to drain.
    * @param doDrain  If false, drain will only be simulated.
    * @return FluidStack representing the Fluid and amount that was (or would have been, if
    * simulated) drained.
    */
   @Nullable
   FluidStack drain(ItemStack stack, int maxDrain, boolean doDrain);   
}
