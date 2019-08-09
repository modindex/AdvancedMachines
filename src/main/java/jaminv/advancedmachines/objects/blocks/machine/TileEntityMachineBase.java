package jaminv.advancedmachines.objects.blocks.machine;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.lib.inventory.MachineInventoryHandler;
import jaminv.advancedmachines.lib.recipe.IRecipeManager;
import jaminv.advancedmachines.lib.recipe.RecipeBase;
import jaminv.advancedmachines.lib.recipe.RecipeInput;
import jaminv.advancedmachines.lib.recipe.RecipeOutput;
import jaminv.advancedmachines.objects.blocks.inventory.TileEntityInventory;
import jaminv.advancedmachines.objects.material.MaterialExpansion;
import jaminv.advancedmachines.util.ModConfig;
import jaminv.advancedmachines.util.interfaces.ICanProcess;
import jaminv.advancedmachines.util.interfaces.IDirectional;
import jaminv.advancedmachines.util.interfaces.IHasGui;
import jaminv.advancedmachines.util.interfaces.IHasMetadata;
import jaminv.advancedmachines.util.interfaces.IRedstoneControlled;
import jaminv.advancedmachines.util.message.ProcessingStateMessage;
import jaminv.advancedmachines.util.message.RedstoneStateMessage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;

public abstract class TileEntityMachineBase extends TileEntity implements ITickable, IHasGui, IMachineEnergy, IRedstoneControlled, IHasMetadata, IDirectional, ICanProcess, MachineInventoryHandler.IObserver {

	public TileEntityMachineBase(IRecipeManager recipeManager) {
		super();
	
		inventory.addObserver(this);
		this.energy = new MachineEnergyStorage();
		this.recipeManager = recipeManager;
	}
	
	/* =========== *
	 *  Inventory  *
	 * =========== */
	
	public MachineInventoryHandler inventory = new MachineInventoryHandler();
	
	/* ======== *
	 *  Energy  *
	 * ======== */
	
	protected MachineEnergyStorage energy;
	
	public float getEnergyPercent() { return (float)energy.getEnergyStored() / energy.getMaxEnergyStored(); }
	public int getEnergyStored() { return energy.getEnergyStored(); }
	public int getMaxEnergyStored() { return energy.getMaxEnergyStored(); }
	public MachineEnergyStorage getEnergy() { return energy; }
	
	/* ========== *
	 *  Redstone  *
	 * ========== */
	
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
	
	public void checkRedstone() {
		redstone = world.isBlockPowered(pos);
	}	
	
	/* ============ *
	 *  Other Data  *
	 * ============ */
	
	private MaterialExpansion material;
	public MaterialExpansion getMaterial() { return material; }
	public int getMultiplier() { return material.getMultiplier(); }
	
	public void setMeta(int meta) {
		this.material = MaterialExpansion.byMetadata(meta);
		energy.setMaterial(material);
	}

	protected EnumFacing facing = EnumFacing.NORTH;
	
	public void setFacing(EnumFacing facing) { this.facing = facing; }
	public EnumFacing getFacing() { return facing; }
	
	private final IRecipeManager recipeManager;
	public IRecipeManager getRecipeManager() { return recipeManager; }
	
	/* ======== *
	 *  Events  *
	 * ======== */
	
	@Override
	public void onInventoryContentsChanged(int slot) {
		sleep = false;

		// We only care about input slots changing while processing (and only on the server)
		if (world.isRemote || !isProcessing()) { return; }
		if (!inventory.isSlotInput(slot)) { return; }

		onInputChanged(slot, lastRecipe);
	}
	
	public void onInputChanged(int slot, RecipeBase recipe) {
		if(recipe.getRecipeQty(getInput(), getOutput()) <= 0) { haltProcess(); }		
	}
	
	/* ================= *
	 *  Processing Data  *
	 * ================= */
	
	private int tick;
	
	private boolean sleep = false;
	/** Call to restart tick updates when a tile entity's state changes. */
	public void wake() { sleep = false; }
	
	private int processTimeRemaining = -1;
	private int totalProcessTime = 0;
	
	public float getProcessPercent() {
		if (totalProcessTime <= 0 || processTimeRemaining <= 0) { return 0.0f; }
		return ((float)totalProcessTime - processTimeRemaining + ModConfig.general.tickUpdate) / totalProcessTime;
	}
	
	private RecipeBase lastRecipe;
	
	public boolean isProcessing() {
		return world.isRemote ? processingState : processTimeRemaining > 0 && this.isRedstoneActive();
	}	
	
	protected boolean processingState = false;
	public void setProcessingState(boolean state) {
		this.processingState = state;
	}
	
	/* ============ *
	 *  Processing  *
	 * ============ */	
	
	/**
	 * Main Processing Loop
	 */		
	@Override
	public void update() {
		if (sleep) { return; }

		boolean didSomething = false;
		
		tick++;
		if (tick < ModConfig.general.tickUpdate) { return; }
		tick = 0;

		boolean oldProcess = this.isProcessing();
		
		checkRedstone();
		didSomething = preProcess() || process() || postProcess();
		
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
	
	/**
	 * Override for pre-process tasks 
	 * 
	 * Default method does nothing and returns false.
	 * @return true if any operation was performed, false otherwise. 
	 */
	protected boolean preProcess() { return false; }
	
	/**
	 * Override for post-process tasks.
	 * 
	 * Default method does nothing and returns false.
	 * @return true if any operation was performed, false otherwise. 
	 */
	protected boolean postProcess() { return false; }	
	
	/** @return true if any operation was performed, false otherwise. */	
	protected final boolean process() {
		if (world.isRemote) { return false; }
		
		if (!this.isRedstoneActive()) { return false; }
		
		if (!isProcessing()) {
			RecipeBase recipe = recipeManager.getRecipe(inventory.getInput());
			if (recipe == null || !beginProcess(recipe)) { return false; }
			processTimeRemaining = totalProcessTime = recipe.getProcessTime();
			lastRecipe = recipe;
			return true;
		} else if (lastRecipe == null) {
			// TE was just loaded from NBT
			lastRecipe = recipeManager.getRecipe(inventory.getInput());
		}

		if (!extractEnergy(lastRecipe, totalProcessTime)) { return false; }
		processTimeRemaining -= ModConfig.general.tickUpdate;
		
		if (processTimeRemaining <= 0) {
			endProcess(lastRecipe);
			
			processTimeRemaining = 0;
		}
		
		return true;
	}
	
	protected boolean beginProcess(RecipeBase recipe) {
		return recipe.getRecipeQty(inventory.getInput(), inventory.getOutput()) > 0;
	}
	
	protected boolean extractEnergy(RecipeBase lastRecipe, int totalProcessTime) {
		int amount = (lastRecipe.getEnergy() / totalProcessTime) * ModConfig.general.tickUpdate;
		
		int extract = energy.extractEnergyInternal(amount, true);
		if (extract < amount) { return false; }
		
		energy.extractEnergyInternal(amount, false);
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
	
	/* ============== *
	 *  Capabilities  *
	 * ============== */
	
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
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory));
		}		
		if (capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(this.energy);
		}
		return super.getCapability(capability, facing);
	}		
	
	/* ================= *
	 *  Network and NBT  *
	 * ================= */
	
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
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}	
}
