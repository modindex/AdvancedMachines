package jaminv.advancedmachines.machine.expansion;

import java.util.HashMap;
import java.util.Map;

import jaminv.advancedmachines.machine.TileEntityMachineMultiblock;
import jaminv.advancedmachines.machine.multiblock.MultiblockBorders;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface MachineUpgradeTileEntity {
	public void setBorders(World world, MultiblockBorders borders);
}
