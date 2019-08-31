package jaminv.advancedmachines.machine;

import javax.annotation.Nullable;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.ModConfig;
import jaminv.advancedmachines.init.HasGui;
import jaminv.advancedmachines.lib.container.ISyncManager;
import jaminv.advancedmachines.lib.container.ISyncSubject;
import jaminv.advancedmachines.lib.container.SyncManager;
import jaminv.advancedmachines.lib.energy.EnergyStorageAdvanced;
import jaminv.advancedmachines.lib.energy.IEnergyStorageInternal;
import jaminv.advancedmachines.lib.fluid.FluidHandler;
import jaminv.advancedmachines.lib.fluid.IFluidHandlerAdvanced;
import jaminv.advancedmachines.lib.inventory.IItemHandlerMachine;
import jaminv.advancedmachines.lib.inventory.MachineInventoryHandler;
import jaminv.advancedmachines.lib.machine.IMachineController;
import jaminv.advancedmachines.lib.machine.IMachineTE;
import jaminv.advancedmachines.lib.machine.IRedstoneControlled;
import jaminv.advancedmachines.lib.machine.MachineController;
import jaminv.advancedmachines.lib.machine.MachineStorage;
import jaminv.advancedmachines.lib.machine.MachineStorageCapability;
import jaminv.advancedmachines.lib.recipe.IRecipeManager;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import jaminv.advancedmachines.util.helper.Directional;
import jaminv.advancedmachines.util.network.ProcessingStateMessage;
import jaminv.advancedmachines.util.network.RedstoneStateMessage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.items.CapabilityItemHandler;

public abstract class TileMachine extends TileEntity implements ITickable, HasGui, IMachineTE, VariantExpansion.Needs, Directional, ISyncSubject {

	protected final MachineStorage storage;
	protected final MachineController controller;
	protected final SyncManager sync = new SyncManager();
	
	protected final MachineInventoryHandler inventory = new MachineInventoryHandler();
	protected final FluidHandler inputTanks = new FluidHandler(ModConfig.general.defaultMachineFluidCapacity * VariantExpansion.maxMultiplier,
		ModConfig.general.defaultMachineFluidTransfer * VariantExpansion.maxMultiplier);
	protected final FluidHandler outputTanks = new FluidHandler(ModConfig.general.defaultMachineFluidCapacity * VariantExpansion.maxMultiplier,
			ModConfig.general.defaultMachineFluidTransfer * VariantExpansion.maxMultiplier);
	protected final EnergyStorageAdvanced energy = new EnergyStorageAdvanced(ModConfig.general.defaultMachineEnergyCapacity * VariantExpansion.maxMultiplier);
	
	public TileMachine(IRecipeManager recipeManager) {
		super();
		
		storage = new MachineStorage(inventory, inputTanks, outputTanks, energy, recipeManager);
		controller = new MachineController(storage, recipeManager, this);
		
		sync.addSubject(this);
		sync.addSubject(controller);
	}
	
	public IMachineController getController() { return controller; }
	public IItemHandlerMachine getInventory() { return inventory; }
	public IFluidHandlerAdvanced getInputTanks() { return inputTanks; }
	public IFluidHandlerAdvanced getOutputTanks() { return outputTanks; }
	public IEnergyStorageInternal getEnergy() { return energy; }
	public IRecipeManager getRecipeManager() { return storage.getRecipeManager(); }
	
	public ISyncManager getSyncManager() {
		return new SyncManager().addSubject(this).addSubject(controller);
	}
	
	/* Base machine data */
	
	private VariantExpansion variant = VariantExpansion.BASIC;
	public VariantExpansion getVariant() { return variant; }
	public int getMultiplier() { return variant.getMultiplier(); }
	
