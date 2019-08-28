package jaminv.advancedmachines.machine.expansion;

import jaminv.advancedmachines.init.property.Properties;
import jaminv.advancedmachines.lib.render.ModelBakery;
import jaminv.advancedmachines.lib.render.ModelBakeryProvider;
import jaminv.advancedmachines.machine.TileEntityMachineMultiblock;
import jaminv.advancedmachines.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.objects.blocks.properties.BlockProperties;
import jaminv.advancedmachines.objects.variant.HasVariant;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import jaminv.advancedmachines.util.helper.BlockHelper;
import jaminv.advancedmachines.util.helper.BlockHelper.ScanResult;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMachineExpansionType extends Block implements HasVariant<VariantExpansion>, MachineUpgrade, ModelBakeryProvider {
	
	protected final VariantExpansion variant;
	
	public BlockMachineExpansionType(VariantExpansion variant) {
		super(Material.IRON);
		BlockProperties.MACHINE.apply(this);
		setSoundType(SoundType.METAL);
		this.variant = variant;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityMachineExpansionType();
	}	
	
	@Override
	public VariantExpansion getVariant() { return variant; }
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		scanMultiblock(worldIn, pos);
		BlockHelper.setVariant(worldIn, pos, variant);
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
		BlockStateContainer.Builder builder = new BlockStateContainer.Builder(this);
		return builder.add(Properties.EXPANSION_VARIANT,
				Properties.BORDER_TOP, Properties.BORDER_BOTTOM,
				Properties.BORDER_NORTH, Properties.BORDER_SOUTH,
				Properties.BORDER_EAST, Properties.BORDER_WEST).build();
	}
	
	@Override
	public IExtendedBlockState getExtendedState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		IExtendedBlockState ext = (IExtendedBlockState)state;
        TileEntity tileentity = worldIn instanceof ChunkCache ? ((ChunkCache)worldIn).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : worldIn.getTileEntity(pos);
        MultiblockBorders borders = MultiblockBorders.DEFAULT;

        if (tileentity instanceof TileEntityMachineExpansionType) {
        	TileEntityMachineExpansionType te = (TileEntityMachineExpansionType)tileentity;
        	borders = te.getBorders();
        }
        
        return (IExtendedBlockState) ext.withProperty(Properties.EXPANSION_VARIANT, variant)
        	.withProperty(Properties.BORDER_TOP, borders.getTop()).withProperty(Properties.BORDER_BOTTOM, borders.getBottom())
        	.withProperty(Properties.BORDER_NORTH, borders.getNorth()).withProperty(Properties.BORDER_SOUTH, borders.getSouth())
        	.withProperty(Properties.BORDER_EAST, borders.getEast()).withProperty(Properties.BORDER_WEST, borders.getWest());
	}	
	
	public static void scanMultiblock(World world, BlockPos pos) {
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
		return variant.getMultiplier();
	}
	
	@Override
	public void setMultiblock(World world, BlockPos pos, BlockPos parent, MultiblockBorders borders) {
		TileEntity tileentity = world.getTileEntity(pos);
		if (tileentity instanceof MachineUpgradeTileEntity) {
			((MachineUpgradeTileEntity)tileentity).setBorders(world, borders); 
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.TRANSLUCENT;
	}

	@Override @SideOnly(Side.CLIENT) public ModelBakery getModelBakery() { return new ModelBakeryExpansionType(variant.getName()); }
} 
