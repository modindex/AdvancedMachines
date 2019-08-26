package jaminv.advancedmachines.machine.instance.grinder;

import jaminv.advancedmachines.machine.BlockMachineMultiblock;
import jaminv.advancedmachines.machine.expansion.expansion.ModelBakeryMachineExpansion;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.objects.material.MaterialExpansion;
import jaminv.advancedmachines.util.enums.EnumGui;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMachineGrinder extends BlockMachineMultiblock {

	public BlockMachineGrinder(String name) {
		super(name);
	}
	
	@Override public MachineType getMachineType() { return MachineType.GRINDER; }	
	
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
