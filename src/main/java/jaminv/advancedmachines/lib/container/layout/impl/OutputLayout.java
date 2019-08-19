package jaminv.advancedmachines.lib.container.layout.impl;

import jaminv.advancedmachines.lib.container.ContainerMachine;
import jaminv.advancedmachines.lib.container.layout.Layout;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class OutputLayout extends Layout {
	public static class SlotOutput extends SlotItemHandler {

		public SlotOutput(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
			super(itemHandler, index, xPosition, yPosition);
		}

		@Override
		public boolean isItemValid(ItemStack stack) {
			return false;
		}
	}	
	
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
		return new SlotOutput(itemHandler,slotIndex, x, y);
	}	
}	


