package jaminv.advancedmachines.objects.blocks.machine.expansion;

import jaminv.advancedmachines.objects.blocks.machine.multiblock.TileEntityMachineMultiblock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public interface IMachineUpgradeTool extends IMachineUpgradeTileEntity {
	public void setParent(BlockPos pos);
	
	public boolean tickUpdate(TileEntityMachineMultiblock te);
	
	public int getPriority();
}
