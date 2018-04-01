package jaminv.advancedmachines.objects.blocks.machine.expansion;

import jaminv.advancedmachines.objects.blocks.machine.multiblock.TileEntityMachineMultiblock;

public interface IMachineUpgradeTool extends IMachineUpgradeTileEntity {

	public void tickUpdate(TileEntityMachineMultiblock te);
	
	public int getPriority();
}
