package jaminv.advancedmachines.lib.machine;

import org.apache.commons.lang3.ArrayUtils;

import jaminv.advancedmachines.lib.energy.EnergyObservable;
import jaminv.advancedmachines.lib.energy.EnergyStorage;
import jaminv.advancedmachines.lib.fluid.FluidHandler;
import jaminv.advancedmachines.lib.fluid.FluidObservable;
import jaminv.advancedmachines.lib.fluid.FluidTank;
import jaminv.advancedmachines.lib.inventory.ItemHandlerSeparated;
import jaminv.advancedmachines.lib.inventory.ItemObservable;
import jaminv.advancedmachines.lib.recipe.RecipeManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
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
public class MachineStorage implements StorageCombined, INBTSerializable<NBTTagCompound> {
	
	protected final ItemHandlerSeparated inventory;
	protected final FluidHandler inputTanks;
	protected final FluidHandler outputTanks;
	protected final EnergyStorage energy;
	protected final RecipeManager recipeManager;
	
	public MachineStorage(ItemHandlerSeparated inventory, FluidHandler inputTanks, FluidHandler outputTanks, 
			EnergyStorage energy, RecipeManager recipeManager) {
		
		this.inventory = inventory;
		this.inputTanks = inputTanks;
		this.outputTanks = outputTanks;
		this.energy = energy;
		this.recipeManager = recipeManager;
	}
	
	public ItemHandlerSeparated getInventory() { return inventory; }
	public FluidHandler getInputTanks() { return inputTanks; }
	public FluidHandler getOutputTanks() { return outputTanks; }
	public EnergyStorage getEnergy() { return energy; }
	public RecipeManager getRecipeManager() { return recipeManager; }
	
	/* IItemHandler */
	
	@Override public int getSlots() { return inventory.getSlots(); }
	@Override public ItemStack getStackInSlot(int slot) { return inventory.getStackInSlot(slot); }
	
	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (ItemHandlerHelper.canItemStacksStack(stack, inventory.getStackInSlot(slot))) {
			return inventory.insertItem(slot, stack, simulate);
		}
		if (inventory.isSlotInput(slot) && !recipeManager.isItemValid(stack, inventory.getItemInput(), inputTanks.getStacks())) {
			return stack;
		}
		return inventory.insertItem(slot,  stack, simulate);
	}
		
	@Override public ItemStack extractItem(int slot, int amount, boolean simulate) { return inventory.extractItem(slot, amount, simulate); }
	@Override public int getSlotLimit(int slot) { return inventory.getSlotLimit(slot); }
	
	/* IItemHandlerModifiable */

	@Override public void setStackInSlot(int slot, ItemStack stack) { inventory.setStackInSlot(slot, stack); }
	
	/* IItemObservable */

	@Override public ItemObservable addObserver(ItemObservable.IObserver observer) { return inventory.addObserver(observer); }
	
	/* IItemHandlerMachine */
	
	@Override public boolean isSlotInput(int slotIndex) { return inventory.isSlotInput(slotIndex); }
	@Override public boolean isSlotOutput(int slotIndex) { return inventory.isSlotOutput(slotIndex); }
	@Override public boolean isSlotSecondary(int slotIndex) { return inventory.isSlotSecondary(slotIndex); }
	@Override public boolean isSlotAdditional(int slotIndex) { return inventory.isSlotAdditional(slotIndex); }
	
	@Override public int getInputSlotCount() { return inventory.getInputSlotCount(); }
	@Override public int getOutputSlotCount() { return inventory.getOutputSlotCount(); }
	@Override public int getSecondarySlotCount() { return inventory.getSecondarySlotCount(); }
	@Override public int getAdditionalSlotCount() { return inventory.getAdditionalSlotCount(); }

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
	@Override public ItemStack[] getItemAdditional() { return inventory.getItemAdditional(); }
	
	/* IFluidHandler */

	@Override public IFluidTankProperties[] getTankProperties() { 
		return ArrayUtils.addAll(inputTanks.getTankProperties(), outputTanks.getTankProperties()); 
	}

	// TODO: CHECK Fluid fill check for valid recipe
	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if (resource == null) { return 0; }
		int drained = inputTanks.fillSame(resource, doFill);
		if (drained >= resource.amount) { return drained; }
		
		if (recipeManager.isFluidValid(new FluidStack(resource, resource.amount - drained),
				inventory.getItemInput(), inputTanks.getStacks())) {
			return drained + inputTanks.fill(resource, doFill);
		}
		return drained;
	}

	@Override public FluidStack drain(FluidStack resource, boolean doDrain) { return outputTanks.drain(resource, doDrain); }
	@Override public FluidStack drain(int maxDrain, boolean doDrain) { return outputTanks.drain(maxDrain, doDrain); }
	
	/* FluidHandler */
	
	@Override public int fillInternal(FluidStack resource, boolean doFill) { return outputTanks.fillInternal(resource, doFill); }
	@Override public FluidStack drainInternal(FluidStack resource, boolean doDrain) { return inputTanks.drainInternal(resource, doDrain); }
	@Override public FluidStack drainInternal(int maxDrain, boolean doDrain) { return inputTanks.drainInternal(maxDrain, doDrain); }

	@Override public FluidStack[] getStacks() { return inputTanks.getStacks(); }
	@Override public FluidTank[] getTanks() { return outputTanks.getTanks(); }
	
	@Override public int fillSame(FluidStack resource, boolean doFill) { return inputTanks.fillSame(resource, doFill); }

	@Override public boolean canFill() { return inputTanks.canFill(); }
	@Override public boolean canDrain() { return outputTanks.canDrain(); }

	/* IFluidObservable */

	@Override public void addObserver(FluidObservable.Observer observer) { 
		inputTanks.addObserver(observer);
		outputTanks.addObserver(observer);
	}

	/* IEnergyStorage */
	
	@Override public int receiveEnergy(int maxReceive, boolean simulate) { return energy.receiveEnergy(maxReceive, simulate); }
	@Override public int extractEnergy(int maxExtract, boolean simulate) { return energy.extractEnergy(maxExtract, simulate); }
	@Override public int getEnergyStored() { return energy.getEnergyStored(); }
	@Override public int getMaxEnergyStored() { return energy.getMaxEnergyStored(); }
	@Override public boolean canExtract() { return energy.canExtract(); }
	@Override public boolean canReceive() { return energy.canReceive(); }
	
	/* EnergyStorage */
	
	@Override public int receiveEnergyInternal(int maxReceive, boolean simulate) { return energy.receiveEnergyInternal(maxReceive, simulate); }
	@Override public int extractEnergyInternal(int maxExtract, boolean simulate) { return energy.extractEnergyInternal(maxExtract, simulate); }

	/* IEnergyObservable */
	
	@Override public void addObserver(EnergyObservable.IObserver observer) { energy.addObserver(observer); }

	/* INBTSerializable */

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("inventory", inventory.serializeNBT());
		nbt.setTag("inputTanks", inputTanks.serializeNBT());
		nbt.setTag("outputTanks", outputTanks.serializeNBT());
		nbt.setTag("energy", energy.serializeNBT());
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("inventory")) { inventory.deserializeNBT(nbt.getCompoundTag("inventory")); }
		if (nbt.hasKey("inputTanks")) { inputTanks.deserializeNBT(nbt.getCompoundTag("inputTanks")); }
		if (nbt.hasKey("outputTanks")) { outputTanks.deserializeNBT(nbt.getCompoundTag("outputTanks")); }
		if (nbt.hasKey("energy")) { energy.deserializeNBT(nbt.getCompoundTag("energy")); }
	}	

}
