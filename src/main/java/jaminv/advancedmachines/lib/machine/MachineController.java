 package jaminv.advancedmachines.lib.machine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import jaminv.advancedmachines.lib.container.ISyncSubject;
import jaminv.advancedmachines.lib.energy.IEnergyObservable;
import jaminv.advancedmachines.lib.energy.IEnergyStorageAdvanced;
import jaminv.advancedmachines.lib.energy.IEnergyStorageInternal;
import jaminv.advancedmachines.lib.fluid.IFluidHandlerInternal;
import jaminv.advancedmachines.lib.fluid.IFluidObservable;
import jaminv.advancedmachines.lib.inventory.IItemHandlerMachine;
import jaminv.advancedmachines.lib.inventory.IItemObservable;
import jaminv.advancedmachines.lib.inventory.InventoryHelper;
import jaminv.advancedmachines.lib.recipe.Ingredient;
import jaminv.advancedmachines.lib.recipe.Recipe;
import jaminv.advancedmachines.lib.recipe.RecipeManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;

public class MachineController implements IMachineController, IItemObservable.IObserver, IFluidObservable.IObserver, IEnergyObservable.IObserver, 
		INBTSerializable<NBTTagCompound>, ISyncSubject {
	
	protected final IItemHandlerMachine inventory;
	protected final IFluidHandlerInternal fluidtank;
	protected final IEnergyStorageAdvanced energy;
	protected final RecipeManager recipemanager;
	protected final IMachineTE te;
	protected boolean includeAdditional = false;
	
	public MachineController(IItemHandlerMachine inventory, IFluidHandlerInternal fluidtank, IEnergyStorageAdvanced energy, RecipeManager recipemanager, IMachineTE te) {
		this.inventory = inventory;
		this.fluidtank = fluidtank;
		this.energy = energy;
		this.recipemanager = recipemanager;
		this.te = te;
	}
	
	public MachineController(IMachineStorage storage, RecipeManager recipemanager, IMachineTE te) {
		inventory = storage;
		fluidtank = storage;
		energy = storage;
		this.recipemanager = recipemanager;
		
		inventory.addObserver(this);
		fluidtank.addObserver(this);
		energy.addObserver(this);
		
		this.te = te;
	}
	
	public IItemHandlerMachine getInventory() { return inventory; }
	public IFluidHandlerInternal getFluidTank() { return fluidtank; }
	public IEnergyStorageInternal getEnergy() { return energy; }
	public RecipeManager getRecipeManager() { return recipemanager; }	
	
	private List<ISubController> subcontrollers = new ArrayList<ISubController>();
	public void addSubController(ISubController sub) { 
		subcontrollers.add(sub);
		sub.setController(this);
	}
	public void removeSubController(ISubController sub) { subcontrollers.remove(sub); }
	public void clearSubContollers() { subcontrollers.clear(); }
	public int getSubControllerCount() { return subcontrollers.size(); }
	
	private static class SubControllerCompare implements Comparator<ISubController> {
		@Override 
		public int compare(ISubController sub1, ISubController sub2) {
			return sub1.getPriority() - sub2.getPriority();
		}		
	}
	
	@Override
	public void sortSubControllers() {
		subcontrollers.sort(new SubControllerCompare());
	}
	
	/** Include additional inventory as though it were input inventory.
	 * Used for catalyst items. */
	public void includeAdditional(boolean include) {
		this.includeAdditional = include;
	}
	
	/* Base Processing Data */

	private boolean sleep = false;
	/** Call to restart tick updates when a tile entity's state changes. */
	public void wake() { sleep = false; }
	
	private Recipe lastRecipe;	
	private int qtyProcessing = 0;
	private int processTimeRemaining = -1;
	private int totalProcessTime = 0;
	
	public boolean isProcessing() {
		return te.isClient() ? te.isProcessing() : processTimeRemaining > 0;
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
		if (te.isClient() || sleep) { return; }

		sleep = true;

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
		for (ISubController obvserver : subcontrollers) {
			if (obvserver.preProcess((IMachineController)this)) { wake(); }
		}
	}
	
	/** Send postProcess() message to observers */
	protected void postProcess() {
		for (ISubController obvserver : subcontrollers) {
			if (obvserver.postProcess((IMachineController)this)) { wake(); }
		}
	}
	
	protected ItemStack[] getItemInput() {
		if (!includeAdditional) { return inventory.getItemInput(); }
		return ArrayUtils.addAll(inventory.getItemInput(), inventory.getItemAdditional());
	}
	
	/** @return true if any operation was performed, false otherwise. */	
	protected final void process(int ticks) {
		if (!te.isRedstoneActive()) { return; }
		
		if (!isProcessing()) {
			beginProcess();
			return;
		} else if (lastRecipe == null) {
			// TE was just loaded from NBT
			lastRecipe = recipemanager.getRecipe(getItemInput(), fluidtank.getStacks());
			if (lastRecipe == null) { haltProcess(); return; }
		}

		if (!extractEnergy(lastRecipe, ticks)) { return; }
		processTimeRemaining -= ticks;
		
		if (processTimeRemaining <= 0) {
			endProcess(lastRecipe);			
			beginProcess();
			return;
		}		
		wake();
		return;
	}
	
	protected void beginProcess() {
		Recipe recipe = recipemanager.getRecipe(getItemInput(), fluidtank.getStacks());
		if (recipe == null) { return; }
		
		qtyProcessing = Math.min(getRecipeQty(recipe), te.getProcessingMultiplier());
		if (qtyProcessing <= 0) { return; }
		
		processTimeRemaining = totalProcessTime = recipe.getProcessTime();
		lastRecipe = recipe;
		wake();
	}
	
	protected int getRecipeQty(Recipe recipe) {
		return recipe.getRecipeQty(inventory.getItemInput(), fluidtank.getStacks(),
			inventory.getOutput(), fluidtank.getTanks());
	}	
	
	protected boolean extractEnergy(Recipe lastRecipe, int ticks) {
		int amount = (lastRecipe.getEnergy() / totalProcessTime) * ticks;
		
		int extract = energy.extractEnergyInternal(amount, true);
		if (extract < amount) { return false; }
		
		energy.extractEnergyInternal(amount, false);
		return true;
	}
	
	
	protected void endProcess(Recipe recipe) {
		Recipe.Input input = recipe.getInput(true);
		for (Ingredient item : input.getItems()) { extractItem(item); }
		for (FluidStack fluid : input.getFluids()) { extractFluid(fluid); }

		// Output
		Recipe.Output output = recipe.getOutput();
		for (ItemStack item : output.getItems()) { outputItem(item, 
			inventory.getFirstOutputSlot(), inventory.getLastOutputSlot()); }
		for (FluidStack fluid : output.getFluids()) { outputFluid(fluid); }
		
		// Secondary
		for (int i = 0; i < qtyProcessing; i++) {
			Recipe.Output secondary = recipe.getSecondary();
			for (ItemStack item : secondary.getItems()) { outputItem(item, 
				inventory.getFirstSecondarySlot(), inventory.getLastSecondarySlot()); }
			for (FluidStack fluid : secondary.getFluids()) { outputFluid(fluid); }
		}
		
		qtyProcessing = 0;
	}
	
	protected void extractItem(Ingredient item) {
		int qty = item.getCount() * qtyProcessing;
		int count = 0;
		
		// I had generalized code for this, but it's really only ever used here.
		for (int i = 0; i < inventory.getSlots(); i++) {
			if (item.isValid(inventory.getStackInSlot(i))) {
				ItemStack result = inventory.extractItem(i, qty, false);
				count += result.getCount();
			}
			if (count >= qty) { return; }
		}
	}
	
	protected void extractFluid(FluidStack fluid) {
		int amount = fluid.amount * qtyProcessing;
		fluidtank.drainInternal(new FluidStack(fluid, amount), true);
	}
	
	protected void outputItem(ItemStack item, int firstSlot, int lastSlot) {
		ItemStack copy = item.copy();
		copy.setCount(item.getCount() * qtyProcessing);
		InventoryHelper.pushStack(copy, inventory, firstSlot, lastSlot, false);
	}
	
	protected void outputFluid(FluidStack fluid) {
		int amount = fluid.amount * qtyProcessing;
		fluidtank.fillInternal(new FluidStack(fluid, amount), true);
	}

	/** Ends processing entirely, not just paused. */
	protected void haltProcess() {
		if (!te.isClient()) { 
			processTimeRemaining = -1;
			qtyProcessing = 0;
			te.setProcessingState(false);
		}
	}
	
	/* Events */	

	/**
	 * CANTFIX: When a use adds items to an existing stack, MC briefly sends an inventory change event saying the slot is empty.
	 * This resets the recipe. This doesn't happen with hoppers, etc, just human interaction. There doesn't appear to be a way to fix it.
	 */
	@Override
	public void onInventoryContentsChanged(int slot) {
		wake();
		te.onControllerUpdate();

		// We only care about input slots changing while processing (and only on the server)
		if (te.isClient() || !isProcessing()) { return; }

		int qty = this.getRecipeQty(lastRecipe);
		if (qty <= 0) { haltProcess(); }
		if (qty < qtyProcessing) { qtyProcessing = qty; }
	}

	@Override public void onEnergyChanged() { wake(); te.onControllerUpdate(); }
	@Override public void onTankContentsChanged() { wake(); te.onControllerUpdate(); }
	
	/* ISyncSubject */
	
	public int getFieldCount() { return 3; }
	public int getField(int id) {
		switch(id) {
		case 0:	return processTimeRemaining;
		case 1:	return totalProcessTime;
		case 2: return qtyProcessing;
		}
		return 0;
	}
	
	public void setField(int id, int value) {
		switch(id) {
		case 0:	processTimeRemaining = value; return;
		case 1:	totalProcessTime = value; return;
		case 2: qtyProcessing = value; return;
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
		
		if (processTimeRemaining > 0 && !te.isProcessing()) {
			te.setProcessingState(true);
		}
	}
}
