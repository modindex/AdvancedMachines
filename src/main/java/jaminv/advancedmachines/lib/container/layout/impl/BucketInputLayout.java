package jaminv.advancedmachines.lib.container.layout.impl;

import jaminv.advancedmachines.lib.container.layout.Layout;
import jaminv.advancedmachines.lib.container.layout.impl.BucketOutputLayout.SlotBucket;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class BucketInputLayout extends Layout {
	
	public static class SlotBucketInput extends SlotBucket {

		public SlotBucketInput(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
			super(itemHandler, index, xPosition, yPosition);
		}
		
		@Override
		public boolean isItemValid(ItemStack stack) {
			if (!super.isItemValid(stack)) { return false; }
			return FluidUtil.getFluidContained(stack) != null;
		}		
	}	
	
	public BucketInputLayout(int xpos, int ypos) {
		super(xpos, ypos);
	}

	@Override
	public SlotItemHandler createSlot(IItemHandler itemHandler, int slotIndex, int x, int y) {
		return new SlotBucketInput(itemHandler, slotIndex, x, y);
	}
}
