package jaminv.advancedmachines.lib.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface IItemHandlerMachine extends IItemHandlerInternal, IItemObservable, INBTSerializable<NBTTagCompound> {
	/** Insert items specifically into the secondary output slots */
	public ItemStack insertSecondary(ItemStack stack, boolean simulate);
	
	/** Return copies of the stacks in input slots */
	public ItemStack[] getInput();
	/** Return copies of the stacks in output slots */
	public ItemStack[] getOutput();
}
