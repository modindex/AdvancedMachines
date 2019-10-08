package jaminv.advancedmachines.machine.instance.stabilizer;

import jaminv.advancedmachines.init.GuiProxy;
import jaminv.advancedmachines.machine.BlockMachine;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMachineStabilizer extends BlockMachine {

	public BlockMachineStabilizer(VariantExpansion variant) {
		super(variant);
	}
	
	@Override public MachineType getMachineType() { return MachineType.STABILIZER; }	
	
	protected int getGuiId() { return GuiProxy.STABILIZER; }
	
	@Override
	public TileEntity createTileEntity(World worldIn, IBlockState state) {
		return new TileMachineStabilizer();
	}
}
