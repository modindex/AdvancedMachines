package jaminv.advancedmachines.objects.blocks.machine.instance.melter;

import jaminv.advancedmachines.objects.blocks.machine.BlockMachineMultiblock;
import jaminv.advancedmachines.util.enums.EnumGui;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMachineMelter extends BlockMachineMultiblock {

	public BlockMachineMelter(String name) {
		super(name);
	}
	
	protected int getGuiId() { return EnumGui.MELTER.getId(); }
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityMachineMelter();
	}
	
	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return TileEntityMachineMelter.class;
	}
}
