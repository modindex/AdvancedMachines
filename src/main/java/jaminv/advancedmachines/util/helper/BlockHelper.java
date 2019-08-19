package jaminv.advancedmachines.util.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.Iterables;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.init.property.Properties;
import jaminv.advancedmachines.objects.blocks.machine.TileEntityMachine;
import jaminv.advancedmachines.objects.blocks.machine.expansion.IMachineUpgradeTileEntity;
import jaminv.advancedmachines.objects.blocks.machine.expansion.redstone.TileEntityMachineRedstone;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.util.interfaces.IDirectional;
import jaminv.advancedmachines.util.interfaces.IHasMetadata;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public class BlockHelper {
	
	public abstract static class BlockChecker {
		public static enum Action { SCAN, SKIP, END };
		
		public abstract Action checkBlock(World world, BlockPos pos);
	}
	
	public abstract static class BlockCallback {
		public abstract void callback(World world, BlockPos pos);
	}
	
	public static void iterateBlocks(World world, BlockPos min, BlockPos max, BlockCallback callback) {
		for (int x = min.getX(); x <= max.getX(); x++) {
			for (int y = min.getY(); y <= max.getY(); y++) {
				for (int z = min.getZ(); z <= max.getZ(); z++) {
					callback.callback(world, new BlockPos(x, y, z));
				}
			}
		}
	}
	
	public static String getBlockName(World world, BlockPos pos) {
		return world.getBlockState(pos).getBlock().getLocalizedName();
	}
	
	public static boolean hasProperty(IBlockState state, Object property) {
		return state.getProperties().containsKey(property);
	}
	
	public static boolean hasUnlistedProperty(IExtendedBlockState state, Object property) {
		return state.getUnlistedProperties().containsKey(property);
	}
	
	
	public static EnumFacing getExtendedFacing(IBlockState state) {
		if (!(state instanceof IExtendedBlockState)) { return null; }
		return ((IExtendedBlockState)state).getValue(Properties.FACING);
	}
	
	public static void setMeta(World worldIn, BlockPos pos, ItemStack stack) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof IHasMetadata) {
			((IHasMetadata)te).setMeta(ItemHelper.getMeta(stack));
		}
	}
	
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
		if (te != null && te instanceof IDirectional) {
			((IDirectional)te).setFacing(ret);
		}
		
		return ret;
	}
	
	public static EnumFacing setDirectional(World worldIn, BlockPos pos, EntityLivingBase placer) {
		return setDirectional(worldIn, pos, placer, true);
	}
	
	public static void setBorders(World world, BlockPos pos, MultiblockBorders borders) {
		TileEntity tileentity = world.getTileEntity(pos);
		if (tileentity instanceof IMachineUpgradeTileEntity) {
			((IMachineUpgradeTileEntity)tileentity).setBorders(world, borders);
		}
	}
	
	public static TileEntity getTileEntity(IBlockAccess worldIn, BlockPos pos) {
        return worldIn instanceof ChunkCache ? ((ChunkCache)worldIn).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : worldIn.getTileEntity(pos);
	}
	
	public static class ScanResult {
		protected List<BlockPos> blocks;
		protected int minX, minY, minZ, maxX, maxY, maxZ;
		protected BlockPos end;
		
		public ScanResult(BlockPos pos) {
			blocks = new ArrayList<>();
			minX = maxX = pos.getX();
			minY = maxY = pos.getY();
			minZ = maxZ = pos.getZ();
		}
		
		public void add(BlockPos pos) {
			blocks.add(pos);
			if (minX > pos.getX()) { minX = pos.getX(); }
			if (maxX < pos.getX()) { maxX = pos.getX(); }
			if (minY > pos.getY()) { minY = pos.getY(); }
			if (maxY < pos.getY()) { maxY = pos.getY(); }
			if (minZ > pos.getZ()) { minZ = pos.getZ(); }
			if (maxZ < pos.getZ()) { maxZ = pos.getZ(); }
		}
		
		public List<BlockPos> getBlocks() { return blocks; }
		public int getMinX() { return minX; }
		public int getMaxX() { return maxX; }
		public int getMinY() { return minY; }
		public int getMaxY() { return maxY; }
		public int getMinZ() { return minZ; }
		public int getMaxZ() { return maxZ; }
		public BlockPos getMin() { return new BlockPos(minX, minY, minZ); }
		public BlockPos getMax() { return new BlockPos(maxX, maxY, maxZ); }
		public BlockPos getEnd() { return end; }
	}

	public static ScanResult scanBlocks(World world, BlockPos pos, BlockChecker checker) {
		ScanResult result = new ScanResult(pos);
		Map<BlockPos, Boolean> checked = new HashMap<>();
		checked.put(pos, true);
		
		recurse(world, pos, checker, result, checked, null);
		
		return result;
	}
	
	private static boolean recurse(World world, BlockPos pos, BlockChecker checker, ScanResult result, Map<BlockPos, Boolean> checked, EnumFacing last) {
		result.add(pos);		
		
		for(EnumFacing dir : EnumFacing.VALUES) {
			// Prevent backtracking
			if	((last != null) && (last.getAxis() == dir.getAxis()) && (last.getAxisDirection() != dir.getAxisDirection())) {
				//continue;
			}
			
			BlockPos next = pos.offset(dir);
			if (checked.get(next) != null) { continue; }	// We've already checked this block
			
			checked.put(next, true);
			
			BlockChecker.Action action = checker.checkBlock(world, next);
			
			if (action == BlockChecker.Action.END) {
				result.end = next;
				return false;
			}
			
			if (action == BlockChecker.Action.SCAN) {
				boolean cont = recurse(world, next, checker, result, checked, dir);
				if (!cont) { return false; }
			}
		}
		
		return true;
	}
	
	public static boolean openGui(World world, BlockPos pos, EntityPlayer player, int guiId) {
		if (guiId <= 0) { return false; }
		if (world.isRemote) { return true; }
		player.openGui(Main.instance, guiId, world, pos.getX(), pos.getY(), pos.getZ());
		return true;		
	}	

}
