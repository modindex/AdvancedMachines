package jaminv.advancedmachines.machine.expansion.tank;

import java.util.List;

import jaminv.advancedmachines.AdvancedMachines;
import jaminv.advancedmachines.ModConfig;
import jaminv.advancedmachines.init.GuiProxy;
import jaminv.advancedmachines.lib.fluid.FluidTankAdvanced;
import jaminv.advancedmachines.lib.render.ModelBakery;
import jaminv.advancedmachines.lib.util.blocks.HasItemNBT;
import jaminv.advancedmachines.lib.util.helper.BlockHelper;
import jaminv.advancedmachines.machine.MachineHelper;
import jaminv.advancedmachines.machine.expansion.BlockMachineExpansion;
import jaminv.advancedmachines.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.objects.blocks.Properties;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMachineTank extends BlockMachineExpansion {

	public BlockMachineTank(VariantExpansion variant) {
		super(variant);
	}

	protected int getGuiId() { return GuiProxy.MACHINE_TANK; }
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof TileMachineTank) {
			if (((TileMachineTank)te).onBlockActivated(playerIn, hand)) {
				return true;
			}
		}
		
		return BlockHelper.openGui(AdvancedMachines.instance, worldIn, pos, playerIn, getGuiId());
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		
		HasItemNBT.placeItem(worldIn, pos, stack);
		BlockHelper.setDirectional(worldIn, pos, placer);
	}		
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileMachineTank();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return MachineHelper.addCommonProperties(new BlockStateContainer.Builder(this))
			.add(Properties.FLUID, Properties.CAPACITY)
			.add(Properties.INPUT, Properties.FACING)
			.build();
	}

	@Override
	public IExtendedBlockState getExtendedState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntity tileentity = BlockHelper.getTileEntity(worldIn, pos);

        EnumFacing facing = EnumFacing.NORTH;
        boolean input = true;
        MultiblockBorders borders = MultiblockBorders.DEFAULT;
        FluidStack fluid = null;
        int capacity = 0;

        if (tileentity instanceof TileMachineTank) {
        	TileMachineTank te = (TileMachineTank)tileentity;
        	facing = te.getFacing();
        	input = te.getInputState();
        	borders = te.getBorders();
        	fluid = te.getTank().getFluid();
        	capacity = te.getTank().getCapacity();
        }
        
        return (IExtendedBlockState) MachineHelper.withCommonProperties((IExtendedBlockState)state, variant, borders)
    		.withProperty(Properties.FLUID, fluid).withProperty(Properties.CAPACITY, capacity)
        	.withProperty(Properties.FACING, facing).withProperty(Properties.INPUT, input);
	}
	
	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player,
			boolean willHarvest) {
		if (willHarvest) { return true; } // If it will harvest, delay deletion of the block until after getDrops
		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state,
			int fortune) {
		super.getDrops(drops, world, pos, state, fortune);
		HasItemNBT.getDrops(drops, world, pos, BlockMachineTank.class);
	}
	
	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te,
			ItemStack stack) {
		super.harvestBlock(worldIn, player, pos, state, te, stack);
        worldIn.setBlockToAir(pos);
	}
        
	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		super.addInformation(stack, player, tooltip, advanced);
		if (stack.hasTagCompound()) {
			FluidTankAdvanced tank = new FluidTankAdvanced(
					ModConfig.general.defaultMachineFluidCapacity * variant.getMultiplier(),
					ModConfig.general.defaultMachineFluidTransfer * variant.getMultiplier());
			tank.deserializeNBT(stack.getTagCompound().getCompoundTag("tank"));
			tank.addInformation(tooltip, advanced);
		}
	}
	
	@Override
	@SideOnly (Side.CLIENT)
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.TRANSLUCENT;
	}
	
	// %1.13% Remove
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	/* %1.13%
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}
	*/

	@Override @SideOnly(Side.CLIENT) public ModelBakery getModelBakery() { return new ModelBakeryMachineTank(variant); }
}