	public void setVariant(VariantExpansion variant) {
		if (variant == null) { return; } // FIXME: Backwards compatibility
		this.variant = variant;
		this.energy.setEnergyCapacity(ModConfig.general.defaultMachineEnergyCapacity * getMultiplier());
		this.inputTanks.setFluidCapacity(ModConfig.general.defaultMachineFluidCapacity * getMultiplier());
		this.outputTanks.setFluidCapacity(ModConfig.general.defaultMachineFluidCapacity * getMultiplier());
	}

	protected EnumFacing facing = EnumFacing.NORTH;
	
	public void setFacing(EnumFacing facing) { this.facing = facing; }
	public EnumFacing getFacing() { return facing; }		

	/* ICanProcess */
	
	protected boolean processingState = false;
	public boolean isProcessing() { return processingState; }	
	public void setProcessingState(boolean state) {	
		this.processingState = state;
		
		if (world != null && !world.isRemote) {
			Main.NETWORK.sendToAll(getProcessingStateMessage(state));
		}
	}

	protected IMessage getProcessingStateMessage(boolean state) {
		return new ProcessingStateMessage(this.getPos(), this.getPos(), state);
	}
	
	/* IRedstoneControlled */
	
	protected RedstoneState redstoneState = RedstoneState.IGNORE;
	
	public RedstoneState getRedstoneState() { return redstoneState; }
	public void setRedstoneState(RedstoneState state) {
		this.redstoneState = state;
		
		if (world.isRemote) {
			Main.NETWORK.sendToServer(new RedstoneStateMessage(this.getPos(), state));
		}
	}
	
	public boolean isRedstoneActive() {
		return IRedstoneControlled.isRedstoneActive(redstoneState, world.isBlockPowered(pos));
	}

	/* ============ *
	 *  Processing  *
	 * ============ */
	
	@Override public boolean isClient() { return world.isRemote; }	
	
	private int tick;

	@Override
	public void update() {
		tick++;
		if (tick < ModConfig.general.tickUpdate) { return; }
		tick = 0;
		
		if (preProcess()) { controller.wake(); }
		
		controller.tick(ModConfig.general.tickUpdate);
	}
	
	protected boolean preProcess() { return false; }
	
	
	@Override
	public void onControllerUpdate() {
		markDirty();
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
	}	

	/* ============== *
	 *  Capabilities  *
	 * ============== */

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return true;
		}
		if (capability == CapabilityEnergy.ENERGY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		MachineStorageCapability cap = new MachineStorageCapability(storage);
		
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(cap);
		}		
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(cap);
		}
		if (capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(cap);
		}
		return super.getCapability(capability, facing);
	}		
	
	/* ============== *
	 *  ISyncManager  *
	 * ============== */
	
	public int getFieldCount() { return 1; }
	public int getField(int id) {
		switch(id) {
		case 0:	return redstoneState.getValue();
		}
		return 0;
	}
	
	public void setField(int id, int value) {
		switch(id) {
		case 0:	redstoneState = RedstoneState.fromValue(value);
		}
	}
	
	/* ================= *
	 *  Network and NBT  *
	 * ================= */	
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		setVariant(VariantExpansion.lookup(compound.getString("variant")));
		if (compound.hasKey("facing")) {
			facing = EnumFacing.byName(compound.getString("facing"));
		}
		redstoneState = RedstoneState.fromValue(compound.getInteger("redstoneState"));
		processingState = compound.getBoolean("processingState");
		
		if (compound.hasKey("controller")) {
			controller.deserializeNBT(compound.getCompoundTag("controller"));
		}
		if (compound.hasKey("storage")) {
			storage.deserializeNBT(compound.getCompoundTag("storage"));
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("variant", variant.getName());
		compound.setString("facing", facing.getName());
		compound.setInteger("redstoneState", redstoneState.getValue());
		compound.setBoolean("processingState", processingState);

		compound.setTag("controller", controller.serializeNBT());
		compound.setTag("storage", storage.serializeNBT());
		return compound;
	}	
    
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
    }

    public NBTTagCompound getUpdateTag() {
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
