package jaminv.advancedmachines.objects.blocks.machine.expansion.redstone;

import jaminv.advancedmachines.lib.machine.IMachineController;
import jaminv.advancedmachines.objects.blocks.machine.expansion.TileEntityMachineExpansionBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityMachineRedstone extends TileEntityMachineExpansionBase implements IMachineController.ISubController {
	
	protected EnumFacing facing = EnumFacing.NORTH;
	protected IMachineController controller;
	
	public void setFacing(EnumFacing facing) {
		this.facing = facing;
	}
	public EnumFacing getFacing() {
		return facing;
	}
	
	public TileEntityMachineRedstone() {
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
	public boolean preProcess(IMachineController controller) {
		// TODO: Redstone
		//te.setRedstone(redstone);
		return false;
	}
	
	@Override
	public void setController(IMachineController controller) {
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
