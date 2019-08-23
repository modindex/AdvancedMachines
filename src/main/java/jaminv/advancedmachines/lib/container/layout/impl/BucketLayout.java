package jaminv.advancedmachines.lib.container.layout.impl;

import jaminv.advancedmachines.lib.container.layout.ItemLayoutGrid;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class BucketLayout extends ItemLayoutGrid {
	
	public static class SlotBucket extends SlotItemHandler {
		public SlotBucket(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
			super(itemHandler, index, xPosition, yPosition);
		}

		@Override
		public boolean isItemValid(ItemStack stack) {
			return FluidUtil.getFluidHandler(stack) != null;
		}

		@Override
		public int getSlotStackLimit() {
			return 1;
		}			
	}
		
	public BucketLayout(int xpos, int ypos) {
		super(xpos, ypos);
	}

	@Override
	public SlotItemHandler createSlot(IItemHandler itemHandler, int slotIndex, int x, int y) {
		return new SlotBucket(itemHandler, slotIndex, x, y);
	}
}
