package jaminv.advancedmachines.objects.blocks.machine;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public abstract class ContainerMachineBase extends Container {
	final protected int SLOT_X_SPACING = 18;
	final protected int SLOT_Y_SPACING = 18;
	final protected int BORDER_X_SPACING = 8;
	
	public class SlotOutput extends SlotItemHandler {

		public SlotOutput(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
			super(itemHandler, index, xPosition, yPosition);
		}

		@Override
		public boolean isItemValid(ItemStack stack) {
			return false;
		}		
	}
}
