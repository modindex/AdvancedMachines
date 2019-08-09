package jaminv.advancedmachines.lib.fluid;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface IFluidHandlerMachine extends IFluidHandlerInternal {
	/** Return copies of the fluids in input tanks */
	public FluidStack[] getInput();
	/** Return copies of the fluids in output tanks */
	public FluidStack[] getOutput();
	
	public IFluidHandlerMachine setCapacity(int capacity);
}
