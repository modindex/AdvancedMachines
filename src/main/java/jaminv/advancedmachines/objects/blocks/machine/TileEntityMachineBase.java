package jaminv.advancedmachines.objects.blocks.machine;

import javax.annotation.Nullable;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.lib.container.ISyncSubject;
import jaminv.advancedmachines.lib.container.SyncManager;
import jaminv.advancedmachines.lib.energy.IEnergyStorageAdvanced;
import jaminv.advancedmachines.lib.fluid.IFluidHandlerMachine;
import jaminv.advancedmachines.lib.inventory.IItemHandlerMachine;
import jaminv.advancedmachines.lib.machine.IMachineTE;
import jaminv.advancedmachines.lib.machine.IRedstoneControlled;
import jaminv.advancedmachines.lib.machine.MachineController;
import jaminv.advancedmachines.lib.machine.MachineStorage;
import jaminv.advancedmachines.lib.network.ProcessingStateMessage;
import jaminv.advancedmachines.lib.network.RedstoneStateMessage;
import jaminv.advancedmachines.lib.recipe.IRecipeManager;
import jaminv.advancedmachines.objects.material.MaterialExpansion;
import jaminv.advancedmachines.util.ModConfig;
import jaminv.advancedmachines.util.interfaces.IDirectional;
import jaminv.advancedmachines.util.interfaces.IHasGui;
import jaminv.advancedmachines.util.interfaces.IHasMetadata;
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
import net.minecraftforge.items.CapabilityItemHandler;

public abstract class TileEntityMachineBase extends TileEntity implements ITickable, IHasGui, IMachineTE, IHasMetadata, IDirectional, ISyncSubject {

	protected final MachineStorage storage;
	protected final MachineController controller;
	protected final SyncManager sync = new SyncManager();
	
	public TileEntityMachineBase(IItemHandlerMachine inventory, IFluidHandlerMachine fluidtank, IEnergyStorageAdvanced energy, IRecipeManager recipeManager) {
		super();
		
		storage = new MachineStorage(inventory, fluidtank, energy, recipeManager);
		controller = new MachineController(storage, this, world.isRemote);
		
		sync.addSubject(this);
		sync.addSubject(controller);
	}

	public IItemHandlerMachine getInventory() { return storage.getInventory(); }
	public IFluidHandlerMachine getFluidTank() { return storage.getFluidTank(); }
	public IEnergyStorageAdvanced getEnergy() { return storage.getEnergy(); }
	public IRecipeManager getRecipeManager() { return storage.getRecipeManager(); }
	
	/* Base machine data */
	
	private MaterialExpansion material;
	public MaterialExpansion getMaterial() { return material; }
	public int getMultiplier() { return material.getMultiplier(); }
	
	public void setMeta(int meta) {
		this.material = MaterialExpansion.byMetadata(meta);
		this.getEnergy().setCapacity(ModConfig.general.defaultMachineEnergyCapacity * getMultiplier());
		this.getFluidTank().setCapacity(ModConfig.general.defaultMachineFluidCapacity * getMultiplier());		
	}

	protected EnumFacing facing = EnumFacing.NORTH;
	
	public void setFacing(EnumFacing facing) { this.facing = facing; }
	public EnumFacing getFacing() { return facing; }		

	/* ICanProcess */
	
	protected boolean processingState = false;
	public boolean isProcessing() { return processingState; }	
	public void setProcessingState(boolean state) {	
		this.processingState = state;
		
		if (world.isRemote) {
			Main.NETWORK.sendToServer(new ProcessingStateMessage(this.getPos(), this.getPos(), state));
		}
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
	
	private int tick;

	@Override
	public void update() {
		tick++;
		if (tick < ModConfig.general.tickUpdate) { return; }
		tick = 0;
		
		controller.tick(ModConfig.general.tickUpdate);
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
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(storage);
		}		
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(storage);
		}
		if (capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(storage);
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
		setMeta(compound.getInteger("meta"));
		if (compound.hasKey("facing")) {
			facing = EnumFacing.byName(compound.getString("facing"));
		}
		redstoneState = RedstoneState.fromValue(compound.getInteger("redstoneState"));
		
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
		compound.setInteger("meta", material.getMeta());
		compound.setString("facing", facing.getName());
		compound.setInteger("redstoneState", redstoneState.getValue());

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
