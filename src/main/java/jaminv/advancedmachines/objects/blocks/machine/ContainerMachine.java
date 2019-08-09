package jaminv.advancedmachines.objects.blocks.machine;

import jaminv.advancedmachines.lib.recipe.IRecipeManager;
import jaminv.advancedmachines.objects.blocks.inventory.ContainerInventory;
import jaminv.advancedmachines.objects.blocks.inventory.ContainerLayout;
import jaminv.advancedmachines.objects.blocks.inventory.Layout;
import jaminv.advancedmachines.util.helper.InventoryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerMachine extends ContainerInventory {
	
	public static class InputLayout extends Layout {
		IRecipeManager recipe;
		
		public InputLayout(IRecipeManager recipe, int xpos, int ypos, int xspacing, int yspacing, int rows, int cols) {
			super(xpos, ypos, xspacing, yspacing, rows, cols);
			this.recipe = recipe;
		}
		public InputLayout(IRecipeManager recipe, int xpos, int ypos, int rows, int cols) {
			super(xpos, ypos, rows, cols);
			this.recipe = recipe;
		}
		public InputLayout(IRecipeManager recipe, int xpos, int ypos) {
			super(xpos, ypos);
			this.recipe = recipe;
		}
		
		public SlotItemHandler createSlot(IItemHandler itemHandler, int slotIndex, int x, int y) {
			return new ContainerMachine.SlotInput(recipe, itemHandler, slotIndex, x, y);
		}
	}
	
	public static class OutputLayout extends Layout {
		public OutputLayout(int xpos, int ypos, int xspacing, int yspacing, int rows, int cols) {
			super(xpos, ypos, xspacing, yspacing, rows, cols);
		}
		public OutputLayout(int xpos, int ypos, int rows, int cols) {
			super(xpos, ypos, rows, cols);
		}
		public OutputLayout(int xpos, int ypos) {
			super(xpos, ypos);
		}		

		public SlotItemHandler createSlot(IItemHandler itemHandler, int slotIndex, int x, int y) {
			return new ContainerMachine.SlotOutput(itemHandler,slotIndex, x, y);
		}
	}	
	
	public static class SlotOutput extends SlotItemHandler {

		public SlotOutput(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
			super(itemHandler, index, xPosition, yPosition);
		}

		@Override
		public boolean isItemValid(ItemStack stack) {
			return false;
		}
	}
	
	public static class SlotInput extends SlotItemHandler {
		IRecipeManager recipe;
		public SlotInput(IRecipeManager recipe, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
			super(itemHandler, index, xPosition, yPosition);
			this.recipe = recipe;
		}
		
		@Override
		public boolean isItemValid(ItemStack stack) {
			return recipe.isItemValid(stack);
		}
	}
	
	private final IRecipeManager recipeManager;
	protected final TileEntityMachineBase machineTe;

	public ContainerMachine(IInventory playerInventory, ContainerLayout layout, TileEntityMachineBase te, IRecipeManager manager) {
		super(playerInventory, layout, te);
		this.recipeManager = manager;
		this.machineTe = te;
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
		
		if (index < getTileEntity().getInventorySize()) {
			if (!this.mergeItemStack(itemstack1, machineTe.getInventorySize(), this.inventorySlots.size(), true)) {
				return ItemStack.EMPTY;
			}
		} else {
			itemstack1 = InventoryHelper.pushStack(itemstack1, machineTe.getInventory(), machineTe.getFirstInputSlot(), machineTe.getInputCount() + machineTe.getFirstInputSlot(), false);
			
			if (!recipeManager.isItemValid(itemstack1, machineTe.getInput())) {
				return ItemStack.EMPTY;
			}
			if (!this.mergeItemStack(itemstack1, 0, machineTe.getInputCount(), false)) {
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
	
	protected int[] cachedFields;
	
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		
		boolean sendAll = false;
		if (cachedFields == null) {
			cachedFields = new int[machineTe.getFieldCount()];
			sendAll = true;
		}

        for (int i = 0; i < this.listeners.size(); ++i)
        {
            IContainerListener icontainerlistener = this.listeners.get(i);
            
            for (int f = 0; f < machineTe.getFieldCount(); f++) {
            	if (sendAll || cachedFields[f] != this.machineTe.getField(f)) {
            		icontainerlistener.sendWindowProperty(this, f, this.machineTe.getField(f));
            	}
            }
        }
        
        for (int f = 0; f < this.machineTe.getFieldCount(); f++) {
        	cachedFields[f] = this.machineTe.getField(f);
        }
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int data) {
		if (id == 4) {
			int a = 0;
		}
		machineTe.setField(id, data);
	}
}
