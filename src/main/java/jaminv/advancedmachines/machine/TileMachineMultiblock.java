package jaminv.advancedmachines.machine;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import jaminv.advancedmachines.lib.machine.IMachineController.ISubController;
import jaminv.advancedmachines.lib.recipe.RecipeManager;
import jaminv.advancedmachines.machine.expansion.MachineUpgrade.UpgradeType;
import jaminv.advancedmachines.machine.expansion.MachineUpgradeTile;
import jaminv.advancedmachines.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.machine.multiblock.MultiblockBuilder;
import jaminv.advancedmachines.machine.multiblock.MultiblockState;
import jaminv.advancedmachines.machine.multiblock.MultiblockState.MultiblockNull;
import jaminv.advancedmachines.machine.multiblock.MultiblockUpgrades;
import jaminv.advancedmachines.machine.multiblock.face.MachineFace;
import jaminv.advancedmachines.machine.multiblock.face.MachineFaceBuilder;
import jaminv.advancedmachines.machine.multiblock.face.MachineFaceTile;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.util.network.ProcessingStateMessage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class TileMachineMultiblock extends TileMachine implements MachineUpgradeTile, MachineFaceTile {

	public TileMachineMultiblock(RecipeManager recipeManager) {
		super(recipeManager);
	}
	
	protected MultiblockBorders borders = new MultiblockBorders();
	public void setBorders(MultiblockBorders borders) { this.borders = borders; }
	public MultiblockBorders getBorders() {	return borders;	}
	
	protected void addSubcontrollers() {
		this.controller.clearSubContollers();
		for (int i = 0; i < upgrades.getToolCount(); i++) {
			BlockPos tool = upgrades.getTool(i);
			TileEntity te = world.getTileEntity(tool);
			if (te instanceof ISubController) {
				controller.addSubController((ISubController)te);
			}
		}
	}
	
	@Override
	protected boolean preProcess() {
		if (controller.getSubControllerCount() == 0 && upgrades.getToolCount() > 0) {
			this.addSubcontrollers();
			return true;
		}
		return false;
	}

	public abstract MachineType getMachineType();
	
	protected MachineFace face = MachineFace.NONE;
	protected BlockPos facemin, facemax;

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

	public MachineFace getMachineFace() { return face; }

	protected MultiblockState multiblockState = new MultiblockNull();
	public String getMultiblockString() { 
		if (multiblockState instanceof MultiblockNull) {
			scanMultiblock(null);
		}
		return multiblockState.getMultiblockString(); 
	}
	
	protected MultiblockUpgrades upgrades = MultiblockUpgrades.EMPTY;
	protected BlockPos multiblockMin = null, multiblockMax = null;
	
	protected void redrawMultiblock() {
		if (multiblockState.getMultiblockMin() == null ||
				multiblockState.getMultiblockMax() == null) { return; }
		world.markBlockRangeForRenderUpdate(multiblockState.getMultiblockMin(), 
				multiblockState.getMultiblockMax());		
	}
	
	public void scanMultiblock(@Nullable BlockPos blockDestroyed) {
		this.controller.wake();
		markDirty();
		facemin = null; facemax = null;
		this.redrawMultiblock();
		
		multiblockState = MultiblockBuilder.scanMultiblock(world, pos, blockDestroyed);
		if (multiblockState.isValid()) {
			this.addSubcontrollers();
			Pair<BlockPos, BlockPos> face = MachineFaceBuilder.scanFace(world, pos, facing);
			if (face != null) { facemin = face.getLeft(); face.getRight(); }
		}
		this.redrawMultiblock();
	}
	
	@Override
	protected IMessage getProcessingStateMessage(boolean state) {
		if (facemin != null && facemax != null) {
			return new ProcessingStateMessage(facemin, facemax, state);
		} else {
			super.getProcessingStateMessage(state);
		}

		return super.getProcessingStateMessage(state);
	}

	@Override
	public int getProcessingMultiplier() {
		return Math.max(upgrades.get(UpgradeType.MULTIPLY), getMultiplier());
	}	
	
	/* ================= *
	 *  Network and NBT  *
	 * ================= */

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("borders")) {
			borders.deserializeNBT(compound.getCompoundTag("borders"));
		}		
		if (compound.hasKey("upgrades")) {
			upgrades.deserializeNBT(compound.getCompoundTag("upgrades"));
		}
    	if (compound.hasKey("multiblockMin")) {
    		multiblockMin = NBTUtil.getPosFromTag(compound.getCompoundTag("multiblockMin"));
    	}
    	if (compound.hasKey("multiblockMax")) {
    		multiblockMax = NBTUtil.getPosFromTag(compound.getCompoundTag("multiblockMax"));
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
		compound.setTag("borders",  borders.serializeNBT());		
		compound.setTag("upgrades",  upgrades.serializeNBT());
        if (multiblockMin != null) { compound.setTag("multiblockMin", NBTUtil.createPosTag(multiblockMin)); }
        if (multiblockMax != null) { compound.setTag("multiblockMax", NBTUtil.createPosTag(multiblockMax)); }
        compound.setString("face", face.getName());
        if (facemin != null) { compound.setTag("facemin", NBTUtil.createPosTag(facemin)); }
        if (facemax != null) { compound.setTag("facemax", NBTUtil.createPosTag(facemax)); }
		return compound;
	}
}
