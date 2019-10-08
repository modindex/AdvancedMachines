package jaminv.advancedmachines.lib.container;

import jaminv.advancedmachines.lib.container.layout.ILayoutManager;
import jaminv.advancedmachines.lib.container.layout.ILayoutUser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ContainerInventory extends Container implements ILayoutUser {

	IItemHandler inventory;
	SyncManager sync;
	
	public ContainerInventory(ILayoutManager layout, IItemHandler inventory, IInventory playerInventory) {
		this.inventory = inventory;
		layout.addInventorySlots(this, inventory);
		layout.addPlayerSlots(this, playerInventory);
	}	
	
	@Override
	public Slot addSlot(Slot slotIn) {
		return addSlotToContainer(slotIn);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		int slotIndex = 0;
		
		ItemStack itemstack = null;
		Slot slot = this.inventorySlots.get(index);
		
		if (slot == null || !slot.getHasStack()) {
			return ItemStack.EMPTY;
		}

		ItemStack itemstack1 = slot.getStack();
		itemstack = itemstack1.copy();
		
		if (index < inventory.getSlots()) {
			if (!this.mergeItemStack(itemstack1, inventory.getSlots(), this.inventorySlots.size(), true)) {
				return ItemStack.EMPTY;
			}
		} else {
			if (!this.mergeItemStack(itemstack1, 0, inventory.getSlots(), false)) {
				return ItemStack.EMPTY;
			}
		}
		
		if (itemstack1.isEmpty()) {
			slot.putStack(ItemStack.EMPTY);
		} else {
			slot.onSlotChanged();
		}
		return itemstack;
	}

	public boolean canInteractWith(EntityPlayer playerIn) { return true; }	
}
