package jaminv.advancedmachines.objects.blocks.machine.multiblock.face;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public interface ICanHaveMachineFace {
	public static final PropertyMachineFace MACHINE_FACE = PropertyMachineFace.create("machine_face");	
	
	void setMachineFace(MachineFace face, MachineType parent, EnumFacing facing, BlockPos parentloc);
	
	void setActive(boolean active);
}
