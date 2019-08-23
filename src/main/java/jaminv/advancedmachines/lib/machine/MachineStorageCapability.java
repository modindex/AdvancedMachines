package jaminv.advancedmachines.lib.machine;

import jaminv.advancedmachines.lib.energy.IEnergyStorageAdvanced;
import jaminv.advancedmachines.lib.fluid.IFluidHandlerAdvanced;
import jaminv.advancedmachines.lib.inventory.IItemHandlerMachine;
import jaminv.advancedmachines.lib.recipe.IRecipeManager;
import net.minecraft.item.ItemStack;

/**
 * Expose Machine Storage Capabilities
 * 
 * This is a thin wrapper that is tightly coupled to `MachineStorage` that's used for exposing
 * capabilities. It blocks a few item transfer capabilities that you wouldn't want logistic devices
 * having access to, but players should be allowed to do. This class prevents logistics systems from
 * removing items from input slots and from adding items to output slots.
 * 
 * Although I don't particularly like tightly coupling this object, doing so prevented a lot of 
 * duplicate code (mostly just wrapper functions, but there are a lot of them).
 *
 * @author jamin
 */
public class MachineStorageCapability extends MachineStorage {

	protected MachineStorage storage;
	public MachineStorageCapability(MachineStorage storage) {
		super(storage.getInventory(), storage.getInputTanks(), storage.getOutputTanks(), storage, storage.recipeManager);
		this.storage = storage;
	}
	
	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (inventory.isSlotOutput(slot) || inventory.isSlotSecondary(slot)) { return stack; }
		return super.insertItem(slot, stack, simulate);
	}

	@Override 
	public ItemStack extractItem(int slot, int amount, boolean simulate) { 
		if (inventory.isSlotInput(slot)) { return ItemStack.EMPTY; }
		return inventory.extractItem(slot, amount, simulate); 
	}
}
