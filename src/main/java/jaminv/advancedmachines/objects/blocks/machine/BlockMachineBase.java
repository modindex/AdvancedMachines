package jaminv.advancedmachines.objects.blocks.machine;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.objects.blocks.DirectionalBlock;
import jaminv.advancedmachines.util.interfaces.IHasTileEntity;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockMachineBase extends DirectionalBlock implements ITileEntityProvider, IHasTileEntity {

	public BlockMachineBase(String name) {
		super(name, Material.IRON);
	}
	
	protected int getGuiId() { return -1; }
		
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		int gui_id = getGuiId();
		if (gui_id <= 0) { return false; }
		if (worldIn.isRemote) { return true; }
		TileEntity te = worldIn.getTileEntity(pos);
		if (!(te instanceof TileEntityMachineBase)) {
			return false;
		}
		playerIn.openGui(Main.instance, gui_id, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;		
	}
}
