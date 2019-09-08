package jaminv.advancedmachines.machine.expansion;

import jaminv.advancedmachines.lib.render.ModelBakeryProvider;
import jaminv.advancedmachines.lib.util.blocks.BlockProperties;
import jaminv.advancedmachines.lib.util.helper.BlockHelper;
import jaminv.advancedmachines.lib.util.helper.BlockIterator;
import jaminv.advancedmachines.lib.util.helper.BlockIterator.ScanResult;
import jaminv.advancedmachines.machine.MachineHelper;
import jaminv.advancedmachines.machine.TileMachineMultiblock;
import jaminv.advancedmachines.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.machine.multiblock.MultiblockBuilder;
import jaminv.advancedmachines.objects.blocks.BlockPropertiesMod;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockMachineExpansion extends Block implements VariantExpansion.HasVariant, MachineUpgrade, ModelBakeryProvider {
	
	protected final VariantExpansion variant;
	
    protected static final BlockProperties props = BlockPropertiesMod.MACHINE;

	public BlockMachineExpansion(VariantExpansion variant) {		
		super(props.getMaterial());
		props.apply(this);
		setSoundType(props.getSoundType());	
		this.variant = variant;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileMachineExpansion();
	}	
	
	@Override
	public VariantExpansion getVariant() { return variant; }
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		scanMultiblock(worldIn, pos, false);
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof TileMachineExpansion) {
			((TileMachineExpansion)te).setVariant(variant);
		}
	}	
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		scanMultiblock(worldIn, pos, true);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return MachineHelper.addCommonProperties(new BlockStateContainer.Builder(this)).build();
	}
	
	@Override
	public IExtendedBlockState getExtendedState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntity tileentity = BlockHelper.getTileEntity(worldIn, pos);
        MultiblockBorders borders = MultiblockBorders.DEFAULT;

        if (tileentity instanceof TileMachineExpansion) {
        	TileMachineExpansion te = (TileMachineExpansion)tileentity;
        	borders = te.getBorders();
        }
        
        return MachineHelper.withCommonProperties((IExtendedBlockState)state, variant, borders);
	}	
	
	public static void scanMultiblock(World world, BlockPos pos, boolean destroy) {
		ScanResult result = BlockIterator.scanBlocks(world, pos, new MultiblockBuilder.MultiblockChecker());
		BlockPos end = result.getEnd();
		if (end == null) { return; }
		
		TileEntity te = world.getTileEntity(end);
		if (te instanceof TileMachineMultiblock) {
			((TileMachineMultiblock)te).scanMultiblock(destroy ? pos : null);
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
	@SideOnly(Side.CLIENT)
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.TRANSLUCENT;
	}
} 
