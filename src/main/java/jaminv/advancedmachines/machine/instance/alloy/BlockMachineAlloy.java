package jaminv.advancedmachines.machine.instance.alloy;

import jaminv.advancedmachines.machine.BlockMachineMultiblock;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import jaminv.advancedmachines.proxy.GuiProxy;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMachineAlloy extends BlockMachineMultiblock {

	public BlockMachineAlloy(VariantExpansion variant) {
		super(variant);
	}
	
	@Override public MachineType getMachineType() { return MachineType.ALLOY; }	
	
	protected int getGuiId() { return GuiProxy.ALLOY; }
	
	@Override
	public TileEntity createTileEntity(World worldIn, IBlockState state) {
		return new TileMachineAlloy();
	}
}
