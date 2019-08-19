package jaminv.advancedmachines.lib.machine;

import jaminv.advancedmachines.lib.energy.IEnergyObservable;
import jaminv.advancedmachines.lib.energy.IEnergyStorageAdvanced;
import jaminv.advancedmachines.lib.fluid.IFluidHandlerMachine;
import jaminv.advancedmachines.lib.fluid.IFluidObservable;
import jaminv.advancedmachines.lib.fluid.IFluidTankInternal;
import jaminv.advancedmachines.lib.inventory.IItemHandlerMachine;
import jaminv.advancedmachines.lib.inventory.IItemObservable;
import jaminv.advancedmachines.lib.inventory.IItemObservable.IObserver;
import jaminv.advancedmachines.lib.recipe.IRecipeManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.ItemHandlerHelper;

/**
 * Storage Manager that links items and fluids together so that insertItem() and fill() routines can be aware
 * of both item and fluid inputs for recipe comparisons.
 * 
 * Energy included as well for convenience.  
 * 
 * @author Jamin VanderBerg
 */
public class MachineStorage implements IMachineStorage, INBTSerializable<NBTTagCompound> {
	
	protected final IItemHandlerMachine inventory;
	protected final IFluidHandlerMachine fluidtank;
	protected final IEnergyStorageAdvanced energy;
	protected final IRecipeManager recipemanager;
	
	public MachineStorage(IItemHandlerMachine inventory, IFluidHandlerMachine fluidtank, IEnergyStorageAdvanced energy, IRecipeManager recipemanager) {
		this.inventory = inventory;
		this.fluidtank = fluidtank;
		this.energy = energy;
		this.recipemanager = recipemanager;
	}
	
	public IItemHandlerMachine getInventory() { return inventory; }
	public IFluidHandlerMachine getFluidTank() { return fluidtank; }
	public IEnergyStorageAdvanced getEnergy() { return energy; }
	public IRecipeManager getRecipeManager() { return recipemanager; }
	
	/* IItemHandler */
	
