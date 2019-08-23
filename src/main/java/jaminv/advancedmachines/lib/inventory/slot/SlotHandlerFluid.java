package jaminv.advancedmachines.lib.inventory.slot;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidUtil;

public class SlotHandlerFluid implements ISlotHandler {

	@Override public boolean canInsert(int slot, ItemStack stack) { return FluidUtil.getFluidHandler(stack) != null; }

	@Override public boolean canExtract(int slot, int amount, ItemStack contents) { return true; }

	@Override
	public int getStackLimit(int slot, int defaultLimit) {
		return 1;
	}

}
