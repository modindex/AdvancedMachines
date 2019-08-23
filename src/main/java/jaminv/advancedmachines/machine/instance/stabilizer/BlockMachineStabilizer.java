package jaminv.advancedmachines.machine.instance.stabilizer;

import jaminv.advancedmachines.machine.BlockMachineMultiblock;
import jaminv.advancedmachines.util.enums.EnumGui;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMachineStabilizer extends BlockMachineMultiblock {

	public BlockMachineStabilizer(String name) {
		super(name);
	}
	
	protected int getGuiId() { return EnumGui.STABILIZER.getId(); }
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityMachineStabilizer();
	}
	
	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return TileEntityMachineStabilizer.class;
	}
}
