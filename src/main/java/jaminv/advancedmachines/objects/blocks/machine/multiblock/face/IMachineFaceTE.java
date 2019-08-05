package jaminv.advancedmachines.objects.blocks.machine.multiblock.face;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public interface IMachineFaceTE {
	void setMachineFace(MachineFace face, MachineType parent, EnumFacing facing, BlockPos parentloc);
	
	void setActive(boolean active);
}
