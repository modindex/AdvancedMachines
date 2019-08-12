package jaminv.advancedmachines.lib.inventory;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface IItemHandlerInternal extends IItemHandler, IItemHandlerModifiable {
	public ItemStack insertItem(ItemStack stack, boolean simulate);
	public ItemStack insertItemInternal(ItemStack stack, boolean simulate);
	
	public int extractItem(IItemGeneric input, boolean simulate);
	
	public default ItemStack extractItem(ItemStack stack, boolean simulate) {
		int result = extractItem(new IItemGeneric.ItemStackWrapper(stack), simulate);
		if (result == 0) { return ItemStack.EMPTY; }
		return new ItemStack(stack.getItem(), result, Items.DIAMOND.getDamage(stack), stack.getTagCompound());
	}
	
	public int extractItemInternal(IItemGeneric input, boolean simulate);
	
	public default ItemStack extractItemInternal(ItemStack stack, boolean simulate) {
		int result = extractItemInternal(new IItemGeneric.ItemStackWrapper(stack), simulate);
		if (result == 0) { return ItemStack.EMPTY; }
		return new ItemStack(stack.getItem(), result, Items.DIAMOND.getDamage(stack), stack.getTagCompound());
	}	
}