	@Override public int getSlots() { return inventory.getSlots(); }
	@Override public ItemStack getStackInSlot(int slot) { return inventory.getStackInSlot(slot); }
	
	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (ItemHandlerHelper.canItemStacksStack(stack, inventory.getStackInSlot(slot))) {
			return inventory.insertItem(slot, stack, simulate);
		}
		if (inventory.isSlotInput(slot) && !recipemanager.isItemValid(stack, inventory.getItemInput(), fluidtank.getFluidInput())) {
			return stack;
		}
		return inventory.insertItem(slot,  stack, simulate);
	}
		
	@Override public ItemStack extractItem(int slot, int amount, boolean simulate) { return inventory.extractItem(slot, amount, simulate); }
	@Override public int getSlotLimit(int slot) { return inventory.getSlotLimit(slot); }
	
	/* IItemHandlerModifiable */

	@Override public void setStackInSlot(int slot, ItemStack stack) { inventory.setStackInSlot(slot, stack); }
	
	/* IItemObservable */

	@Override public IItemObservable addObserver(IItemObservable.IObserver observer) { return inventory.addObserver(observer); }
	
	/* IItemHandlerMachine */
	
	@Override public boolean isSlotInput(int slotIndex) { return inventory.isSlotInput(slotIndex); }
	@Override public boolean isSlotOutput(int slotIndex) { return inventory.isSlotOutput(slotIndex); }
	@Override public boolean isSlotSecondary(int slotIndex) { return inventory.isSlotSecondary(slotIndex); }
	@Override public boolean isSlotAdditional(int slotIndex) { return inventory.isSlotAdditional(slotIndex); }

	@Override public int getFirstInputSlot() { return inventory.getFirstInputSlot(); }
	@Override public int getFirstOutputSlot() { return inventory.getFirstOutputSlot(); }
	@Override public int getFirstSecondarySlot() { return inventory.getFirstSecondarySlot(); }
	@Override public int getFirstAdditionalSlot() { return inventory.getFirstAdditionalSlot(); }
	
	@Override public int getLastInputSlot() { return inventory.getLastInputSlot(); }
	@Override public int getLastOutputSlot() { return inventory.getLastOutputSlot(); }
	@Override public int getLastSecondarySlot() { return inventory.getLastSecondarySlot(); }
	@Override public int getLastAdditionalSlot() { return inventory.getLastAdditionalSlot(); }

	@Override public ItemStack[] getItemInput() { return inventory.getItemInput(); }
	@Override public ItemStack[] getOutput() { return inventory.getOutput(); }
	
	/* IFluidHandler */

	@Override public IFluidTankProperties[] getTankProperties() { return fluidtank.getTankProperties(); }

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if (!recipemanager.isFluidValid(resource, inventory.getItemInput(), fluidtank.getFluidInput())) {
			return 0;
		}
		return fluidtank.fill(resource, doFill); 
	}

	@Override public FluidStack drain(FluidStack resource, boolean doDrain) { return fluidtank.drain(resource, doDrain); }
	@Override public FluidStack drain(int maxDrain, boolean doDrain) { return fluidtank.drain(maxDrain, doDrain); }
	
	/* IFluidHandlerInternal */
	
	@Override public int fillInternal(FluidStack resource, boolean doFill) { return fluidtank.fillInternal(resource, doFill); }
	@Override public FluidStack drainInternal(FluidStack resource, boolean doDrain) { return fluidtank.drainInternal(resource, doDrain); }
	@Override public FluidStack drainInternal(int maxDrain, boolean doDrain) { return fluidtank.drainInternal(maxDrain, doDrain); }

	@Override public int getTankCount() { return fluidtank.getTankCount(); }
	@Override public IFluidTank getTank(int index) { return fluidtank.getTank(index); }

	/* IFluidObservable */
	
	@Override public void addObserver(IFluidObservable.IObserver observer) { fluidtank.addObserver(observer); }

	/* IFluidHandlerMachine */
	
	@Override public FluidStack[] getFluidInput() { return fluidtank.getFluidInput(); }
	@Override public IFluidTankInternal[] getFluidOutput() { return fluidtank.getFluidOutput(); }
	@Override public void setFluidCapacity(int capacity) { fluidtank.setFluidCapacity(capacity); }

	/* IEnergyStorage */
	
	@Override public int receiveEnergy(int maxReceive, boolean simulate) { return energy.receiveEnergy(maxReceive, simulate); }
	@Override public int extractEnergy(int maxExtract, boolean simulate) { return energy.extractEnergy(maxExtract, simulate); }
	@Override public int getEnergyStored() { return energy.getEnergyStored(); }
	@Override public int getMaxEnergyStored() { return energy.getMaxEnergyStored(); }
	@Override public boolean canExtract() { return energy.canExtract(); }
	@Override public boolean canReceive() { return energy.canReceive(); }
	
	/* IEnergyStorageInternal */
	
	@Override public int receiveEnergyInternal(int maxReceive, boolean simulate) { return energy.receiveEnergyInternal(maxReceive, simulate); }
	@Override public int extractEnergyInternal(int maxExtract, boolean simulate) { return energy.extractEnergyInternal(maxExtract, simulate); }

	/* IEnergyObservable */
	
	@Override public void addObserver(IEnergyObservable.IObserver observer) { energy.addObserver(observer); }

	/* IEnergyStorageAdvanced */
	
	@Override public void setEnergyCapacity(int capacity) { energy.setEnergyCapacity(capacity); }
	@Override public void setEnergy(int capacity) { energy.setEnergy(capacity); }	

	/* INBTSerializable */

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("inventory", inventory.serializeNBT());
		nbt.setTag("fluidtank", fluidtank.serializeNBT());
		nbt.setTag("energy", energy.serializeNBT());
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("inventory")) { inventory.deserializeNBT(nbt.getCompoundTag("inventory")); }
		if (nbt.hasKey("fluidtank")) { fluidtank.deserializeNBT(nbt.getCompoundTag("fluidtank")); }
		if (nbt.hasKey("energy")) { energy.deserializeNBT(nbt.getCompoundTag("energy")); }
	}	

}
