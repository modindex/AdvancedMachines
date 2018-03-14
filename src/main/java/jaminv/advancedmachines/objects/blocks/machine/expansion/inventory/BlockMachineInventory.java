package jaminv.advancedmachines.objects.blocks.machine.expansion.inventory;

import jaminv.advancedmachines.objects.blocks.machine.expansion.BlockMachineExpansion;
import jaminv.advancedmachines.objects.blocks.machine.expansion.TileEntityMachineExpansion;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.util.enums.EnumGui;
import jaminv.advancedmachines.util.helper.BlockHelper;
import jaminv.advancedmachines.util.interfaces.IHasTileEntity;
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

public class BlockMachineInventory extends BlockMachineExpansion implements ITileEntityProvider, IHasTileEntity {
	
    public static final PropertyBool INPUT = PropertyBool.create("input");
    public static final PropertyDirection FACING = PropertyDirection.create("facing");

	public BlockMachineInventory(String name) {
		super(name);
	}
	
	protected int getGuiId() { return EnumGui.MACHINE_INVENTORY.getId(); }
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

		TileEntity te = worldIn.getTileEntity(pos);
		if (te != null && te instanceof TileEntityMachineInventory) {
			((TileEntityMachineInventory)te).setFacing(EnumFacing.getDirectionFromEntityLiving(pos, placer));
		}
	}		 
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		return BlockHelper.openGui(worldIn, pos, playerIn, getGuiId());
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityMachineInventory();
	}
	
	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return TileEntityMachineInventory.class;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		VARIANT = this.getVariant();		
		return new BlockStateContainer(this, new IProperty[] { VARIANT, INPUT, FACING, BORDER_TOP, BORDER_BOTTOM, BORDER_NORTH, BORDER_SOUTH, BORDER_EAST, BORDER_WEST });
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntity tileentity = worldIn instanceof ChunkCache ? ((ChunkCache)worldIn).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : worldIn.getTileEntity(pos);

        EnumFacing facing = EnumFacing.NORTH;
        boolean input = true;
        MultiblockBorders borders = MultiblockBorders.DEFAULT;

        if (tileentity instanceof TileEntityMachineInventory) {
        	TileEntityMachineInventory te = (TileEntityMachineInventory)tileentity;
        	facing = te.getFacing();
        	input = te.getInputState();
        	borders = te.getBorders();
        }
        
        return state.withProperty(FACING, facing).withProperty(INPUT, input)
        	.withProperty(BORDER_TOP, borders.getTop()).withProperty(BORDER_BOTTOM, borders.getBottom())
        	.withProperty(BORDER_NORTH, borders.getNorth()).withProperty(BORDER_SOUTH, borders.getSouth())
        	.withProperty(BORDER_EAST, borders.getEast()).withProperty(BORDER_WEST, borders.getWest());
	}
}
