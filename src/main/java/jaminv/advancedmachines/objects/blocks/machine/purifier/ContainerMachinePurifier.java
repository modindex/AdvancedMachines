package jaminv.advancedmachines.objects.blocks.machine.purifier;

import jaminv.advancedmachines.objects.blocks.machine.ContainerMachineBase;
import jaminv.advancedmachines.util.managers.machine.PurifierManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerMachinePurifier extends ContainerMachineBase {

	private TileEntityMachinePurifier te;
	
	final int INPUT_X_POS = 53;
	final int INPUT_Y_POS = 37;
	final int OUTPUT_X_POS = 107;
	final int OUTPUT_Y_POS = 37;
	
	final int SECONDARY_X_POS = 8;
	final int SECONDARY_Y_POS = 82;
	
	final int INVENTORY_X_POS = 8;
	final int INVENTORY_Y_POS = 111;
	final int HOTBAR_X_POS = 8;
	final int HOTBAR_Y_POS = 169;
	
	public ContainerMachinePurifier(IInventory playerInventory, TileEntityMachinePurifier te) {
		this.te = te;
		
		addOwnSlots();
		addPlayerSlots(playerInventory);
	}
	
	private void addPlayerSlots(IInventory playerInventory) {
		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < 9; ++col) {
				int x = INVENTORY_X_POS + col * SLOT_X_SPACING;
				int y = INVENTORY_Y_POS + row * SLOT_Y_SPACING;
				this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 10, x, y));
			}
		}
		
		for (int row = 0; row < 9; ++row) {
			int x = HOTBAR_X_POS + row * 18;
			int y = HOTBAR_Y_POS;
			this.addSlotToContainer(new Slot(playerInventory, row, x, y));
		}
	}
	
	private void addOwnSlots() {
		IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		int x = 9;
		int y = 6;
		
		int slotIndex = 0;

		addSlotToContainer(new SlotInput(itemHandler, slotIndex, INPUT_X_POS, INPUT_Y_POS));
		slotIndex++;
		addSlotToContainer(new SlotOutput(itemHandler, slotIndex, OUTPUT_X_POS, OUTPUT_Y_POS));
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = null;
		Slot slot = this.inventorySlots.get(index);
		
		if (slot == null || !slot.getHasStack()) {
			return ItemStack.EMPTY;
		}

		ItemStack itemstack1 = slot.getStack();
		itemstack = itemstack1.copy();
		
		if (index < TileEntityMachinePurifier.SIZE) {
			if (!this.mergeItemStack(itemstack1, 2, this.inventorySlots.size(), true)) {
				return ItemStack.EMPTY;
			}
		} else {
			if (PurifierManager.getRecipe(itemstack1) == null) {
				return ItemStack.EMPTY;
			}
			if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
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
	
	public class SlotInput extends SlotItemHandler {
		
		public SlotInput(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
			super(itemHandler, index, xPosition, yPosition);
		}

		@Override
		public boolean isItemValid(ItemStack stack) {
			return PurifierManager.getRecipe(stack) != null;
		}
	}	
}
