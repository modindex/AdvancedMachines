package jaminv.advancedmachines.machine.expansion.multiply;

import javax.annotation.Nullable;

import jaminv.advancedmachines.machine.expansion.TileMachineExpansion;
import jaminv.advancedmachines.machine.multiblock.face.MachineFace;
import jaminv.advancedmachines.machine.multiblock.face.MachineFaceTile;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;

public class TileMachineMultiply extends TileMachineExpansion implements MachineFaceTile {

	protected MachineFace face = MachineFace.NONE;
	protected MachineType parent = MachineType.NONE;
	protected EnumFacing facing = EnumFacing.UP;
	protected BlockPos parentpos = null;
	protected boolean active = false;
	
	public void setMachineFace(MachineFace face, MachineType parent, EnumFacing facing, BlockPos pos) {
		this.face = face;
		this.parent = parent;
		this.facing = facing;
		parentpos = pos;
	}
	
	@Override
	public void setActive(boolean active) {
		this.active = active;
		if (world != null) { world.checkLightFor(EnumSkyBlock.BLOCK, this.getPos()); }
	}
	
	public boolean isActive() { return active; }

	public MachineFace getMachineFace() { return face; }
	public MachineType getMachineParent() { return parent; }
	public EnumFacing getFacing() {	return facing; }
	public BlockPos getParentPos() { return parentpos; }

	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
    	active = compound.getBoolean("active");
		if (compound.hasKey("face")) {
    		face = MachineFace.lookup(compound.getString("face"));
    	}
    	if (compound.hasKey("parent")) {
    		parent = MachineType.lookup(compound.getString("parent"));
    	}
		if (compound.hasKey("facing")) {
			facing = EnumFacing.byName(compound.getString("facing"));
		}		
		if (compound.hasKey("parentpos")) {
			parentpos = NBTUtil.getPosFromTag(compound.getCompoundTag("parentpos"));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		compound.setBoolean("active", active);
		compound.setString("face", face.getName());
		compound.setString("parent", parent.getName());
		compound.setString("facing", facing.getName());
		if (parentpos != null) {
			compound.setTag("parentpos", NBTUtil.createPosTag(parentpos));
		}

		return compound;
	}
	
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
    }
    
	public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }	
}
