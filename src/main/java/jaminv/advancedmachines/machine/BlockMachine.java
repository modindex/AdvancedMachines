package jaminv.advancedmachines.machine;

import jaminv.advancedmachines.init.property.Properties;
import jaminv.advancedmachines.machine.expansion.MachineUpgrade;
import jaminv.advancedmachines.objects.variant.HasVariant;
import jaminv.advancedmachines.objects.variant.MaterialBase;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import jaminv.advancedmachines.util.helper.BlockHelper;
import jaminv.advancedmachines.util.interfaces.IHasTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;

/**
 * I'm not sure that this class is strictly necessary.
 * There are currently no objects that derive from this class.
 * 
 * Potential TODO: Push this class into BlockMachineMultiblock
 */

public abstract class BlockMachine extends Block implements HasVariant<VariantExpansion> {
	
    protected VariantExpansion variant;

	public BlockMachine(VariantExpansion variant) {		
		super(Material.IRON);
		setCreativeTab(CreativeTabs.DECORATIONS);
		setHardness(3.5f);
		setSoundType(SoundType.STONE);
		
		this.variant = variant;
	}

	protected abstract int getGuiId();
	
	public VariantExpansion getVariant() { return variant; }
		
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		return BlockHelper.openGui(worldIn, pos, playerIn, getGuiId());
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		
		BlockHelper.setDirectional(worldIn, pos, placer, false);
		BlockHelper.setVariant(worldIn, pos, variant);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		BlockStateContainer.Builder builder = new BlockStateContainer.Builder(this);
		return builder.add(Properties.EXPANSION_VARIANT)
			.add(Properties.FACING, Properties.ACTIVE)
			.build();
	}	
	
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		IExtendedBlockState ext = (IExtendedBlockState)state;		
        TileEntity tileentity = BlockHelper.getTileEntity(worldIn, pos);

        VariantExpansion variant = VariantExpansion.BASIC;
        EnumFacing facing = EnumFacing.NORTH;
       	boolean active = false;

        if (tileentity instanceof TileEntityMachine) {
        	TileEntityMachine te = (TileEntityMachine)tileentity;
        	variant = te.getVariant();
        	facing = te.getFacing();
        	active = te.isProcessing();
        }
        
        return (IExtendedBlockState) ext.withProperty(Properties.EXPANSION_VARIANT, variant)
            	.withProperty(Properties.FACING, facing).withProperty(Properties.ACTIVE, active);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
}
