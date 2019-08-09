package jaminv.advancedmachines.lib.machine;

import jaminv.advancedmachines.lib.energy.EnergyStorageAdvanced;
import jaminv.advancedmachines.lib.energy.IEnergyStorageInternal;
import jaminv.advancedmachines.lib.fluid.IFluidHandlerInternal;
import jaminv.advancedmachines.lib.fluid.IFluidHandlerMachine;
import jaminv.advancedmachines.lib.fluid.MachineFluidHandler;
import jaminv.advancedmachines.lib.inventory.IItemHandlerMachine;
import jaminv.advancedmachines.lib.inventory.MachineInventoryHandler;
import jaminv.advancedmachines.lib.recipe.IRecipeManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

/**
 * Storage Manager that links items and fluids together so that insertItem() and fill() routines can be aware
 * of both item and fluid inputs for recipe comparisons.
 * 
 * Energy included as well for convenience.  
 * 
 * @author JaminVanderBerg
 */
public class MachineStorage implements IItemHandler, IItemHandlerModifiable, IFluidHandler, IEnergyStorage, INBTSerializable<NBTTagCompound> {
	
	protected final IItemHandlerMachine inventory;
	protected final IFluidHandlerMachine fluidtank;
	protected final IEnergyStorageInternal energy;
	protected final IRecipeManager recipemanager;
	
	public MachineStorage(IItemHandlerMachine inventory, IFluidHandlerMachine fluidtank, IEnergyStorageInternal energy, IRecipeManager recipemanager) {
		this.inventory = inventory;
		this.fluidtank = fluidtank;
		this.energy = energy;
		this.recipemanager = recipemanager;
	}
	
	public IItemHandlerMachine getInventory() { return inventory; }
	public IFluidHandlerInternal getFluidTank() { return fluidtank; }
	public IEnergyStorageInternal getEnergy() { return energy; }
	public IRecipeManager getRecipeManager() { return recipemanager; }
	
	/* IItemHandler */
	
	@Override
	public int getSlots() { return inventory.getSlots(); }
	
	@Override
	public ItemStack getStackInSlot(int slot) { return inventory.getStackInSlot(slot); }
	
	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (!recipemanager.isItemValid(stack, inventory.getInput(), fluidtank.getInput())) {
			return ItemStack.EMPTY;
		}
		return inventory.insertItem(slot,  stack, simulate);
	}
		
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) { return inventory.extractItem(slot, amount, simulate); }

	@Override
	public int getSlotLimit(int slot) { return inventory.getSlotLimit(slot); }
	
	/* IItemHandlerModifiable */
	
	@Override
	public void setStackInSlot(int slot, ItemStack stack) { inventory.setStackInSlot(slot, stack); }

	/* IFluidHandler */
	
	@Override
	public IFluidTankProperties[] getTankProperties() { return fluidtank.getTankProperties(); }

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if (!recipemanager.isItemValid(resource, inventory.getInput(), fluidtank.getOutput())) {
			return 0;
		}
		return fluidtank.fill(resource, doFill); 
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) { return fluidtank.drain(resource, doDrain); }

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) { return fluidtank.drain(maxDrain, doDrain); }
	
	/* IEnergyStorage */
	
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) { return energy.receiveEnergy(maxReceive, simulate); }

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) { return energy.extractEnergy(maxExtract, simulate); }

	@Override
	public int getEnergyStored() { return energy.getEnergyStored(); }
	
	@Override
	public int getMaxEnergyStored() { return energy.getMaxEnergyStored(); }
	
	@Override
	public boolean canExtract() { return energy.canExtract(); }
	
	@Override
	public boolean canReceive() { return energy.canReceive(); }

	
	@Override
	public NBTTagCompound serializeNBT() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		
	}	

}
