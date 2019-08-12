package jaminv.advancedmachines.objects.blocks.machine;

import jaminv.advancedmachines.lib.container.ISyncManager;
import jaminv.advancedmachines.lib.container.layout.ILayoutManager;
import jaminv.advancedmachines.lib.inventory.IItemHandlerInternal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerMachine extends Container {

	IItemHandlerInternal inventory;
	ISyncManager sync;
	
	public ContainerMachine(ILayoutManager layout, IItemHandlerInternal inventory, IInventory playerInventory, ISyncManager sync) {
		this.inventory = inventory;
		this.sync = sync;
	}	
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		Slot slot = this.inventorySlots.get(index);
		if (slot == null || !slot.getHasStack()) { return ItemStack.EMPTY; }
		ItemStack stack = slot.getStack().copy();
		ItemStack result;
		
		if (index < inventory.getSlots()) {
			result = inventory.extractItem(index, stack.getCount(), false);
		} else {
			result = inventory.insertItem(stack, false);
		}
		
		if (!result.isEmpty()) {
			stack.setCount(stack.getCount() - result.getCount());
			slot.putStack(stack);
			slot.onSlotChanged();
		}
		return stack;
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
			sync.detectAndSendChanges(this, listeners);
		}
	}
	
	public boolean canInteractWith(EntityPlayer playerIn) { return true; }	
}
