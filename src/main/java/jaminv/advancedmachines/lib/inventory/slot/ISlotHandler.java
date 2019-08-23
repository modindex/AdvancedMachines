package jaminv.advancedmachines.lib.inventory.slot;

import net.minecraft.item.ItemStack;

public interface ISlotHandler {
	public boolean canInsert(int slot, ItemStack stack);
	public boolean canExtract(int slot, int amount, ItemStack contents);
	
	public int getStackLimit(int slot, int defaultLimit);	
}
