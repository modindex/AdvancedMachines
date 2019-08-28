package jaminv.advancedmachines.machine.expansion;

import jaminv.advancedmachines.init.property.Properties;
import jaminv.advancedmachines.lib.render.ModelBakery;
import jaminv.advancedmachines.lib.render.ModelBakeryProvider;
import jaminv.advancedmachines.machine.MachineHelper;
import jaminv.advancedmachines.machine.TileMachineMultiblock;
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

public abstract class BlockMachineExpansion extends Block implements VariantExpansion.Has, MachineUpgrade, ModelBakeryProvider {
	
	protected final VariantExpansion variant;
	
	public BlockMachineExpansion(VariantExpansion variant) {
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
		return new TileMachineExpansion();
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
	
	public static void scanMultiblock(World world, BlockPos pos) {
		ScanResult result = BlockHelper.scanBlocks(world, pos, new TileMachineMultiblock.MultiblockChecker());
		BlockPos end = result.getEnd();
		if (end == null) { return; }
		
		TileEntity te = world.getTileEntity(end);
		if (te instanceof TileMachineMultiblock) {
			((TileMachineMultiblock)te).scanMultiblock();
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
		if (tileentity instanceof MachineUpgradeTile) {
			((MachineUpgradeTile)tileentity).setBorders(world, borders); 
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.TRANSLUCENT;
	}
} 
