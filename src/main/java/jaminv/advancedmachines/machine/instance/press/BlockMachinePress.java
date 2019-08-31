package jaminv.advancedmachines.machine.instance.press;

import jaminv.advancedmachines.init.GuiProxy;
import jaminv.advancedmachines.machine.BlockMachineMultiblock;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMachinePress extends BlockMachineMultiblock {

	public BlockMachinePress(VariantExpansion variant) {
		super(variant);
	}
	
	@Override public MachineType getMachineType() { return MachineType.PRESS; }	
	
	protected int getGuiId() { return GuiProxy.PRESS; }
	
	@Override
	public TileEntity createTileEntity(World worldIn, IBlockState state) {
		return new TileMachinePress();
	}
}
