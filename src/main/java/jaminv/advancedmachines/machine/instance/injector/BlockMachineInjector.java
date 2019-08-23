package jaminv.advancedmachines.machine.instance.injector;

import jaminv.advancedmachines.machine.BlockMachineMultiblock;
import jaminv.advancedmachines.util.enums.EnumGui;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMachineInjector extends BlockMachineMultiblock {

	public BlockMachineInjector(String name) {
		super(name);
	}
	
	protected int getGuiId() { return EnumGui.INJECTOR.getId(); }
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityMachineInjector();
	}
	
	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return TileEntityMachineInjector.class;
	}
}
