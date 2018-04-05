package jaminv.advancedmachines.objects.blocks.machine;

import jaminv.advancedmachines.objects.items.ItemStackHandlerObservable;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class ItemStackHandlerMachineWrapper implements IItemHandlerModifiable {

	TileEntityMachineBase te;
	ItemStackHandler items;
	
	public ItemStackHandlerMachineWrapper(TileEntityMachineBase te, ItemStackHandler items) {
		this.te = te;
		this.items = items;
	}

	@Override
	public int getSlots() {
		return items.getSlots();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return items.getStackInSlot(slot);
	}

	@Override
	public int getSlotLimit(int slot) {
		return items.getSlotLimit(slot);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (slot < te.getFirstInputSlot() || slot > te.getFirstInputSlot() + te.getInputCount() - 1) {
			return stack;
		}
		if (!te.getRecipeManager().isItemValid(stack, null)) {
			return stack;
		}
		
		return items.insertItem(slot, stack, simulate);
	}
	
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (slot >= te.getFirstInputSlot() && slot <= te.getFirstInputSlot() + te.getInputCount() - 1) {
			return ItemStack.EMPTY;
		}
		
		return items.extractItem(slot, amount, simulate);
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		items.setStackInSlot(slot, stack);
	}
}

