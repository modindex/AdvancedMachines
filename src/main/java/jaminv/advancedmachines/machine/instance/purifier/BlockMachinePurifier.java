package jaminv.advancedmachines.machine.instance.purifier;

import jaminv.advancedmachines.machine.BlockMachineMultiblock;
import jaminv.advancedmachines.machine.expansion.expansion.ModelBakeryMachineExpansion;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.util.enums.EnumGui;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMachinePurifier extends BlockMachineMultiblock {

	public BlockMachinePurifier(String name) {
		super(name);
	}
	
	@Override public MachineType getMachineType() { return MachineType.PURIFIER; }	
	
	protected int getGuiId() { return EnumGui.PURIFIER.getId(); }
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityMachinePurifier();
	}
	
	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return TileEntityMachinePurifier.class;
	}
}
