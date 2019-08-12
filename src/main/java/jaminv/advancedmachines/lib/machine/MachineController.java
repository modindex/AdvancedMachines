 package jaminv.advancedmachines.lib.machine;

import java.util.ArrayList;
import java.util.List;

import jaminv.advancedmachines.lib.container.ISyncSubject;
import jaminv.advancedmachines.lib.energy.IEnergyObservable;
import jaminv.advancedmachines.lib.energy.IEnergyStorageAdvanced;
import jaminv.advancedmachines.lib.energy.IEnergyStorageInternal;
import jaminv.advancedmachines.lib.fluid.IFluidHandlerInternal;
import jaminv.advancedmachines.lib.fluid.IFluidHandlerMachine;
import jaminv.advancedmachines.lib.fluid.IFluidObservable;
import jaminv.advancedmachines.lib.inventory.IItemGeneric;
import jaminv.advancedmachines.lib.inventory.IItemHandlerInternal;
import jaminv.advancedmachines.lib.inventory.IItemHandlerMachine;
import jaminv.advancedmachines.lib.inventory.IItemObservable;
import jaminv.advancedmachines.lib.machine.IRedstoneControlled.RedstoneState;
import jaminv.advancedmachines.lib.recipe.IRecipe;
import jaminv.advancedmachines.lib.recipe.IRecipeManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;

public class MachineController implements IMachineController, IItemObservable.IObserver, IFluidObservable.IObserver, IEnergyObservable.IObserver, INBTSerializable<NBTTagCompound>, ISyncSubject {
	
	protected final IItemHandlerMachine inventory;
	protected final IFluidHandlerMachine fluidtank;
	protected final IEnergyStorageAdvanced energy;
	protected final IRecipeManager recipemanager;
	protected final IMachineTE te;
	protected final boolean isClient;
	
	public MachineController(IItemHandlerMachine inventory, IFluidHandlerMachine fluidtank, IEnergyStorageAdvanced energy, IRecipeManager recipemanager, IMachineTE te, boolean isClient) {
		this.inventory = inventory;
		this.fluidtank = fluidtank;
		this.energy = energy;
		this.recipemanager = recipemanager;
		this.te = te;
		this.isClient = isClient;
	}
	
	public MachineController(MachineStorage storage, IMachineTE te, boolean isClient) {
		inventory = storage.inventory;
		fluidtank = storage.fluidtank;
		energy = storage.energy;
		recipemanager = storage.recipemanager;
		this.te = te;
		this.isClient = isClient;
	}
	
	public IItemHandlerInternal getInventory() { return inventory; }
	public IFluidHandlerInternal getFluidTank() { return fluidtank; }
	public IEnergyStorageInternal getEnergy() { return energy; }
	public IRecipeManager getRecipeManager() { return recipemanager; }	
	
	private List<IObserver> observers = new ArrayList<>();
	public IMachineController addObserver(IObserver obv) { observers.add(obv); return this; }
	
	/* Base Processing Data */
	
	private boolean sleep = false;
	/** Call to restart tick updates when a tile entity's state changes. */
	public void wake() { sleep = false; }
	
	private IRecipe lastRecipe;	
	private int qtyProcessing = 0;
	private int processTimeRemaining = -1;
	private int totalProcessTime = 0;
	
	public boolean isProcessing() {
		return isClient ? te.isProcessing() : processTimeRemaining > 0;
	}
	
	public int getQtyProcessing() { return qtyProcessing; }	
	
	public float getProcessPercent() {
		if (totalProcessTime <= 0 || processTimeRemaining <= 0) { return 0.0f; }
		return ((float)totalProcessTime - processTimeRemaining) / totalProcessTime;
	}
	
	/**
	 * Main Processing Loop
	 */		
	public void tick(int ticks) {
		if (isClient || sleep) { return; }

		boolean sleep = true;

		boolean oldProcess = this.isProcessing();
		
		preProcess();
		process(ticks);
		postProcess();
			
		boolean newProcess = this.isProcessing();
		if (newProcess != oldProcess) {
			te.setProcessingState(newProcess);
		}
	}
	
	/** Send preProcess() message to observers 
	 * @return */
	protected void preProcess() {
		for (IObserver obvserver : observers) {
			if (obvserver.preProcess((IMachineController)this)) { wake(); }
		}
	}
	
	/** Send postProcess() message to observers */
	protected void postProcess() {
		for (IObserver obvserver : observers) {
			if (obvserver.postProcess((IMachineController)this)) { wake(); }
		}
	}	
	
