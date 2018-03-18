package jaminv.advancedmachines.util.helper;

import jaminv.advancedmachines.util.recipe.IRecipeManager;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class InventoryHelper {

	/**
	 * Push items into inventory
	 * 
	 * @param item
	 * @param inv
	 * @return Number of items pushed
	 */
	public static ItemStack pushStack(ItemStack stack, ItemStackHandler inv, int startIndex, int endIndex) {
        boolean flag = false;
        int i = startIndex;

        if (stack.isStackable()) {
            while (!stack.isEmpty()) {
                if (i >= endIndex) { break; }

                ItemStack itemstack = inv.getStackInSlot(i);

                if (InventoryHelper.canStack(stack, itemstack)) {
                    int j = itemstack.getCount() + stack.getCount();
                    int maxSize = stack.getMaxStackSize();

                    if (j <= maxSize) {
                        stack.setCount(0);
                        itemstack.setCount(j);
                        flag = true;
                    } else if (itemstack.getCount() < maxSize) {
                        stack.shrink(maxSize - itemstack.getCount());
                        itemstack.setCount(maxSize);
                        flag = true;
                    }
                }
                i++;
            }
        }

        if (!stack.isEmpty()) {
            i = startIndex;
            while (true) {
        		if (i >= endIndex) { break; }

                ItemStack itemstack = inv.getStackInSlot(i);

                if (itemstack.isEmpty()) {
                	stack = inv.insertItem(i, stack, false);
                    flag = true;
                    break;
                }

                i++;
            }
        }

        return stack;		
	}
	
	public static ItemStack pushStack(ItemStack stack, ItemStackHandler inv) {
		return InventoryHelper.pushStack(stack, inv, 0, inv.getSlots());
	}
	
	/**
	 * Determines if two items can be stacked together
	 * 
	 * Source and dest are technically different, in that dest can be EMPTY, but source cannot.
	 * @param source
	 * @param dest
	 * @return true if items are stackable, false if they are not
	 */
	public static boolean canStack(ItemStack source, ItemStack dest) {
		return !source.isEmpty() 
			&& source.getItem() == dest.getItem() 
			&& (!source.getHasSubtypes() || source.getMetadata() == dest.getMetadata()) 
			&& ItemStack.areItemStackShareTagsEqual(source, dest)
			&& (dest.isEmpty() || dest.isEmpty());
	}
	
}
