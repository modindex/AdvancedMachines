package jaminv.advancedmachines.machine.instance.furnace;

import jaminv.advancedmachines.machine.BlockMachineMultiblock;
import jaminv.advancedmachines.machine.expansion.multiply.ModelBakeryMachineMultiply;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import jaminv.advancedmachines.util.enums.EnumGui;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMachineFurnace extends BlockMachineMultiblock {

	public BlockMachineFurnace(VariantExpansion variant) {
		super(variant);
	}
	
	@Override public MachineType getMachineType() { return MachineType.FURNACE; }	
	
	protected int getGuiId() { return EnumGui.FURNACE.getId(); }

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileMachineFurnace();
	}
}	
