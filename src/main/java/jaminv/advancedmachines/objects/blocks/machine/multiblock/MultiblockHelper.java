package jaminv.advancedmachines.objects.blocks.machine.multiblock;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MultiblockHelper {
	public static TileEntityMachineMultiblock getParent(World world, BlockPos parent) {
		if (parent == null) { return null; }
		TileEntity te = world.getTileEntity(parent);
		if (te instanceof TileEntityMachineMultiblock) {
			return (TileEntityMachineMultiblock)te;
		}
		return null;
	}
	
	public static void wakeParent(World world, BlockPos parent) {
		TileEntityMachineMultiblock te = getParent(world, parent);
		if (te != null) { te.wake(); }
	}
	
	public static void sortTools(World world, BlockPos parent) {
		TileEntityMachineMultiblock te = getParent(world, parent);
		if (te != null) { te.sortTools(); }
	}
}
