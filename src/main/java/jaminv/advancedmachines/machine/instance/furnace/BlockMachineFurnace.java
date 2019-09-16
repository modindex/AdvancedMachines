package jaminv.advancedmachines.machine.instance.furnace;

import jaminv.advancedmachines.init.GuiProxy;
import jaminv.advancedmachines.machine.BlockMachine;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMachineFurnace extends BlockMachine {

	public BlockMachineFurnace(VariantExpansion variant) {
		super(variant);
	}
	
	@Override public MachineType getMachineType() { return MachineType.FURNACE; }	
	
	protected int getGuiId() { return GuiProxy.FURNACE; }

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileMachineFurnace();
	}
}	
