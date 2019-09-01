package jaminv.advancedmachines.lib.container;

import jaminv.advancedmachines.lib.container.layout.ILayoutManager;
import jaminv.advancedmachines.lib.container.layout.ILayoutUser;
import jaminv.advancedmachines.lib.inventory.IItemHandlerMachine;
import jaminv.advancedmachines.lib.inventory.InventoryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerMachine extends Container implements ILayoutUser {

	IItemHandlerMachine inventory;
	IInventory playerInventory;
	ISyncManager sync;
	ILayoutManager layout;
	
	public ContainerMachine(ILayoutManager layout, IItemHandlerMachine inventory, IInventory playerInventory, ISyncManager sync) {
		this.inventory = inventory;
		this.playerInventory = playerInventory;
		this.sync = sync;
		this.layout = layout;
				
		layout.addInventorySlots(this, inventory);
		layout.addPlayerSlots(this, playerInventory);
	}
	
	@Override
	public Slot addSlot(Slot slotIn) {
		return addSlotToContainer(slotIn);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		Slot slot = this.inventorySlots.get(index);
		if (slot == null || !slot.getHasStack()) { return ItemStack.EMPTY; }
		ItemStack stack = slot.getStack().copy();
		ItemStack result;
		
		if (index < inventory.getSlots()) {
			result = inventory.extractItem(index, stack.getCount(), true);
			if (!result.isEmpty()) {
				if (mergeItemStack(result.copy(), layout.getInventorySlots(), layout.getInventorySlots() + layout.getPlayerSlots(), true)) {
					inventory.extractItem(index, result.getCount(), false);
				}
			}
		} else {
			result = InventoryHelper.pushStackToInput(stack, inventory, false);
			result = InventoryHelper.pushStackToAdditional(result, inventory, false);
			slot.putStack(result);
			slot.onSlotChanged();
		}
		if (result.getCount() == stack.getCount()) { return ItemStack.EMPTY; }
		
		return result;
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (sync != null) {
			sync.detectAndSendChanges(this, listeners);
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int data) {
		if (sync != null) {
			sync.updateProgressBar(id, data);
		}
	}
	
	public boolean canInteractWith(EntityPlayer playerIn) { return true; }	
}
