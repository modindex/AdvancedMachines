package jaminv.advancedmachines.machine.instance.alloy;

import jaminv.advancedmachines.machine.BlockMachineMultiblock;
import jaminv.advancedmachines.machine.expansion.expansion.ModelBakeryMachineExpansion;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.objects.material.MaterialExpansion;
import jaminv.advancedmachines.util.enums.EnumGui;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMachineAlloy extends BlockMachineMultiblock {

	public BlockMachineAlloy(String name) {
		super(name);
	}
	
	@Override public MachineType getMachineType() { return MachineType.ALLOY; }	
	
	protected int getGuiId() { return EnumGui.ALLOY.getId(); }
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityMachineAlloy();
	}
	
	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return TileEntityMachineAlloy.class;
	}
}