	/** @return true if any operation was performed, false otherwise. */	
	protected final void process(int ticks) {
		if (!te.isRedstoneActive()) { return; }
		
		if (!isProcessing()) {
			IRecipe recipe = recipemanager.getRecipe(inventory.getInput(), fluidtank.getInput());
			if (recipe == null || !beginProcess(recipe)) { return; }
			processTimeRemaining = totalProcessTime = recipe.getProcessTime();
			lastRecipe = recipe;
			wake(); return;
		} else if (lastRecipe == null) {
			// TE was just loaded from NBT
			lastRecipe = recipemanager.getRecipe(inventory.getInput(), fluidtank.getInput());
		}

		if (!extractEnergy(lastRecipe, ticks)) { return; }
		processTimeRemaining -= ticks;
		
		if (processTimeRemaining <= 0) {
			endProcess(lastRecipe);
			
			processTimeRemaining = 0;
		}
		
		wake();
		return;
	}
	
	protected boolean beginProcess(IRecipe recipe) {
		qtyProcessing = Math.min(getRecipeQty(recipe), te.getProcessingMultiplier());
		return qtyProcessing > 0;
	}
	
	protected int getRecipeQty(IRecipe recipe) {
		return recipe.getRecipeQty(inventory.getInput(), fluidtank.getInput(),
			inventory.getOutput(), fluidtank.getOutput());
	}	
	
	protected boolean extractEnergy(IRecipe lastRecipe, int ticks) {
		int amount = (lastRecipe.getEnergy() / totalProcessTime) * ticks;
		
		int extract = energy.extractEnergyInternal(amount, true);
		if (extract < amount) { return false; }
		
		energy.extractEnergyInternal(amount, false);
		return true;
	}
	
	
	protected void endProcess(IRecipe recipe) {
		IRecipe.IInput input = recipe.getInput();
		for (IItemGeneric item : input.getItems()) { extractItem(item); }
		for (FluidStack fluid : input.getFluids()) { extractFluid(fluid); }

		// Output
		IRecipe.IOutput output = recipe.getOutput();
		for (ItemStack item : output.getItems()) { outputItem(item); }
		for (FluidStack fluid : output.getFluids()) { outputFluid(fluid); }
		
		// Secondary
		for (int i = 0; i < qtyProcessing; i++) {
			IRecipe.IOutput secondary = recipe.getSecondary();
			for (ItemStack item : secondary.getItems()) { outputItem(item); }
			for (FluidStack fluid : secondary.getFluids()) { outputFluid(fluid); }
		}
	}
	
	protected void extractItem(IItemGeneric item) {
		int count = item.getCount() * qtyProcessing;
		inventory.extractItemInternal(new IItemGeneric.ItemGenericWrapper(item, count), false);
	}
	
	protected void extractFluid(FluidStack fluid) {
		int amount = fluid.amount * qtyProcessing;
		fluidtank.drainInternal(new FluidStack(fluid, amount), true);
	}
	
	protected void outputItem(ItemStack item) {
		ItemStack copy = item.copy();
		copy.setCount(item.getCount() * qtyProcessing);
		inventory.insertItemInternal(copy, false);
	}
	
	protected void outputFluid(FluidStack fluid) {
		int amount = fluid.amount * qtyProcessing;
		fluidtank.fillInternal(new FluidStack(fluid, amount), true);
	}

	/** Ends processing entirely, not just paused. */
	protected void haltProcess() {
		if (!isClient) { 
			processTimeRemaining = -1;
			qtyProcessing = 0;
			te.setProcessingState(false);
		}
	}
	
	/* Events */	

	@Override
	public void onInventoryContentsChanged(int slot) {
		wake();

		// We only care about input slots changing while processing (and only on the server)
		if (!isClient || !isProcessing()) { return; }

		int qty = this.getRecipeQty(lastRecipe);
		if (qty <= 0) { haltProcess(); }
		if (qty < qtyProcessing) { qtyProcessing = qty; }
	}

	@Override public void onEnergyChanged() { wake(); }
	@Override public void onTankContentsChanged() { wake(); }
	
	/* ISyncSubject */
	
	public int getFieldCount() { return 3; }
	public int getField(int id) {
		switch(id) {
		case 0:	return processTimeRemaining;
		case 1:	return totalProcessTime;
		case 2:	return energy.getEnergyStored();
		}
		return 0;
	}
	
	public void setField(int id, int value) {
		switch(id) {
		case 0:	processTimeRemaining = value; return;
		case 1:	totalProcessTime = value; return;
		case 2:	energy.setEnergy(value); return;
		}
	}	
	
	/* NBT */

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("processTimeRemaining", this.processTimeRemaining);
		nbt.setInteger("totalProcessTime", this.totalProcessTime);
		nbt.setInteger("qtyProcessing", this.qtyProcessing);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.processTimeRemaining = nbt.getInteger("processTimeRemaining");
		this.totalProcessTime = nbt.getInteger("totalProcessTime");
		this.qtyProcessing = nbt.getInteger("qtyProcessing");
		
		if (processTimeRemaining > 0) {
			te.setProcessingState(true);
		}
	}
}
