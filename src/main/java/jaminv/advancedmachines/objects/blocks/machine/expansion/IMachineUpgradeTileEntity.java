package jaminv.advancedmachines.objects.blocks.machine.expansion;

import java.util.HashMap;
import java.util.Map;

import jaminv.advancedmachines.objects.blocks.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.TileEntityMachineMultiblock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IMachineUpgradeTileEntity {
	public void setBorders(MultiblockBorders borders);
}
