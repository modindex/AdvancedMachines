package jaminv.advancedmachines.objects.blocks.inventory;

import jaminv.advancedmachines.objects.blocks.inventory.DialogInventory.ContainerLayout;
import jaminv.advancedmachines.util.recipe.IRecipeManager;
import jaminv.advancedmachines.util.recipe.machine.purifier.PurifierManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerInventory extends Container {

	protected DialogInventory dialog;
	protected TileEntityInventory te;
	
	public ContainerInventory(IInventory playerInventory, TileEntityInventory te, DialogInventory dialog) {
		this.te = te;
		this.dialog = dialog;
		
		addOwnSlots();
		addPlayerSlots(playerInventory);
	}
	
	public static SlotItemHandler createSlot(IItemHandler itemHandler, int slotIndex, int x, int y) {
		return new SlotItemHandler(itemHandler, slotIndex, x, y);
	}
	
	private void addOwnSlots() {
		IItemHandler itemHandler = this.te.getInventory();
		int slotIndex = 0;
		
		ContainerLayout[] layouts = dialog.getLayouts();
		for (int i = 0; i < layouts.length; i++) {
			ContainerLayout layout = layouts[i];
			
			for (int r = 0; r < layout.getRows(); r++) {
				for (int c = 0; c < layout.getCols(); c++) {
					int x = layout.xpos + c * layout.xspacing;
					int y = layout.ypos + r * layout.yspacing;
					addSlotToContainer(layout.createSlot(itemHandler, slotIndex, x, y));
					slotIndex++;
				}
			}
		}
	}
	
	private void addPlayerSlots(IInventory playerInventory) {
		DialogInventory.ContainerLayout inventory = dialog.getInventoryLayout();
		DialogInventory.ContainerLayout hotbar = dialog.getHotbarLayout();
		
		int slotIndex = 9;		
		for (int r = 0; r < inventory.getRows(); r++) {
			for (int c = 0; c < inventory.getCols(); c++) {
				int x = inventory.xpos + c * inventory.xspacing;
				int y = inventory.ypos + r * inventory.yspacing;
				this.addSlotToContainer(new Slot(playerInventory, slotIndex, x, y));
				slotIndex++;
			}
		}

		for (int r = 0; r < hotbar.getRows(); r++) {
			for (int c = 0; c < hotbar.getCols(); c++) {
				int x = hotbar.xpos + c * hotbar.xspacing;
				int y = hotbar.ypos + r * hotbar.yspacing;
				this.addSlotToContainer(new Slot(playerInventory, c, x, y));
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
