package jaminv.advancedmachines.lib.inventory;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface IItemHandlerInternal extends IItemHandler, IItemHandlerModifiable {
	public ItemStack insertItem(ItemStack stack, boolean simulate);
	public ItemStack insertItemInternal(ItemStack stack, boolean simulate);
	
	public int extractItem(IItemGeneric input, int count, boolean simulate);
	
	public default ItemStack extractInput(ItemStack stack, boolean simulate) {
		int result = extractItem(new IItemGeneric.ItemStackWrapper(stack), stack.getCount(), simulate);
		if (result == 0) { return ItemStack.EMPTY; }
		return new ItemStack(stack.getItem(), result, Items.DIAMOND.getDamage(stack), stack.getTagCompound());
	}
	
	public int extractItemInternal(IItemGeneric input, int count, boolean simulate);
	
	public default ItemStack extractItemInternal(ItemStack stack, boolean simulate) {
		int result = extractItemInternal(new IItemGeneric.ItemStackWrapper(stack), stack.getCount(), simulate);
		if (result == 0) { return ItemStack.EMPTY; }
		return new ItemStack(stack.getItem(), result, Items.DIAMOND.getDamage(stack), stack.getTagCompound());
	}	
}
