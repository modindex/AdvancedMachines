package jaminv.advancedmachines.machine.instance.melter;

import jaminv.advancedmachines.init.GuiProxy;
import jaminv.advancedmachines.machine.BlockMachine;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMachineMelter extends BlockMachine {

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
