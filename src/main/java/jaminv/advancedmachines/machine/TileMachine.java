package jaminv.advancedmachines.machine;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import jaminv.advancedmachines.AdvancedMachines;
import jaminv.advancedmachines.ModConfig;
import jaminv.advancedmachines.init.HasGui;
import jaminv.advancedmachines.lib.container.SyncManager;
import jaminv.advancedmachines.lib.container.SyncManagerStandard;
import jaminv.advancedmachines.lib.container.SyncSubject;
import jaminv.advancedmachines.lib.energy.EnergyStorageAdvanced;
import jaminv.advancedmachines.lib.energy.IEnergyStorageInternal;
import jaminv.advancedmachines.lib.fluid.BucketHandler;
import jaminv.advancedmachines.lib.fluid.FluidTanksHandler;
import jaminv.advancedmachines.lib.fluid.FluidHandlerAdditional;
import jaminv.advancedmachines.lib.inventory.ItemHandlerSeparated;
import jaminv.advancedmachines.lib.inventory.MachineInventoryHandler;
import jaminv.advancedmachines.lib.machine.MachineController;
import jaminv.advancedmachines.lib.machine.MachineController.SubController;
import jaminv.advancedmachines.lib.machine.MachineControllerDefault;
import jaminv.advancedmachines.lib.machine.MachineStorage;
import jaminv.advancedmachines.lib.machine.MachineStorageCapability;
import jaminv.advancedmachines.lib.machine.MachineTile;
import jaminv.advancedmachines.lib.machine.RedstoneControlled;
import jaminv.advancedmachines.lib.recipe.RecipeManager;
import jaminv.advancedmachines.lib.util.helper.BlockIterator;
import jaminv.advancedmachines.lib.util.helper.HasFacing;
import jaminv.advancedmachines.machine.expansion.MachineUpgrade.UpgradeType;
import jaminv.advancedmachines.machine.expansion.MachineUpgradeTile;
import jaminv.advancedmachines.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.machine.multiblock.MultiblockBuilder;
import jaminv.advancedmachines.machine.multiblock.MultiblockState;
import jaminv.advancedmachines.machine.multiblock.face.MachineFace;
import jaminv.advancedmachines.machine.multiblock.face.MachineFaceBuilder;
import jaminv.advancedmachines.machine.multiblock.face.MachineFaceTile;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.machine.multiblock.network.MultiblockDestroyMessage;
import jaminv.advancedmachines.machine.multiblock.network.MultiblockUpdateMessage;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import jaminv.advancedmachines.util.network.RedstoneStateMessage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

