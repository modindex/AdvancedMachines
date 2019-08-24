package jaminv.advancedmachines.machine.expansion.speed;

import jaminv.advancedmachines.machine.expansion.BlockMachineExpansionType;
import jaminv.advancedmachines.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.util.interfaces.IHasTileEntity;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockMachineSpeed extends BlockMachineExpansionType implements ITileEntityProvider, IHasTileEntity {
	
	public BlockMachineSpeed(String name) {
		super(name);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityMachineSpeed();
	}
	
	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return TileEntityMachineSpeed.class;
	}
	
	@Override
	public UpgradeType getUpgradeType() {
		return UpgradeType.SPEED;
	}	

	@Override
	public void setMultiblock(World world, BlockPos pos, BlockPos parent, MultiblockBorders borders) {
		super.setMultiblock(world, pos, parent, borders);
		TileEntity tileentity = world.getTileEntity(pos);
		if (tileentity instanceof TileEntityMachineSpeed) {
			TileEntityMachineSpeed te = (TileEntityMachineSpeed)tileentity;
			te.setBorders(world, borders);
		}
	}
	
	@Override
	public void registerModels() {
		registerCustomModel("bakedmodel_speed", BakedModelSpeed.class);
		registerVariantModels();
	}		
}
