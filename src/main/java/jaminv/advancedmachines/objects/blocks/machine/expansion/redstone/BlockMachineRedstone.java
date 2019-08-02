package jaminv.advancedmachines.objects.blocks.machine.expansion.redstone;

import jaminv.advancedmachines.init.property.Properties;
import jaminv.advancedmachines.objects.blocks.machine.expansion.BlockMachineExpansionBase;
import jaminv.advancedmachines.objects.blocks.machine.expansion.expansion.BakedModelExpansion;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.util.interfaces.IHasTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.property.IExtendedBlockState;

public class BlockMachineRedstone extends BlockMachineExpansionBase implements ITileEntityProvider, IHasTileEntity {
	
    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    public static final PropertyDirection FACING = PropertyDirection.create("facing");

	public BlockMachineRedstone(String name) {
		super(name);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

		TileEntity te = worldIn.getTileEntity(pos);
		if (te != null && te instanceof TileEntityMachineRedstone) {
			((TileEntityMachineRedstone)te).setFacing(EnumFacing.getDirectionFromEntityLiving(pos, placer));
		}
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof TileEntityMachineRedstone) {
			((TileEntityMachineRedstone)te).checkRedstone();
			worldIn.setBlockState(pos, state.withProperty(ACTIVE, ((TileEntityMachineRedstone)te).getRedstone()));
		}
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityMachineRedstone();
	}
	
	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return TileEntityMachineRedstone.class;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		VARIANT = this.getVariant();
		BlockStateContainer.Builder builder = new BlockStateContainer.Builder(this);
		return builder.add(VARIANT)
			.add(Properties.BORDER_TOP, Properties.BORDER_BOTTOM) 
			.add(Properties.BORDER_NORTH, Properties.BORDER_SOUTH)
			.add(Properties.BORDER_EAST, Properties.BORDER_WEST)
			.add(Properties.FACING, Properties.ACTIVE)
			.build();
	}
	
	@Override
	public IExtendedBlockState getExtendedState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		IExtendedBlockState ext = (IExtendedBlockState)state;
        TileEntity tileentity = worldIn instanceof ChunkCache ? ((ChunkCache)worldIn).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : worldIn.getTileEntity(pos);

        EnumFacing facing = EnumFacing.NORTH;
        boolean active = false;
        MultiblockBorders borders = MultiblockBorders.DEFAULT;

        if (tileentity instanceof TileEntityMachineRedstone) {
        	TileEntityMachineRedstone te = (TileEntityMachineRedstone)tileentity;
        	facing = te.getFacing();
        	active = te.getRedstone();
        	borders = te.getBorders();
        }
        
        return (IExtendedBlockState) ext.withProperty(Properties.FACING, facing).withProperty(Properties.ACTIVE, active)
            	.withProperty(Properties.BORDER_TOP, borders.getTop()).withProperty(Properties.BORDER_BOTTOM, borders.getBottom())
            	.withProperty(Properties.BORDER_NORTH, borders.getNorth()).withProperty(Properties.BORDER_SOUTH, borders.getSouth())
            	.withProperty(Properties.BORDER_EAST, borders.getEast()).withProperty(Properties.BORDER_WEST, borders.getWest());
	}
	
	@Override
	public void setMultiblock(World world, BlockPos pos, BlockPos parent, MultiblockBorders borders) {
		super.setMultiblock(world, pos, parent, borders);
		TileEntity tileentity = world.getTileEntity(pos);
		if (tileentity instanceof TileEntityMachineRedstone) {
			TileEntityMachineRedstone te = (TileEntityMachineRedstone)tileentity;
			te.setBorders(world, borders);
		}
	}
	
	@Override
	public void registerModels() {
		registerCustomModel("bakedmodel_redstone", BakedModelRedstone.class);
		registerVariantModels();
	}	
}
