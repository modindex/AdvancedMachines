package jaminv.advancedmachines.objects.blocks.fluid;

import jaminv.advancedmachines.objects.blocks.inventory.ContainerInventory;
import jaminv.advancedmachines.objects.blocks.inventory.Layout;
import jaminv.advancedmachines.objects.blocks.inventory.TileEntityInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerFluid extends ContainerInventory {
	
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
	
	public static class BucketInputLayout extends Layout {
		public BucketInputLayout(int xpos, int ypos) {
			super(xpos, ypos);
		}

		@Override
		public SlotItemHandler createSlot(IItemHandler itemHandler, int slotIndex, int x, int y) {
			// TODO Auto-generated method stub
			return new SlotBucketInput(itemHandler, slotIndex, x, y);
		}
	}
	
	public static class BucketOutputLayout extends Layout {
		public BucketOutputLayout(int xpos, int ypos) {
			super(xpos, ypos);
		}

		@Override
		public SlotItemHandler createSlot(IItemHandler itemHandler, int slotIndex, int x, int y) {
			// TODO Auto-generated method stub
			return new SlotBucket(itemHandler, slotIndex, x, y);
		}
	}
	
	public ContainerFluid(IInventory playerInventory, TileEntityInventory te) {
		super(playerInventory, null, te);
	}
}
