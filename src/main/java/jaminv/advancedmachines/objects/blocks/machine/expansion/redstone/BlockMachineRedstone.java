package jaminv.advancedmachines.objects.blocks.machine.expansion.redstone;

import jaminv.advancedmachines.objects.blocks.machine.TileEntityMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.expansion.BlockMachineExpansion;
import jaminv.advancedmachines.objects.blocks.machine.expansion.TileEntityMachineExpansion;
import jaminv.advancedmachines.objects.blocks.machine.expansion.inventory.TileEntityMachineInventory;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.TileEntityMachineMultiblock;
import jaminv.advancedmachines.util.enums.EnumGui;
import jaminv.advancedmachines.util.helper.BlockHelper;
import jaminv.advancedmachines.util.interfaces.IHasTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class BlockMachineRedstone extends BlockMachineExpansion implements ITileEntityProvider, IHasTileEntity {
	
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
		return new BlockStateContainer(this, new IProperty[] { VARIANT, ACTIVE, FACING, BORDER_TOP, BORDER_BOTTOM, BORDER_NORTH, BORDER_SOUTH, BORDER_EAST, BORDER_WEST });
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
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
        
        return state.withProperty(FACING, facing).withProperty(ACTIVE, active)
        	.withProperty(BORDER_TOP, borders.getTop()).withProperty(BORDER_BOTTOM, borders.getBottom())
        	.withProperty(BORDER_NORTH, borders.getNorth()).withProperty(BORDER_SOUTH, borders.getSouth())
        	.withProperty(BORDER_EAST, borders.getEast()).withProperty(BORDER_WEST, borders.getWest());
	}
	
	@Override
	public void setMultiblock(World world, BlockPos pos, BlockPos parent, MultiblockBorders borders) {
		super.setMultiblock(world, pos, parent, borders);
		TileEntity tileentity = world.getTileEntity(pos);
		if (tileentity instanceof TileEntityMachineRedstone) {
			TileEntityMachineRedstone te = (TileEntityMachineRedstone)tileentity;
			te.setBorders(borders);
		}
	}
}
