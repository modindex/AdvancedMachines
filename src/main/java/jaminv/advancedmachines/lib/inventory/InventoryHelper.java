package jaminv.advancedmachines.lib.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class InventoryHelper {
	
	/** Push item stack (if possible) into a slot range within an inventory. 
	 * @return ItemStack with remaining items. */
	public static ItemStack pushStack(ItemStack stack, IItemHandler inv, int firstSlot, int lastSlot, boolean simulate) {
		ItemStack ret = stack; // We don't need a copy because insertItem() will copy
		
		for (int i = firstSlot; i <= lastSlot; i++) {
			ret = inv.insertItem(i, ret, simulate);
		}
		
		return ret;
	}
	
	/** Push item stack (if possible) into input slots of an inventory.
	 * Convenience method of `pushStack`.
	 * @return ItemStack with remaining items. */
	public static ItemStack pushStackToInput(ItemStack stack, ItemHandlerSeparated inv, boolean simulate) {
		return pushStack(stack, inv, inv.getFirstInputSlot(), inv.getLastInputSlot(), simulate);
	}
	
	/** Push item stack (if possible) into additional slots of an inventory.
	 * Convenience method of `pushStack`.
	 * @return ItemStack with remaining items. */
	public static ItemStack pushStackToAdditional(ItemStack stack, ItemHandlerSeparated inv, boolean simulate) {
		return pushStack(stack, inv, inv.getFirstAdditionalSlot(), inv.getLastAdditionalSlot(), simulate);
	}
	
	/**
	 * Move items from a single slot within an inventory (if possible) into another inventory.
	 * Source inventory items are extracted.
	 * @return true if any items were moved
	 */
	public static boolean moveInventorySlot(IItemHandler source, int slotIndex, IItemHandler dest, int firstSlot, int lastSlot, boolean simulate) {
		ItemStack stack = source.getStackInSlot(slotIndex);
		if (stack.isEmpty()) { return false; }
		
		ItemStack result = pushStack(stack, dest, firstSlot, lastSlot, simulate);
		
		int change = stack.getCount() - result.getCount();
		if (change == 0) { return false; }

		ItemStack ext = source.extractItem(slotIndex, change, simulate);
		if (ext.isEmpty() || ext.getCount() != change) { return false; }
		return true;
	}
	
	/**
	 * Move all items from a slot range within an inventory (if possible) into a slot range in another inventory.
	 * Source inventory items are extracted.
	 * @return true if any items were moved
	 */
	public static boolean moveAll(IItemHandler source, int firstSourceSlot, int lastSourceSlot, IItemHandler dest, int firstDestSlot, int lastDestSlot) {
		boolean ret = false;
		for (int i = firstSourceSlot; i <= lastSourceSlot; i++) {
			if (moveInventorySlot(source, i, dest, firstDestSlot, lastDestSlot, true)) {
				moveInventorySlot(source, i, dest, firstDestSlot, lastDestSlot, false);
				ret = true;
			}
		}
		return ret;
	}
	
	/** Move all items (if possible) from inventory into input slots of another inventory .
	 * Source inventory items are extracted.
	 * Convenience method of `moveAll`. 
	 * @return true if any items were moved */
	public static boolean moveAllToInput(IItemHandler source, ItemHandlerSeparated dest) {
		return moveAll(source, 0, source.getSlots()-1, dest, dest.getFirstInputSlot(), dest.getLastInputSlot());
	}
	
	/** Move output slot items (if possible) into another inventory.
	 * Source inventory items are extracted.
	 * Convenience method of `moveAll`. 
	 * @return true if any items were moved */
	public static boolean moveOutputToAll(ItemHandlerSeparated source, IItemHandler dest) {
		return moveAll(source, source.getFirstOutputSlot(), source.getLastOutputSlot(), dest, 0, dest.getSlots()-1);
	}
}
