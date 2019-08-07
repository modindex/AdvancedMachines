package jaminv.advancedmachines.objects.blocks.machine;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.objects.blocks.energy.EnergyStorageObservable;
import jaminv.advancedmachines.objects.blocks.inventory.TileEntityInventory;
import jaminv.advancedmachines.objects.material.MaterialExpansion;
import jaminv.advancedmachines.util.ModConfig;
import jaminv.advancedmachines.util.helper.ItemHelper;
import jaminv.advancedmachines.util.interfaces.ICanProcess;
import jaminv.advancedmachines.util.interfaces.IDirectional;
import jaminv.advancedmachines.util.interfaces.IHasGui;
import jaminv.advancedmachines.util.interfaces.IHasMetadata;
import jaminv.advancedmachines.util.interfaces.IRedstoneControlled;
import jaminv.advancedmachines.util.message.ProcessingStateMessage;
import jaminv.advancedmachines.util.message.RedstoneStateMessage;
import jaminv.advancedmachines.util.recipe.IRecipeManager;
import jaminv.advancedmachines.util.recipe.RecipeBase;
import jaminv.advancedmachines.util.recipe.RecipeInput;
import jaminv.advancedmachines.util.recipe.RecipeOutput;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;

public abstract class TileEntityMachineBase extends TileEntityInventory implements ITickable, IHasGui, IMachineEnergy, IRedstoneControlled, IHasMetadata, IDirectional, ICanProcess {

	public abstract int getInputCount();
	public abstract int getOutputCount();
	public abstract int getSecondaryCount();
	public int getAdditionalCount() { return 0; }
	public int getFirstInputSlot() { return 0; }
	public int getFirstOutputSlot() { return getInputCount(); }
	public int getFirstSecondarySlot() { return getInputCount() + getOutputCount(); }
	public int getFirstAdditionalSlot() { return getInputCount() + getOutputCount() + getSecondaryCount(); }
	public int getInventorySize() { return getInputCount() + getOutputCount() + getSecondaryCount() + getAdditionalCount(); }
	
	protected EnumFacing facing = EnumFacing.NORTH;
	
	public void setFacing(EnumFacing facing) { this.facing = facing; }
	public EnumFacing getFacing() { return facing; }
	
	protected MachineEnergyStorage energy;
	
	public float getEnergyPercent() { return (float)energy.getEnergyStored() / energy.getMaxEnergyStored(); }
	public int getEnergyStored() { return energy.getEnergyStored(); }
	public int getMaxEnergyStored() { return energy.getMaxEnergyStored(); }
	public MachineEnergyStorage getEnergy() { return energy; }
	
	private final IRecipeManager recipeManager;
	public IRecipeManager getRecipeManager() { return recipeManager; }
	
	protected boolean redstone;
	protected RedstoneState redstoneState = RedstoneState.IGNORE;
	
	public RedstoneState getRedstoneState() {
		return redstoneState;
	}
	public void setRedstoneState(RedstoneState state) {
		this.redstoneState = state;
		
		if (world.isRemote) {
			Main.NETWORK.sendToServer(new RedstoneStateMessage(this.getPos(), state));
		}
	}
	
	public boolean isRedstoneActive() {
		return redstoneState == RedstoneState.IGNORE
			|| (redstoneState == RedstoneState.ACTIVE && redstone == true)
			|| (redstoneState == RedstoneState.INACTIVE && redstone == false);
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}
	
	private MaterialExpansion material;
	public MaterialExpansion getMaterial() { return material; }
	public int getMultiplier() { return material.getMultiplier(); }
	
	public void setMeta(int meta) {
		this.material = MaterialExpansion.byMetadata(meta);
		energy.setMaterial(material);
	}
	
	public TileEntityMachineBase(IRecipeManager recipeManager) {
		super();
		
		this.energy = new MachineEnergyStorage();
		this.recipeManager = recipeManager;
		this.prevInput = new ItemStack[getInputCount()];
	}
	
	private int tick;
	private ItemStack[] prevInput;
	
	private boolean sleep = false;
	/** Call to restart tick updates when a tile entity's state changes. */
	public void wake() { sleep = false; }
	
	@Override
	public void onInventoryContentsChanged(int slot) {
		super.onInventoryContentsChanged(slot);
		
		sleep = false;
	}
	
	@Override
	public void update() {
		if (sleep) { return; }

		boolean didSomething = false;
		
		tick++;
		if (tick < ModConfig.general.tickUpdate) { return; }

		boolean oldProcess = this.isProcessing();
		
		checkRedstone();
		didSomething = tickUpdate();
		
		ItemStack[] input = getInput();
		checkInventoryChanges(input);
		
		tick = 0;
		if (canProcess(input)) {
			didSomething = process(input) || didSomething;
		} else {
			haltProcess();
		}
		
		if (!didSomething) { sleep = true; }
		
		if (world.isRemote) { return; }
		
		boolean newProcess = this.isProcessing();
		if (newProcess != oldProcess) {
			sendProcessingMesssage(newProcess);
		}
	}
	
	protected void sendProcessingMesssage(boolean isProcessing) {
		Main.NETWORK.sendToAll(new ProcessingStateMessage(getPos(), getPos(), isProcessing));
	}
	
	/** @return true if any operation was performed, false otherwise. */
	protected boolean tickUpdate() { return false; }
	
	private int processTimeRemaining = -1;
	private int totalProcessTime = 0;
	
	public float getProcessPercent() {
		if (totalProcessTime <= 0 || processTimeRemaining <= 0) { return 0.0f; }
		return ((float)totalProcessTime - processTimeRemaining + ModConfig.general.tickUpdate) / totalProcessTime;
	}
	
