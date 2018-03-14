package jaminv.advancedmachines.objects.blocks.machine.expansion;

import jaminv.advancedmachines.objects.blocks.BlockMaterial;
import jaminv.advancedmachines.objects.blocks.machine.TileEntityMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.expansion.inventory.TileEntityMachineInventory;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.TileEntityMachineMultiblock;
import jaminv.advancedmachines.util.helper.BlockHelper;
import jaminv.advancedmachines.util.helper.BlockHelper.ScanResult;
import jaminv.advancedmachines.util.interfaces.IHasTileEntity;
import jaminv.advancedmachines.util.material.MaterialBase;
import jaminv.advancedmachines.util.material.MaterialExpansion;
import jaminv.advancedmachines.util.material.PropertyMaterial;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class BlockMachineExpansion extends BlockMaterial implements IMachineUpgrade, ITileEntityProvider, IHasTileEntity {
	
	public static final PropertyBool BORDER_TOP = PropertyBool.create("border_top");
	public static final PropertyBool BORDER_BOTTOM = PropertyBool.create("border_bottom");
	public static final PropertyBool BORDER_NORTH = PropertyBool.create("border_north");
	public static final PropertyBool BORDER_SOUTH = PropertyBool.create("border_south");
	public static final PropertyBool BORDER_EAST = PropertyBool.create("border_east");
	public static final PropertyBool BORDER_WEST = PropertyBool.create("border_west");

	public BlockMachineExpansion(String name) {
		super(name, MaterialBase.MaterialType.EXPANSION, null, Material.IRON, 5.0f);
	}


	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return TileEntityMachineExpansion.class;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityMachineExpansion();
	}	
	
	@Override
	protected PropertyMaterial getVariant() {
		return PropertyMaterial.create("variant", MaterialBase.MaterialType.EXPANSION);
	}
	
	@Override
	protected IBlockState createDefaultState() {
		return super.createDefaultState().withProperty(BORDER_TOP, true).withProperty(BORDER_BOTTOM, true)
			.withProperty(BORDER_NORTH, true).withProperty(BORDER_SOUTH, true)
			.withProperty(BORDER_EAST, true).withProperty(BORDER_WEST, true);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		scanMultiblock(worldIn, pos);
	}	
	
	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		scanMultiblock(worldIn, pos);
	}
	
	@Override
	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
		scanMultiblock(worldIn, pos);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		VARIANT = this.getVariant();		
		return new BlockStateContainer(this, new IProperty[] { VARIANT, BORDER_TOP, BORDER_BOTTOM, BORDER_NORTH, BORDER_SOUTH, BORDER_EAST, BORDER_WEST });
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntity tileentity = worldIn instanceof ChunkCache ? ((ChunkCache)worldIn).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : worldIn.getTileEntity(pos);
        MultiblockBorders borders = MultiblockBorders.DEFAULT;

        if (tileentity instanceof TileEntityMachineExpansion) {
        	TileEntityMachineExpansion te = (TileEntityMachineExpansion)tileentity;
        	borders = te.getBorders();
        }
        
        return state.withProperty(BORDER_TOP, borders.getTop()).withProperty(BORDER_BOTTOM, borders.getBottom())
        	.withProperty(BORDER_NORTH, borders.getNorth()).withProperty(BORDER_SOUTH, borders.getSouth())
        	.withProperty(BORDER_EAST, borders.getEast()).withProperty(BORDER_WEST, borders.getWest());
	}
	
	protected void scanMultiblock(World world, BlockPos pos) {
		ScanResult result = BlockHelper.scanBlocks(world, pos, new TileEntityMachineMultiblock.MultiblockChecker());
		BlockPos end = result.getEnd();
		if (end == null) { return; }
		
		TileEntity te = world.getTileEntity(end);
		if (te instanceof TileEntityMachineMultiblock) {
			((TileEntityMachineMultiblock)te).scanMultiblock();
		}
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
		TileEntity tileentity = world.getTileEntity(pos);
		if (tileentity instanceof IMachineUpgradeTileEntity) {
			((IMachineUpgradeTileEntity)tileentity).setBorders(borders);
		}
	}
}
