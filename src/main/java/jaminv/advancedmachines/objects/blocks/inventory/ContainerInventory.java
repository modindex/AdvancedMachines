package jaminv.advancedmachines.objects.blocks.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ContainerInventory extends Container {

	private TileEntityInventory te;
	private IInventory playerInventory;
	
	public ContainerInventory(IInventory playerInventory, ContainerLayout layout, TileEntityInventory te) {
		this.te = te;
		this.container_layout = layout;
		
		addOwnSlots();
		addPlayerSlots(playerInventory);
	}
	
	protected TileEntityInventory getTileEntity() { return te; }
	
	ContainerLayout container_layout = null;
	
	protected ContainerLayout getLayout() { return container_layout; }
	
	private void addOwnSlots() {
		IItemHandler itemHandler = this.te.getInventory();
		int slotIndex = 0;
		
		for (Layout layout : container_layout.getLayouts()) {
			for (int r = 0; r < layout.getRows(); r++) {
				for (int c = 0; c < layout.getCols(); c++) {
					int x = layout.getXPos() + c * layout.getXSpacing();
					int y = layout.getYPos() + r * layout.getYSpacing();
					addSlotToContainer(layout.createSlot(itemHandler, slotIndex, x, y));
					slotIndex++;
				}
			}
		}
	}
	
	private void addPlayerSlots(IInventory playerInventory) {
		int slotIndex = 9;
		Layout inventory = container_layout.getInventoryLayout();
		Layout hotbar = container_layout.getHotbarLayout();		
		
		if (inventory != null) {
			for (int r = 0; r < inventory.getRows(); r++) {
				for (int c = 0; c < inventory.getCols(); c++) {
					int x = inventory.getXPos() + c * inventory.getXSpacing();
					int y = inventory.getYPos() + r * inventory.getYSpacing();
					this.addSlotToContainer(new Slot(playerInventory, slotIndex, x, y));
					slotIndex++;
				}
			}
		}

		if (hotbar != null) {
			for (int r = 0; r < hotbar.getRows(); r++) {
				for (int c = 0; c < hotbar.getCols(); c++) {
					int x = hotbar.getXPos() + c * hotbar.getXSpacing();
					int y = hotbar.getYPos() + r * hotbar.getYSpacing();
					this.addSlotToContainer(new Slot(playerInventory, c, x, y));
				}
			}
		}
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
		
		if (index < te.getInventorySize()) {
			if (!this.mergeItemStack(itemstack1, te.getInventorySize(), this.inventorySlots.size(), true)) {
				return ItemStack.EMPTY;
			}
		} else {
			if (!this.mergeItemStack(itemstack1, 0, te.getInventorySize(), false)) {
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
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return te.canInteractWith(playerIn);
	}
}
