package jaminv.advancedmachines.objects.blocks.machine.instance.alloy;

import jaminv.advancedmachines.client.BakedModelMultiblock;
import jaminv.advancedmachines.objects.blocks.machine.BlockMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.instance.purifier.TileEntityMachinePurifier;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.BlockMachineMultiblock;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.util.enums.EnumGui;
import jaminv.advancedmachines.util.material.MaterialBase.MaterialType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockMachineAlloy extends BlockMachineMultiblock {

	public BlockMachineAlloy(String name) {
		super(name);
	}
	
	protected int getGuiId() { return EnumGui.ALLOY.getId(); }
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityMachineAlloy();
	}
	
	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return TileEntityMachineAlloy.class;
	}

	@Override
	public void registerModels() {
		registerCustomModel(BakedModelMultiblock.ALLOY);
		registerVariantModels();
	}	
}
