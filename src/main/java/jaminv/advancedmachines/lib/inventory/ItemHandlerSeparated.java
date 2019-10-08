package jaminv.advancedmachines.lib.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface ItemHandlerSeparated extends IItemHandler, IItemHandlerModifiable, ItemObservable, INBTSerializable<NBTTagCompound> {
	public boolean isSlotInput(int slotIndex);
	public boolean isSlotOutput(int slotIndex);
	public boolean isSlotSecondary(int slotIndex);
	public boolean isSlotAdditional(int slotIndex);
	
	public int getInputSlotCount();
	public int getOutputSlotCount();
	public int getSecondarySlotCount();
	public int getAdditionalSlotCount();	
	
	public int getFirstInputSlot();
	public int getFirstOutputSlot();
	public int getFirstSecondarySlot();
	public int getFirstAdditionalSlot();

	public int getLastInputSlot();
	public int getLastOutputSlot();
	public int getLastSecondarySlot();
	public int getLastAdditionalSlot();	
	
	/** Return copies of the stacks in input slots */
	public ItemStack[] getItemInput();
	/** Return copies of the stacks in output slots */
	public ItemStack[] getOutput();
	/** Return copies of the stacks in additional slots */
	public ItemStack[] getItemAdditional();
}
