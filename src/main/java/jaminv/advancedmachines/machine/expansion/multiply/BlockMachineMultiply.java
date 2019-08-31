package jaminv.advancedmachines.machine.expansion.multiply;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.lib.render.ModelBakery;
import jaminv.advancedmachines.machine.MachineHelper;
import jaminv.advancedmachines.machine.expansion.BlockMachineExpansion;
import jaminv.advancedmachines.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.machine.multiblock.face.MachineFace;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.objects.blocks.Properties;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import jaminv.advancedmachines.util.helper.BlockHelper;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMachineMultiply extends BlockMachineExpansion {
	
	public BlockMachineMultiply(VariantExpansion variant) {
		super(variant);
	}
	
	@Override public boolean hasTileEntity(IBlockState state) { return true; }

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileMachineMultiply();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return MachineHelper.addCommonProperties(new BlockStateContainer.Builder(this))
			.add(Properties.MACHINE_FACE, Properties.MACHINE_TYPE)
			.add(Properties.FACING, Properties.ACTIVE)
			.build();
	}
	
	@Override
	public IExtendedBlockState getExtendedState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		TileEntity tileentity = BlockHelper.getTileEntity(worldIn, pos);

        MachineFace face = MachineFace.NONE;
        MachineType parent = MachineType.NONE;
        EnumFacing facing = EnumFacing.UP;
        MultiblockBorders borders = MultiblockBorders.DEFAULT;
        boolean active = false;

        if (tileentity instanceof TileMachineMultiply) {
        	TileMachineMultiply te = (TileMachineMultiply)tileentity;
        	face = te.getMachineFace();
        	parent = te.getMachineParent();
        	facing = te.getFacing();
        	active = te.isActive();
        	borders = te.getBorders();
        }
        
        return (IExtendedBlockState) MachineHelper.withCommonProperties((IExtendedBlockState)state, variant, borders)
        	.withProperty(Properties.MACHINE_FACE, face).withProperty(Properties.MACHINE_TYPE, parent)
        	.withProperty(Properties.FACING, facing).withProperty(Properties.ACTIVE, active);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof TileMachineMultiply) {
			TileMachineMultiply exp = (TileMachineMultiply)te;
			if (exp.getMachineFace() == MachineFace.NONE) { return false; }

			BlockPos machine = exp.getParentPos();
			if (worldIn.isRemote) { return true; }
			playerIn.openGui(Main.instance, exp.getMachineParent().getGuiId(), worldIn, machine.getX(), machine.getY(), machine.getZ());
			return true;
		}
		
		return false;
	}

	@Override @SideOnly(Side.CLIENT) public ModelBakery getModelBakery() { return new ModelBakeryMachineMultiply(variant); }
}
