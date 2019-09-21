package jaminv.advancedmachines.lib.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jaminv.advancedmachines.lib.inventory.slot.ISlotHandler;
import jaminv.advancedmachines.lib.inventory.slot.SlotHandlerNormal;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

public class MachineInventoryHandler extends ItemStackHandlerObservable implements ItemHandlerSeparated {
	
	int inputSlots = 0, outputSlots = 0, secondarySlots = 0, additionalSlots = 0;
	List<ISlotHandler> additionalHandlers = new ArrayList<ISlotHandler>(); 
	
	public MachineInventoryHandler() {
		super();
		stacks = NonNullList.create();
	}
	
	public boolean isSlotInput(int slotIndex) { return slotIndex <= getLastInputSlot(); }
	public boolean isSlotOutput(int slotIndex) { return slotIndex >= getFirstOutputSlot() && slotIndex <= getLastOutputSlot(); }
	public boolean isSlotSecondary(int slotIndex) { return slotIndex >= getFirstSecondarySlot() && slotIndex <= getLastSecondarySlot(); }
	public boolean isSlotAdditional(int slotIndex) { return slotIndex >= getFirstAdditionalSlot(); }
	
	public int getInputSlotCount() { return inputSlots; }
	public int getOutputSlotCount() { return outputSlots; }
	public int getSecondarySlotCount() { return secondarySlots; }
	public int getAdditionalSlotCount() { return additionalSlots; }
	
	public int getFirstInputSlot() { return 0; }
	public int getFirstOutputSlot() { return inputSlots; }
	public int getFirstSecondarySlot() { return inputSlots + outputSlots; }
	public int getFirstAdditionalSlot() { return inputSlots + outputSlots + secondarySlots; }

	public int getLastInputSlot() { return getFirstOutputSlot() - 1; }
	public int getLastOutputSlot() { return getFirstSecondarySlot() - 1; }
	public int getLastSecondarySlot() { return getFirstAdditionalSlot() - 1; }
	public int getLastAdditionalSlot() { return stacks.size() - 1; }	
	
	/**
	 * Recommended that these methods be called in order (addInputSlots, addOutputSlots, addSecondarySlots, addAdditionalSlots), and only once.
	 * It should work fine otherwise, but causes array shifting.
	 */
	public MachineInventoryHandler addInputSlots(int numSlots) {
		List<ItemStack> add = Collections.nCopies(numSlots, ItemStack.EMPTY);
		if (inputSlots == 0) { stacks.addAll(add); } else { stacks.addAll(inputSlots, add); }
		inputSlots += numSlots;
		return this;
	}
	
	/**
	 * Recommended that these methods be called in order (addInputSlots, addOutputSlots, addSecondarySlots, addAdditionalSlots), and only once.
	 * It should work fine otherwise, but causes array shifting.
	 */
	public MachineInventoryHandler addOutputSlots(int numSlots) {
		List<ItemStack> add = Collections.nCopies(numSlots, ItemStack.EMPTY);
		stacks.addAll(inputSlots + outputSlots, add);
		outputSlots += numSlots;
		return this;		
	}
	
	/**
	 * Recommended that these methods be called in order (addInputSlots, addOutputSlots, addSecondarySlots, addAdditionalSlots), and only once.
	 * It should work fine otherwise, but causes array shifting.
	 */
	public MachineInventoryHandler addSecondarySlots(int numSlots) {
		List<ItemStack> add = Collections.nCopies(numSlots, ItemStack.EMPTY);
		stacks.addAll(inputSlots + outputSlots + secondarySlots, add);
		secondarySlots += numSlots;
		return this;
	}
	
	/**
	 * Recommended that these methods be called in order (addInputSlots, addOutputSlots, addSecondarySlots, addAdditionalSlots), and only once.
	 * It should work fine otherwise, but causes array shifting.
	 */
	public MachineInventoryHandler addAdditionalSlots(int numSlots, ISlotHandler handler) {
		List<ItemStack> add = Collections.nCopies(numSlots, ItemStack.EMPTY);
		stacks.addAll(inputSlots + outputSlots + secondarySlots + additionalSlots, add);
		additionalSlots += numSlots;
		
		additionalHandlers.addAll(Collections.nCopies(numSlots, handler));
		
		return this;
	}
	
	public MachineInventoryHandler addAdditionalSlots(int numSlots) {
		return this.addAdditionalSlots(numSlots, new SlotHandlerNormal());
	}
	
	/* IItemHandler */

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (this.isSlotAdditional(slot)) {
			if (!additionalHandlers.get(slot - this.getFirstAdditionalSlot()).canInsert(slot, stack)) { return stack; }
		}
		return super.insertItem(slot, stack, simulate);
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (this.isSlotAdditional(slot)) {
			if (!additionalHandlers.get(slot - this.getFirstAdditionalSlot()).canExtract(slot, amount, this.stacks.get(slot))) { 
				return ItemStack.EMPTY; 
			}
		}
		return super.extractItem(slot, amount, simulate);
	}

	@Override
	protected int getStackLimit(int slot, ItemStack stack) {
		int ret = super.getStackLimit(slot, stack);
		if (this.isSlotAdditional(slot)) {
			return additionalHandlers.get(slot - this.getFirstAdditionalSlot()).getStackLimit(slot, ret);
		}
		return ret;
	}	
	
	/* IItemHandlerMachine */
	
	protected ItemStack[] getStacks(int firstSlot, int lastSlot) {
		ItemStack[] ret = new ItemStack[lastSlot - firstSlot + 1];
		for (int i = firstSlot; i <= lastSlot; i++) {
			ret[i - firstSlot] = stacks.get(i).copy();
		}
		return ret;
	}
	
	public ItemStack[] getItemInput() { return getStacks(getFirstInputSlot(), getLastInputSlot()); }
	public ItemStack[] getOutput() { return getStacks(getFirstOutputSlot(), getLastOutputSlot()); }
	public ItemStack[] getItemAdditional() { return getStacks(getFirstAdditionalSlot(), getLastAdditionalSlot()); }
	
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
		super.deserializeNBT(nbt);
		if (nbt.hasKey("inputSlots")) { inputSlots = nbt.getInteger("inputSlots"); }
		if (nbt.hasKey("outputSlots")) { outputSlots = nbt.getInteger("outputSlots"); }
		if (nbt.hasKey("secondarySlots")) { secondarySlots = nbt.getInteger("secondarySlots"); }
		if (nbt.hasKey("additionalSlots")) { additionalSlots = nbt.getInteger("additionalSlots"); }
	}
}