	private RecipeInput lastInput = new RecipeInput();
	private RecipeBase lastRecipe;
	
	public boolean isProcessing() {
		return world.isRemote ? processingState : processTimeRemaining > 0 && this.isRedstoneActive();
	}
	
	protected boolean processingState = false;
	public void setProcessingState(boolean state) {
		this.processingState = state;
	}
	
	public boolean canProcess(ItemStack[] input) {
		
		RecipeBase recipe = recipeManager.getRecipeMatch(input);
		if (recipe == null) { return false; }
		
		return outputItem(recipe.getOutput(0), true);
	}

	/** @return true if any operation was performed, false otherwise. */	
	protected boolean process(ItemStack[] input) {
		if (world.isRemote) { return false; }
		
		if (!this.isRedstoneActive()) { return false; }
		
		if (!isProcessing()) {
			lastRecipe = recipeManager.getRecipe(input);
			processTimeRemaining = totalProcessTime = beginProcess(lastRecipe, input);
			return true;
		} else if(lastRecipe == null) {
			lastRecipe = recipeManager.getRecipe(input);
		}

		if (!extractEnergy(lastRecipe, totalProcessTime)) { return false; }
		processTimeRemaining -= ModConfig.general.tickUpdate;
		
		if (processTimeRemaining <= 0) {
			RecipeBase recipe = lastRecipe;
			
			endProcess(recipe);
			
			processTimeRemaining = 0;
		}
		
		return true;
	}
	
	protected int beginProcess(RecipeBase recipe, ItemStack[] input) {
		return ModConfig.general.processTimeBasic;
	}
	
	protected boolean extractEnergy(RecipeBase lastRecipe, int totalProcessTime) {
		int amount = (lastRecipe.getEnergy() / totalProcessTime) * ModConfig.general.tickUpdate;
		
		int extract = energy.extractEnergy(amount, true);
		if (extract < amount) { return false; }
		
		energy.extractEnergy(amount, false);
		return true;
	}
	
	protected void endProcess(RecipeBase recipe) {
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
	}
	
	/**
	 * Ends processing entirely, not just paused.
	 */
	protected void haltProcess() {
		if (!world.isRemote) { processTimeRemaining = -1; }
	}
	
	public int getFieldCount() { return 4; }
	public int getField(int id) {
		switch(id) {
		case 0:
			return processTimeRemaining;
		case 1:
			return totalProcessTime;
		case 2:
			return energy.getEnergyStored();
		case 3:
			return redstoneState.getValue();
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
		case 3:
			redstoneState = RedstoneState.fromValue(value);
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("meta")) {
			setMeta(compound.getInteger("meta"));
		}
		if (compound.hasKey("facing")) {
			facing = EnumFacing.byName(compound.getString("facing"));
		}		
		
		if (compound.hasKey("energy")) {
			energy.readFromNBT(compound);
		}
		processTimeRemaining = compound.getInteger("processTimeRemaining");
		totalProcessTime = compound.getInteger("totalProcessTime");
		processingState = processTimeRemaining > 0;
		
		if (compound.hasKey("redstone")) {
			redstone = compound.getBoolean("redstone");
		}
		if (compound.hasKey("redstoneState")) {
			redstoneState = RedstoneState.fromValue(compound.getInteger("redstoneState"));
		}
		
		this.prevInput = this.getInput();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("meta", material.getMeta());
		compound.setString("facing", facing.getName());
		energy.writeToNBT(compound);
		compound.setInteger("processTimeRemaining", processTimeRemaining);
		compound.setInteger("totalProcessTime", totalProcessTime);
		compound.setBoolean("redstone", redstone);
		compound.setInteger("redstoneState", redstoneState.getValue());
		return compound;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new ItemStackHandlerMachineWrapper(this, inventory));
		}		
		if (capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(this.energy);
		}
		return super.getCapability(capability, facing);
	}	
	
	public ItemStack[] getInput() {
		ItemStack[] input = new ItemStack[getInputCount()];
		for (int i = 0; i < getInputCount(); i++) {
			input[i] = inventory.getStackInSlot(getFirstInputSlot() + i).copy();
		}
		return input;
	}
	
	private void checkInventoryChanges(ItemStack[] input) {
		for (int i = 0; i < getInputCount(); i++) {
			if (prevInput[i] == null || input[i] == null) {
				prevInput = input;
				haltProcess(); return; 
			}
			if (!ItemHelper.itemsMatchWithCount(prevInput[i], input[i])) {
				prevInput = input;
				haltProcess(); return;
			}
		}
		prevInput = input;
	}
	
	protected boolean removeInput(RecipeInput input) {
		for (int i = getFirstInputSlot(); i < getInputCount() + getFirstInputSlot(); i++) {
			RecipeInput slot = new RecipeInput(inventory.getStackInSlot(i));
			if (input.isValid(inventory.getStackInSlot(i))) {
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
	
	protected ItemStack[] getOutputStacks() {
		ItemStack[] ret = new ItemStack[this.getOutputCount()];
		int stack = 0;
		for (int i = this.getFirstOutputSlot(); i < this.getOutputCount() + getFirstOutputSlot(); i++) {
			ret[stack] = inventory.getStackInSlot(i);
			stack++;
		}
		return ret;
	}
	
	public void checkRedstone() {
		redstone = world.isBlockPowered(pos);
	}
    
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
    }

    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }
    
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		handleUpdateTag(pkt.getNbtCompound());
	}	    
}
