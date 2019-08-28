package jaminv.advancedmachines.machine.instance.stabilizer;

import jaminv.advancedmachines.machine.BlockMachineMultiblock;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import jaminv.advancedmachines.util.enums.EnumGui;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMachineStabilizer extends BlockMachineMultiblock {

	public BlockMachineStabilizer(VariantExpansion variant) {
		super(variant);
	}
	
	@Override public MachineType getMachineType() { return MachineType.STABILIZER; }	
	
	protected int getGuiId() { return EnumGui.STABILIZER.getId(); }
	
	@Override
	public TileEntity createTileEntity(World worldIn, IBlockState state) {
		return new TileMachineStabilizer();
	}
}
