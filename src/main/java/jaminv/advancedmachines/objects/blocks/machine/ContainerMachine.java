package jaminv.advancedmachines.objects.blocks.machine;

import jaminv.advancedmachines.util.recipe.IRecipeManager;
import jaminv.advancedmachines.util.recipe.machine.PurifierManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerMachine extends Container {

	
	public class SlotInput extends SlotItemHandler {
		private IRecipeManager recipeManager;
				
		public SlotInput(IItemHandler itemHandler, int index, int xPosition, int yPosition, IRecipeManager manager) {
			super(itemHandler, index, xPosition, yPosition);
			this.recipeManager = manager;
		}

		@Override
		public boolean isItemValid(ItemStack stack) {
			return recipeManager.isItemValid(stack, null);
		}
	}	
	
	public class SlotOutput extends SlotItemHandler {

		public SlotOutput(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
			super(itemHandler, index, xPosition, yPosition);
		}

		@Override
		public boolean isItemValid(ItemStack stack) {
			return false;
		}		
	}

	protected TileEntityMachineBase te;
	protected IRecipeManager recipeManager;
	protected DialogMachineBase dialog;
	
	public ContainerMachine(IInventory playerInventory, TileEntityMachineBase te, IRecipeManager manager, DialogMachineBase dialog) {
		this.te = te;
		this.recipeManager = manager;
		this.dialog = dialog;
		
		addOwnSlots();
		addPlayerSlots(playerInventory);
	}	
	
	private void addOwnSlots() {
		IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		int slotIndex = 0;
		
		DialogMachineBase.ContainerLayout input = dialog.getInputLayout();
		DialogMachineBase.ContainerLayout output = dialog.getOutputLayout();
		DialogMachineBase.ContainerLayout secondary = dialog.getSecondaryLayout();
		
		for (int r = 0; r < input.getRows(); r++) {
			for (int c = 0; c < input.getCols(); c++) {
				int x = input.xpos + c * input.xspacing;
				int y = input.ypos + r * input.yspacing;
				addSlotToContainer(new SlotInput(itemHandler, slotIndex, x, y, recipeManager));
				slotIndex++;
			}
		}
		
		for (int r = 0; r < output.getRows(); r++) {
			for (int c = 0; c < output.getCols(); c++) {
				int x = output.xpos + c * output.xspacing;
				int y = output.ypos + r * output.yspacing;
				addSlotToContainer(new SlotOutput(itemHandler, slotIndex, x, y));
				slotIndex++;
			}
		}
		
		for (int r = 0; r < secondary.getRows(); r++) {
			for (int c = 0; c < secondary.getCols(); c++) {
				int x = secondary.xpos + c * secondary.xspacing;
				int y = secondary.ypos + r * secondary.yspacing;
				addSlotToContainer(new SlotOutput(itemHandler, slotIndex, x, y));
				slotIndex++;
			}
		}
	}
	
	private void addPlayerSlots(IInventory playerInventory) {
		DialogMachineBase.ContainerLayout inventory = dialog.getInventoryLayout();
		DialogMachineBase.ContainerLayout hotbar = dialog.getHotbarLayout();
		
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
			if (recipeManager.isItemValid(itemstack1, null)) {
				return ItemStack.EMPTY;
			}
			if (!this.mergeItemStack(itemstack1, 0, te.getInputCount(), false)) {
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
	
	
	protected int[] cachedFields;
	
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		
		boolean sendAll = false;
		if (cachedFields == null) {
			cachedFields = new int[te.getFieldCount()];
			sendAll = true;
		}

        for (int i = 0; i < this.listeners.size(); ++i)
        {
            IContainerListener icontainerlistener = this.listeners.get(i);
            
            for (int f = 0; f < this.te.getFieldCount(); f++) {
            	if (sendAll || cachedFields[f] != this.te.getField(f)) {
            		icontainerlistener.sendWindowProperty(this, f, this.te.getField(f));
            	}
            }
        }
        
        for (int f = 0; f < this.te.getFieldCount(); f++) {
        	cachedFields[f] = this.te.getField(f);
        }
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int data) {
		te.setField(id, data);
	}
}
