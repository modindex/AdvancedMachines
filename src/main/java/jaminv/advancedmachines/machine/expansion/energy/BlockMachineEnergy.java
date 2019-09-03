package jaminv.advancedmachines.machine.expansion.energy;

import jaminv.advancedmachines.AdvancedMachines;
import jaminv.advancedmachines.init.GuiProxy;
import jaminv.advancedmachines.lib.render.ModelBakery;
import jaminv.advancedmachines.lib.util.helper.BlockHelper;
import jaminv.advancedmachines.machine.MachineHelper;
import jaminv.advancedmachines.machine.expansion.BlockMachineExpansion;
import jaminv.advancedmachines.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.objects.blocks.Properties;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMachineEnergy extends BlockMachineExpansion {
	
    public static final PropertyDirection FACING = PropertyDirection.create("facing");

	public BlockMachineEnergy(VariantExpansion variant) {
		super(variant);
	}
	
	protected int getGuiId() { return GuiProxy.MACHINE_ENERGY; }
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		
		BlockHelper.setDirectional(worldIn, pos, placer);
	}	 
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		return BlockHelper.openGui(AdvancedMachines.instance, worldIn, pos, playerIn, getGuiId());
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileMachineEnergy();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return MachineHelper.addCommonProperties(new BlockStateContainer.Builder(this)
			.add(Properties.FACING)).build();
	}
	
	@Override
	public IExtendedBlockState getExtendedState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		TileEntity tileentity = BlockHelper.getTileEntity(worldIn, pos);

        EnumFacing facing = EnumFacing.NORTH;
        MultiblockBorders borders = MultiblockBorders.DEFAULT;

        if (tileentity instanceof TileMachineEnergy) {
        	TileMachineEnergy te = (TileMachineEnergy)tileentity;
        	facing = te.getFacing();
        	borders = te.getBorders();
        }
        
        return (IExtendedBlockState) MachineHelper.withCommonProperties((IExtendedBlockState)state, variant, borders)
        	.withProperty(Properties.FACING, facing);
	}

	@Override @SideOnly(Side.CLIENT) public ModelBakery getModelBakery() { return new ModelBakeryMachineEnergy(variant); }
}
