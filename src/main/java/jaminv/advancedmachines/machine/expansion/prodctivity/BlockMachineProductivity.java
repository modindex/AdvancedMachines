package jaminv.advancedmachines.machine.expansion.prodctivity;

import jaminv.advancedmachines.lib.render.ModelBakery;
import jaminv.advancedmachines.machine.expansion.BlockMachineExpansionType;
import jaminv.advancedmachines.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.util.interfaces.IHasTileEntity;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockMachineProductivity extends BlockMachineExpansionType implements ITileEntityProvider, IHasTileEntity {
	
	public BlockMachineProductivity(String name) {
		super(name);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityMachineProductivity();
	}
	
	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return TileEntityMachineProductivity.class;
	}
	
	@Override
	public UpgradeType getUpgradeType() {
		return UpgradeType.PRODUCTIVITY;
	}	

	@Override
	public void setMultiblock(World world, BlockPos pos, BlockPos parent, MultiblockBorders borders) {
		super.setMultiblock(world, pos, parent, borders);
		TileEntity tileentity = world.getTileEntity(pos);
		if (tileentity instanceof TileEntityMachineProductivity) {
			TileEntityMachineProductivity te = (TileEntityMachineProductivity)tileentity;
			te.setBorders(world, borders);
		}
	}
	
	@Override
	public void registerModels() {
		registerVariantModels();
	}

	protected static ModelBakery bakery = new ModelBakeryMachineProductivity();
	@Override public ModelBakery getModelBakery() { return bakery; }
}
