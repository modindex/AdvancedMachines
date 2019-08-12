package jaminv.advancedmachines.lib.inventory;

import java.util.Collections;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class MachineInventoryHandler extends ItemStackHandlerObservable implements IItemHandlerMachine {
	
	int inputSlots = 0, outputSlots = 0, secondarySlots = 0, additionalSlots = 0;
	
	public MachineInventoryHandler() {
		super();
	}
	
	public boolean isSlotInput(int slotIndex) { return slotIndex <= getLastInputSlot(); }
	public boolean isSlotOutput(int slotIndex) { return slotIndex >= getFirstOutputSlot() && slotIndex <= getLastOutputSlot(); }
	public boolean isSlotSecondary(int slotIndex) { return slotIndex >= getFirstSecondarySlot() && slotIndex <= getLastSecondarySlot(); }
	public boolean isSlotAdditional(int slotIndex) { return slotIndex >= getFirstAdditionalSlot(); }
	
	protected int getFirstInputSlot() { return 0; }
	protected int getFirstOutputSlot() { return inputSlots; }
	protected int getFirstSecondarySlot() { return inputSlots + outputSlots; }
	protected int getFirstAdditionalSlot() { return inputSlots + outputSlots + secondarySlots; }

	protected int getLastInputSlot() { return getFirstOutputSlot() - 1; }
	protected int getLastOutputSlot() { return getFirstSecondarySlot() - 1; }
	protected int getLastSecondarySlot() { return getFirstAdditionalSlot() - 1; }
	protected int getLastAdditionalSlot() { return stacks.size() - 1; }	
	
	/**
	 * Recommended that these methods be called in order (addInputSlots, addOutputSlots, addSecondarySlots, addAdditionalSlots), and only once.
	 * It should work fine otherwise, but causes array shifting.
	 */
	public MachineInventoryHandler addInputSlots(int numSlots) {
		stacks.addAll(inputSlots, Collections.nCopies(numSlots, ItemStack.EMPTY));
		inputSlots += numSlots;
		return this;
	}
	
	/**
	 * Recommended that these methods be called in order (addInputSlots, addOutputSlots, addSecondarySlots, addAdditionalSlots), and only once.
	 * It should work fine otherwise, but causes array shifting.
	 */
	public MachineInventoryHandler addOutputSlots(int numSlots) {
		stacks.addAll(inputSlots + outputSlots, Collections.nCopies(numSlots, ItemStack.EMPTY));
		outputSlots += numSlots;
		return this;		
	}
	
	/**
	 * Recommended that these methods be called in order (addInputSlots, addOutputSlots, addSecondarySlots, addAdditionalSlots), and only once.
	 * It should work fine otherwise, but causes array shifting.
	 */
	public MachineInventoryHandler addSecondarySlots(int numSlots) {
		stacks.addAll(inputSlots + outputSlots + secondarySlots, Collections.nCopies(numSlots, ItemStack.EMPTY));
		secondarySlots += numSlots;
		return this;
	}
	
	/**
	 * Recommended that these methods be called in order (addInputSlots, addOutputSlots, addSecondarySlots, addAdditionalSlots), and only once.
	 * It should work fine otherwise, but causes array shifting.
	 */
	public MachineInventoryHandler addAdditionalSlots(int numSlots) {
		stacks.addAll(inputSlots + outputSlots + secondarySlots + additionalSlots, Collections.nCopies(numSlots, ItemStack.EMPTY));
		additionalSlots += numSlots;
		return this;
	}
	
	protected ItemStack[] getStacks(int firstSlot, int lastSlot) {
		ItemStack[] ret = new ItemStack[lastSlot - firstSlot + 1];
		for (int i = firstSlot; i <= lastSlot; i++) {
			ret[i] = stacks.get(i).copy();
		}
		return ret;
	}
	
	public ItemStack[] getInput() { return getStacks(getFirstInputSlot(), getLastInputSlot()); }
	public ItemStack[] getOutput() { return getStacks(getFirstOutputSlot(), getLastOutputSlot()); }
	
	/* IItemHandler */
	
	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (!isSlotInput(slot)) {
			return stack;
		}
		
		return super.insertItem(slot, stack, simulate);
	}
	
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (!isSlotOutput(slot) && !isSlotSecondary(slot)) {
			return ItemStack.EMPTY;
		}
		
		return super.extractItem(slot, amount, simulate);
	}
	
	/* Internal */
	
	public ItemStack insertItem(ItemStack stack, boolean simulate) {
		return insertItem(getFirstInputSlot(), getLastInputSlot(), stack, simulate);
	}
	
	public ItemStack insertItemInternal(ItemStack stack, boolean simulate) {
		return insertItem(getFirstOutputSlot(), getLastOutputSlot(), stack, simulate);
	}
	
	public ItemStack insertSecondary(ItemStack stack, boolean simulate) {
		return insertItem(getFirstSecondarySlot(), getLastSecondarySlot(), stack, simulate);
	}
	
	protected ItemStack insertItem(int firstSlot, int lastSlot, ItemStack stack, boolean simulate) {
		ItemStack ret = stack; // We don't need a copy because insertItem() will copy
		
		for (int i = firstSlot; i <= lastSlot; i++) {
			ret = insertItem(i, ret, simulate);
		}
		
		return ret;
	}
	
	public int extractItem(IItemGeneric input, boolean simulate) {
		return extractInput(getFirstOutputSlot(), getLastSecondarySlot(), input, simulate);		
	}
	
	public int extractItemInternal(IItemGeneric input, boolean simulate) {
		return extractInput(getFirstInputSlot(), getLastInputSlot(), input, simulate);
	}
	
	protected int extractInput(int firstSlot, int lastSlot, IItemGeneric input, boolean simulate) {
		int count = 0;
		for (int i = firstSlot; i <= lastSlot; i++) {
			if (input.isValid(stacks.get(i))) {
				ItemStack result = extractItem(i, input.getCount(), false);
				count += result.getCount();
			}
			if (count >= input.getCount()) { break; }
		}
		
		return count;
	}
	
	/* NBT */

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = super.serializeNBT();
		nbt.setInteger("inputSlots", inputSlots);
		nbt.setInteger("outputSlots", outputSlots);
		nbt.setInteger("secondarySlots", secondarySlots);
		nbt.setInteger("additionalSlots", additionalSlots);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("inputSlots")) { inputSlots = nbt.getInteger("inputSlots"); }
		if (nbt.hasKey("outputSlots")) { outputSlots = nbt.getInteger("outputSlots"); }
		if (nbt.hasKey("secondarySlots")) { secondarySlots = nbt.getInteger("secondarySlots"); }
		if (nbt.hasKey("additionalSlots")) { additionalSlots = nbt.getInteger("additionalSlots"); }
	}

}
