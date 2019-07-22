package jaminv.advancedmachines.objects.blocks.machine.multiblock.face;

import net.minecraft.util.EnumFacing;

public interface ICanHaveMachineFace {
	public static final PropertyMachineFace MACHINE_FACE = PropertyMachineFace.create("machine_face");	
	
	void setMachineFace(MachineFace face, MachineParent parent, EnumFacing facing);
}
