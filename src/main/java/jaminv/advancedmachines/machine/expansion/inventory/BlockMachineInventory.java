package jaminv.advancedmachines.machine.expansion.inventory;

import jaminv.advancedmachines.init.property.Properties;
import jaminv.advancedmachines.lib.render.ModelBakery;
import jaminv.advancedmachines.machine.MachineHelper;
import jaminv.advancedmachines.machine.expansion.BlockMachineExpansion;
import jaminv.advancedmachines.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import jaminv.advancedmachines.proxy.GuiProxy;
import jaminv.advancedmachines.util.helper.BlockHelper;
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

public class BlockMachineInventory extends BlockMachineExpansion {

	public BlockMachineInventory(VariantExpansion variant) {
		super(variant);
	}
	
	protected int getGuiId() { return GuiProxy.MACHINE_INVENTORY; }
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		
		BlockHelper.setDirectional(worldIn, pos, placer);
		BlockHelper.setVariant(worldIn, pos, variant);
	}		 
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		return BlockHelper.openGui(worldIn, pos, playerIn, getGuiId());
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileMachineInventory();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return MachineHelper.addCommonProperties(new BlockStateContainer.Builder(this))
			.add(Properties.INPUT, Properties.FACING)
			.build();
	}
	
	@Override
	public IExtendedBlockState getExtendedState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		TileEntity tileentity = BlockHelper.getTileEntity(worldIn, pos);

        EnumFacing facing = EnumFacing.NORTH;
        boolean input = true;
        MultiblockBorders borders = MultiblockBorders.DEFAULT;

        if (tileentity instanceof TileMachineInventory) {
        	TileMachineInventory te = (TileMachineInventory)tileentity;
        	facing = te.getFacing();
        	input = te.getInputState();
        	borders = te.getBorders();
        }
        
        return (IExtendedBlockState) MachineHelper.withCommonProperties((IExtendedBlockState)state, variant, borders)
        	.withProperty(Properties.FACING, facing).withProperty(Properties.INPUT, input);
	}

	@Override @SideOnly(Side.CLIENT) public ModelBakery getModelBakery() { return new ModelBakeryMachineInventory(variant); }
}
