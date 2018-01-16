package jaminv.advancedmachines.objects.blocks.machine;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.Level;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.objects.blocks.machine.expansion.BlockMachineExpansion;
import jaminv.advancedmachines.util.Config;
import jaminv.advancedmachines.util.interfaces.IHasGui;
import jaminv.advancedmachines.util.recipe.IRecipeManager;
import jaminv.advancedmachines.util.recipe.RecipeBase;
import jaminv.advancedmachines.util.recipe.RecipeInput;
import jaminv.advancedmachines.util.recipe.RecipeOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public abstract class TileEntityMachineBase extends TileEntity implements ITickable, IHasGui {

	public abstract int getInputCount();
	public abstract int getOutputCount();
	public abstract int getSecondaryCount();
	public int getFirstInputSlot() { return 0; }
	public int getFirstOutputSlot() { return getInputCount(); }
	public int getFirstSecondarySlot() { return getInputCount() + getOutputCount(); }
	public int getInventorySize() { return getInputCount() + getOutputCount() + getSecondaryCount(); }
	
	protected ItemStackHandler inventory;
	public MachineEnergyStorage energy;
	
	public float getEnergyPercent() { return (float)energy.getEnergyStored() / energy.getMaxEnergyStored(); }
	public int getEnergyStored() { return energy.getEnergyStored(); }
	public int getMaxEnergyStored() { return energy.getMaxEnergyStored(); }
			
	protected class ItemStackHandlerBase extends ItemStackHandler {
		public ItemStackHandlerBase(int size) {
			super(size);
		}
		
		@Override
		protected void onContentsChanged(int slot) {
			TileEntityMachineBase.this.markDirty();
		}
		
	    @Override
	    @Nonnull
	    public ItemStack getStackInSlot(int slot)
	    {
	    	return super.getStackInSlot(slot);
	    }		
	};
	
	private IRecipeManager recipeManager;
	public IRecipeManager getRecipeManager() { return recipeManager; }
	
	public TileEntityMachineBase(IRecipeManager recipeManager) {
		super();
		this.inventory = new ItemStackHandlerBase(getInventorySize());
		this.energy = new MachineEnergyStorage(50000, 200);
		this.recipeManager = recipeManager;
		this.prevInput = new RecipeInput[getInputCount()];
	}
	
	private int tick;
	private RecipeInput[] prevInput;
		
	@Override
	public void update() {
		tick++;
		if (tick < Config.tickUpdate) { return; }
		
		RecipeInput[] input = getInput();
		checkInventoryChanges(input);
		
		tick = 0;
		if (canProcess(input)) {
			process(input);
		} else {
			haltProcess();
		}
	}
	
	private int processTimeRemaining = -1;
	private int totalProcessTime = 0;
	
	public float getProcessPercent() {
		if (totalProcessTime <= 0 || processTimeRemaining <= 0) { return 0.0f; }
		return ((float)totalProcessTime - processTimeRemaining + Config.tickUpdate) / totalProcessTime;
	}
	
	private RecipeInput lastInput = new RecipeInput();
	private RecipeBase lastRecipe;
	
	public boolean isProcessing() {
		return processTimeRemaining > 0;
	}
	
	public boolean canProcess(RecipeInput[] input) {
		RecipeBase recipe = recipeManager.getRecipeMatch(input);
		if (recipe == null) { return false; }
		
		return outputItem(recipe.getOutput(0), true);
	}

	protected void process(RecipeInput[] input) {
		if (world.isRemote) { return; }
		
		if (!isProcessing()) {
			processTimeRemaining = Config.processTimeBasic;
			totalProcessTime = Config.processTimeBasic;
			lastRecipe = recipeManager.getRecipe(input);
			return;
		} else if(lastRecipe == null) {
			lastRecipe = recipeManager.getRecipe(input);
		}

		if (energy.getEnergyStored() < (lastRecipe.getEnergy() / totalProcessTime) * Config.tickUpdate) { return; }
		
		processTimeRemaining -= Config.tickUpdate;
		energy.useEnergy((lastRecipe.getEnergy() / totalProcessTime) * Config.tickUpdate);
		
		if (processTimeRemaining <= 0) {
			RecipeBase recipe = lastRecipe;
			
			for (int i = 0; i < recipe.getInputCount(); i++) {
				if (recipe.getInput(i).isEmpty()) { continue; }
				if (!removeInput(recipe.getInput(i))) {
					// Some kind of strange error
					Main.logger.log(Level.ERROR,  "error.machine.process.cannot_input");
					haltProcess();
					return;
				}
			}
			
			for (int i = 0; i < recipe.getOutputCount(); i++) {
				if (recipe.getOutput(i).isEmpty()) { continue; }
				if (!outputItem(recipe.getOutput(i), false)) {
					// Some kind of strange error
					Main.logger.log(Level.ERROR, "error.machine.process.cannot_output");
					haltProcess();
					return;
				}
			}
			
			outputSecondary(recipe.getSecondary());
			
			processTimeRemaining = 0;
			return;
		}
	}
	
	protected void haltProcess() {
		processTimeRemaining = -1;
	}
	
	public int getFieldCount() { return 3; }
	public int getField(int id) {
		switch(id) {
		case 0:
			return processTimeRemaining;
		case 1:
			return totalProcessTime;
		case 2:
			return energy.getEnergyStored();
		}
		return 0;
	}
	
	public void setField(int id, int value) {
		switch(id) {
		case 0:
			processTimeRemaining = value; return;
		case 1:
			totalProcessTime = value; return;
		case 2:
			energy.setEnergy(value); return;
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("items")) {
			inventory.deserializeNBT((NBTTagCompound)compound.getTag("items"));
		}
		if (compound.hasKey("energy")) {
			energy.readFromNBT(compound);
		}
		processTimeRemaining = compound.getInteger("processTimeRemaining");
		totalProcessTime = compound.getInteger("totalProcessTime");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("items",  inventory.serializeNBT());
		energy.writeToNBT(compound);
		compound.setInteger("processTimeRemaining", processTimeRemaining);
		compound.setInteger("totalProcessTime", totalProcessTime);
		
		return compound;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		if (capability == CapabilityEnergy.ENERGY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
		}
		if (capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(this.energy);
		}
		return super.getCapability(capability, facing);
	}	
	
	protected RecipeInput[] getInput() {
		RecipeInput[] input = new RecipeInput[getInputCount()];
		for (int i = 0; i < getInputCount(); i++) {
			input[i] = new RecipeInput(inventory.getStackInSlot(getFirstInputSlot() + i));
		}
		return input;
	}
	
	private void checkInventoryChanges(RecipeInput[] input) {
		for (int i = 0; i < getInputCount(); i++) {
			if (prevInput[i] == null || input[i] == null) {
				prevInput = input;
				haltProcess(); return; 
			}
			if (!prevInput[i].equals(input[i])) {
				prevInput = input;
				haltProcess(); return;
			}
		}
		prevInput = input;
	}
	
	protected boolean removeInput(RecipeInput input) {
		for (int i = getFirstInputSlot(); i < getInputCount() + getFirstInputSlot(); i++) {
			RecipeInput slot = new RecipeInput(inventory.getStackInSlot(i));
			if (slot.doesMatch(input)) {
				inventory.extractItem(i, input.getCount(), false);
				return true;
			}
		}
		return false;
	}
	
	protected boolean outputItem(RecipeOutput output, boolean simulate) {
		ItemStack item = output.toItemStack();
		for (int i = getFirstOutputSlot(); i < getOutputCount() + getFirstOutputSlot(); i++) {
			if (inventory.insertItem(i, item, true).isEmpty()) {
				if (!simulate) { inventory.insertItem(i, item, false); }
				return true;
			}
		}
		return false;
	}
	
	protected void outputSecondary(NonNullList<RecipeOutput> secondary) {		
		for (RecipeOutput output : secondary) {
			if (world.rand.nextInt(100) > output.getChance()) { continue; }
			ItemStack item = output.toItemStack();
			for(int slot = getFirstSecondarySlot(); slot < getSecondaryCount() + getFirstSecondarySlot(); slot++) {
				if (inventory.insertItem(slot, item, true).isEmpty()) {
					inventory.insertItem(slot, item, false);
					break;
				}
			}
		}
	}
	
	public boolean canInteractWith(EntityPlayer playerIn) {
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}
	
}
