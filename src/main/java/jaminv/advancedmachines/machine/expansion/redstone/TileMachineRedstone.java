package jaminv.advancedmachines.machine.expansion.redstone;

import jaminv.advancedmachines.lib.machine.MachineController;
import jaminv.advancedmachines.machine.expansion.TileMachineExpansion;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileMachineRedstone extends TileMachineExpansion implements MachineController.SubController {
	
	protected EnumFacing facing = EnumFacing.NORTH;
	protected MachineController controller;
	
	public void setFacing(EnumFacing facing) {
		this.facing = facing;
	}
	public EnumFacing getFacing() {
		return facing;
	}
	
	public TileMachineRedstone() {
		super();
	}
	
	boolean redstone;
	public void checkRedstone() {
		boolean oldRedstone = redstone;
		redstone = world.isBlockPowered(pos);
		if (oldRedstone != redstone) {
			world.markBlockRangeForRenderUpdate(pos, pos);
			if (controller != null) { controller.wake(); }
		}
	}
	
	public boolean getRedstone() {
		return redstone;
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}
	
	@Override
	public boolean preProcess(MachineController controller) {
		// TODO: Redstone
		//te.setRedstone(redstone);
		return false;
	}
	
	@Override
	public void setController(MachineController controller) {
		this.controller = controller;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("facing")) {
			facing = EnumFacing.byName(compound.getString("facing"));
		}
		if (compound.hasKey("redstone")) {
			redstone = compound.getBoolean("redstone");
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("facing", facing.getName());
		compound.setBoolean("redstone", redstone);
		return compound;
	}
	
	@Override
	public int getPriority() {
		return 0;
	}
}