public abstract class TileMachine extends TileEntity implements ITickable, HasGui, MachineTile, HasFacing, SyncSubject,
		MachineUpgradeTile, MachineFaceTile {
	
	protected final MachineStorage storage;
	protected final MachineControllerDefault controller;
	protected final SyncManagerStandard sync = new SyncManagerStandard();
	
	protected final MachineInventoryHandler inventory = new MachineInventoryHandler();
	protected final FluidTanksHandler inputTanks = new FluidTanksHandler(ModConfig.general.defaultMachineFluidCapacity * VariantExpansion.maxMultiplier,
		ModConfig.general.defaultMachineFluidTransfer * VariantExpansion.maxMultiplier);
	protected final FluidTanksHandler outputTanks = new FluidTanksHandler(ModConfig.general.defaultMachineFluidCapacity * VariantExpansion.maxMultiplier,
			ModConfig.general.defaultMachineFluidTransfer * VariantExpansion.maxMultiplier);
	protected final EnergyStorageAdvanced energy = new EnergyStorageAdvanced(ModConfig.general.defaultMachineEnergyCapacity * VariantExpansion.maxMultiplier);
	
	public TileMachine(RecipeManager recipeManager) {
		super();
		
		storage = new MachineStorage(inventory, inputTanks, outputTanks, energy, recipeManager);
		controller = new MachineControllerDefault(storage, recipeManager, this);
		
		sync.addSubject(this);
		sync.addSubject(controller);
	}
	
	public MachineController getController() { return controller; }
	public ItemHandlerSeparated getInventory() { return inventory; }
	public FluidHandlerAdditional getInputTanks() { return inputTanks; }
	public FluidHandlerAdditional getOutputTanks() { return outputTanks; }
	public IEnergyStorageInternal getEnergy() { return energy; }
	public RecipeManager getRecipeManager() { return storage.getRecipeManager(); }
	
	public SyncManager getSyncManager() {
		return new SyncManagerStandard().addSubject(this).addSubject(controller);
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
	
	/* Multiblock Data */
	
	protected MultiblockBorders borders = new MultiblockBorders();
	public void setBorders(MultiblockBorders borders) { this.borders = borders; }
	public MultiblockBorders getBorders() {	return borders;	}
	
	protected MachineFace face = MachineFace.NONE;
	public MachineFace getMachineFace() { return face; }

	protected BlockPos facemin, facemax;

	public abstract MachineType getMachineType();
	
	/* Machine */
	
	BucketHandler bucketHandler = new BucketHandler();
	
	public boolean onBlockActivated(EntityPlayer player, EnumHand hand) {
		return bucketHandler.onBlockActivate(player, hand, storage);
	}

	/* CanProcess */
	
	protected boolean processingState = false;
	public boolean isProcessing() { return processingState; }	
	public void setProcessingState(boolean state) {	
		this.processingState = state;
		if (world != null) { world.checkLightFor(EnumSkyBlock.BLOCK, pos); }
		
		if (world != null && !world.isRemote) {
			AdvancedMachines.NETWORK.sendToAll(new ProcessingStateMessage(this.getPos(), state));
		}
		
		if (facemin != null && facemax != null) {
			setFaceActive(state);
		}
	}
	
	/* RedstoneControlled */
	
	protected RedstoneState redstoneState = RedstoneState.IGNORE;
	
	public RedstoneState getRedstoneState() { return redstoneState; }
	public void setRedstoneState(RedstoneState state) {
		this.redstoneState = state;
		
		if (world.isRemote) {
			AdvancedMachines.NETWORK.sendToServer(new RedstoneStateMessage(this.getPos(), state));
		}
	}
	
	public boolean isRedstoneActive() {
		return RedstoneControlled.isRedstoneActive(redstoneState, world.isBlockPowered(pos));
	}
	
	/* MachineTile */

	@Override
	public int getProcessingMultiplier() {
		return Math.max(multiblockState.getUpgrades().get(UpgradeType.MULTIPLY), getMultiplier());
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
	
	protected boolean preProcess() {
		if (controller.getSubControllerCount() == 0 && multiblockState.getUpgrades().getToolCount() > 0) {
			this.addSubcontrollers();
			return true;
		}
		return false;
	}	
	
	@Override
	public void onControllerUpdate() {
		markDirty();
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
	}
	
	/* ============ *
	 *  Multiblock  *
	 * ============ */
	
	protected void addSubcontrollers() {
		this.controller.clearSubContollers();
		for (int i = 0; i < multiblockState.getUpgrades().getToolCount(); i++) {
			BlockPos tool = multiblockState.getUpgrades().getTool(i);
			TileEntity te = world.getTileEntity(tool);
			if (te instanceof SubController) {
				controller.addSubController((SubController)te);
			}
		}
	}
	
	@Override
	public void setMachineFace(MachineFace face, MachineType parent, EnumFacing facing, BlockPos pos) {
		this.face = face;
		if (face == MachineFace.NONE) {
			facemin = pos; facemax = pos;
		}
	}
	
	@Override
	public void setActive(boolean active) {
		; // no op
	}

	protected MultiblockState multiblockState = new MultiblockState();
	public String getMultiblockString() { 
		if (multiblockState.getMessage() == null) {
			scanMultiblock(null);
		}
		return multiblockState.getMessage().toString(); 
	}
	
	public void scanMultiblock(@Nullable BlockPos blockDestroyed) {
		BlockPos min = multiblockState.getMultiblockMin(), max = multiblockState.getMultiblockMax();
		
		this.controller.wake();
		markDirty();
		
		// breakBlock() doesn't get called on the client
		if (blockDestroyed != null && !world.isRemote) {
			if (blockDestroyed.equals(pos)) {
				if (min != null && max != null) {
					AdvancedMachines.NETWORK.sendToAll(new MultiblockDestroyMessage(min, max));
				}
			} else {
				AdvancedMachines.NETWORK.sendToAll(new MultiblockUpdateMessage(pos, blockDestroyed));
			}
		}
		
		if (min != null && max != null) {
			MultiblockBuilder.setMultiblock(min, max, false, world);
		}
		
		facemin = null; facemax = null;
		
		multiblockState = MultiblockBuilder.scanMultiblock(world, pos, blockDestroyed);
		if (multiblockState.isValid()) {
			this.addSubcontrollers();
			Pair<BlockPos, BlockPos> face = MachineFaceBuilder.scanFace(world, pos, facing);
			if (face != null) { 
				facemin = face.getLeft(); facemax = face.getRight();
				setFaceActive(this.processingState);
			}
		}
	}
	
	protected void setFaceActive(boolean state) {
		BlockIterator.iterateBlocks(world, facemin, facemax, (world, pos) -> {
			TileEntity te = world.getTileEntity(pos);
			if (te instanceof MachineFaceTile) {
				((MachineFaceTile)te).setActive(state);
			}
		});
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
		
		borders.deserializeNBT(compound.getCompoundTag("borders"));
		multiblockState.deserializeNBT(compound.getCompoundTag("multiblock"));	
		
		if (compound.hasKey("controller")) {
			controller.deserializeNBT(compound.getCompoundTag("controller"));
		}
		if (compound.hasKey("storage")) {
			storage.deserializeNBT(compound.getCompoundTag("storage"));
		}
	
    	if (compound.hasKey("face")) {
    		face = MachineFace.lookup(compound.getString("face"));
    	}
    	if (compound.hasKey("facemin")) {
    		facemin = NBTUtil.getPosFromTag(compound.getCompoundTag("facemin"));
    	}
    	if (compound.hasKey("facemax")) {
    		facemax = NBTUtil.getPosFromTag(compound.getCompoundTag("facemax"));
    	}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("variant", variant.getName());
		compound.setString("facing", facing.getName());
		compound.setInteger("redstoneState", redstoneState.getValue());
		compound.setBoolean("processingState", processingState);

		compound.setTag("borders",  borders.serializeNBT());
		compound.setTag("multiblock", multiblockState.serializeNBT());

		compound.setTag("controller", controller.serializeNBT());
		compound.setTag("storage", storage.serializeNBT());	
		
        compound.setString("face", face.getName());
        if (facemin != null) { compound.setTag("facemin", NBTUtil.createPosTag(facemin)); }
        if (facemax != null) { compound.setTag("facemax", NBTUtil.createPosTag(facemax)); }
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
