package jaminv.advancedmachines.machine.instance.injector;

import jaminv.advancedmachines.init.GuiProxy;
import jaminv.advancedmachines.machine.BlockMachine;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMachineInjector extends BlockMachine {

	public BlockMachineInjector(VariantExpansion variant) {
		super(variant);
	}
	
	@Override public MachineType getMachineType() { return MachineType.INJECTOR; }	
	
	protected int getGuiId() { return GuiProxy.INJECTOR; }
	
	@Override
	public TileEntity createTileEntity(World worldIn, IBlockState state) {
		return new TileMachineInjector();
	}
}
