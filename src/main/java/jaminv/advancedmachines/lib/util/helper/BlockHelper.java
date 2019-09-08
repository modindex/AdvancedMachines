package jaminv.advancedmachines.lib.util.helper;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class BlockHelper {
	
	public static EnumFacing getXZFacing(EntityLivingBase living) {

		int quadrant = MathHelper.floor(living.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

		switch (quadrant) {
			case 0:
				return EnumFacing.NORTH;
			case 1:
				return EnumFacing.EAST;
			case 2:
				return EnumFacing.SOUTH;
			case 3:
				return EnumFacing.WEST;
		}
		return EnumFacing.EAST;
	}
	
	public static EnumFacing setDirectional(World worldIn, BlockPos pos, EntityLivingBase placer, boolean AllowUpDown) {
		EnumFacing ret;
		if (AllowUpDown) {
			ret = EnumFacing.getDirectionFromEntityLiving(pos, placer);
		} else {
			ret = getXZFacing(placer);
		}
		
		TileEntity te = worldIn.getTileEntity(pos);
		if (te != null && te instanceof HasFacing) {
			((HasFacing)te).setFacing(ret);
		}
		
		return ret;
	}
	
	public static EnumFacing setDirectional(World worldIn, BlockPos pos, EntityLivingBase placer) {
		return setDirectional(worldIn, pos, placer, true);
	}
	
	/**
	 * Get Tile Entity from world position
	 * 
	 * Intended for use in Block.getExtendedState(), where you need to check if world is a ChunkCache
	 * @param worldIn
	 * @param pos
	 * @return
	 */
	public static TileEntity getTileEntity(IBlockAccess worldIn, BlockPos pos) {
        return worldIn instanceof ChunkCache ? ((ChunkCache)worldIn).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : worldIn.getTileEntity(pos);
	}
	
	public static boolean openGui(Object mod, World world, BlockPos pos, EntityPlayer player, int guiId) {
		if (guiId <= 0) { return false; }
		if (world.isRemote) { return true; }
		player.openGui(mod, guiId, world, pos.getX(), pos.getY(), pos.getZ());
		return true;		
	}	

}
