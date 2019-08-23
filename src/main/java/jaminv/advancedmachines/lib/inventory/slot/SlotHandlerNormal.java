package jaminv.advancedmachines.lib.inventory.slot;

import net.minecraft.item.ItemStack;

public class SlotHandlerNormal implements ISlotHandler {

	@Override public boolean canInsert(int slot, ItemStack stack) { return true; }

	@Override public boolean canExtract(int slot, int amount, ItemStack contents) { return true; }

	@Override
	public int getStackLimit(int slot, int defaultLimit) {
		return defaultLimit;
	}

}
