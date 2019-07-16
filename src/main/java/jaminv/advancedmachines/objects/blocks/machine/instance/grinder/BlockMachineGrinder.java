package jaminv.advancedmachines.objects.blocks.machine.instance.grinder;

import jaminv.advancedmachines.objects.blocks.machine.multiblock.BlockMachineMultiblock;
import jaminv.advancedmachines.util.enums.EnumGui;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMachineGrinder extends BlockMachineMultiblock {

	public BlockMachineGrinder(String name) {
		super(name);
	}
	
	protected int getGuiId() { return EnumGui.GRINDER.getId(); }
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityMachineGrinder();
	}
	
	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return TileEntityMachineGrinder.class;
	}
}
