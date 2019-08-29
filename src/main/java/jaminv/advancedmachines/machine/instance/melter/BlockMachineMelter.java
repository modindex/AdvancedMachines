package jaminv.advancedmachines.machine.instance.melter;

import jaminv.advancedmachines.machine.BlockMachineMultiblock;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import jaminv.advancedmachines.proxy.GuiProxy;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMachineMelter extends BlockMachineMultiblock {

	public BlockMachineMelter(VariantExpansion variant) {
		super(variant);
	}
	
	@Override public MachineType getMachineType() { return MachineType.MELTER; }	
	
	protected int getGuiId() { return GuiProxy.MELTER; }
	
	@Override
	public TileEntity createTileEntity(World worldIn, IBlockState state) {
		return new TileMachineMelter();
	}
}
