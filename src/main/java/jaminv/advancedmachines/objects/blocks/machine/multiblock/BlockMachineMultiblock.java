package jaminv.advancedmachines.objects.blocks.machine.multiblock;

import jaminv.advancedmachines.objects.blocks.machine.BlockMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.TileEntityMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.expansion.IMachineUpgradeTileEntity;
import jaminv.advancedmachines.objects.blocks.machine.expansion.IMachineUpgrade.UpgradeType;
import jaminv.advancedmachines.objects.blocks.machine.expansion.inventory.TileEntityMachineInventory;
import jaminv.advancedmachines.util.helper.BlockHelper;
import jaminv.advancedmachines.util.material.MaterialExpansion;
import jaminv.advancedmachines.util.material.MaterialBase.MaterialType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public abstract class BlockMachineMultiblock extends BlockMachineBase {
	
	public static final PropertyBool BORDER_TOP = PropertyBool.create("border_top");
	public static final PropertyBool BORDER_BOTTOM = PropertyBool.create("border_bottom");
	public static final PropertyBool BORDER_NORTH = PropertyBool.create("border_north");
	public static final PropertyBool BORDER_SOUTH = PropertyBool.create("border_south");
	public static final PropertyBool BORDER_EAST = PropertyBool.create("border_east");
	public static final PropertyBool BORDER_WEST = PropertyBool.create("border_west");	

	public BlockMachineMultiblock(String name) {
		super(name);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		
		scanMultiblock(worldIn, pos, false);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		scanMultiblock(worldIn, pos, true);
		super.breakBlock(worldIn, pos, state);
	}
	
	protected void scanMultiblock(World worldIn, BlockPos pos, boolean destroy) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (!(te instanceof TileEntityMachineMultiblock)) { return; }
		
		((TileEntityMachineMultiblock)te).scanMultiblock(destroy);		
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		VARIANT = this.getVariant();		
		return new BlockStateContainer(this, new IProperty[] { VARIANT, FACING, ACTIVE, BORDER_TOP, BORDER_BOTTOM, BORDER_NORTH, BORDER_SOUTH, BORDER_EAST, BORDER_WEST });
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntity tileentity = worldIn instanceof ChunkCache ? ((ChunkCache)worldIn).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : worldIn.getTileEntity(pos);

        EnumFacing facing = EnumFacing.NORTH;
        boolean active = false;
        MultiblockBorders borders = MultiblockBorders.DEFAULT;

        if (tileentity instanceof TileEntityMachineMultiblock) {
        	TileEntityMachineMultiblock te = (TileEntityMachineMultiblock)tileentity;
        	facing = te.getFacing();
        	active = te.isProcessing();
        	borders = te.getBorders();
        }
        
        return state.withProperty(FACING, facing).withProperty(ACTIVE, active)
        	.withProperty(BORDER_TOP, borders.getTop()).withProperty(BORDER_BOTTOM, borders.getBottom())
        	.withProperty(BORDER_NORTH, borders.getNorth()).withProperty(BORDER_SOUTH, borders.getSouth())
        	.withProperty(BORDER_EAST, borders.getEast()).withProperty(BORDER_WEST, borders.getWest());
	}
	
	@Override
	public UpgradeType getUpgradeType() {
		return UpgradeType.MULTIPLY;
	}

	@Override
	public int getUpgradeQty(World world, BlockPos pos) {
		return MaterialExpansion.byMetadata(this.getMetaFromState(world.getBlockState(pos))).getMultiplier();
	}
	
	@Override
	public void setMultiblock(World world, BlockPos pos, BlockPos parent, MultiblockBorders borders) {
		BlockHelper.setBorders(world, pos, borders);
	}	
}
