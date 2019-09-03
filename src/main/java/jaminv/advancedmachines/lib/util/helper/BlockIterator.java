package jaminv.advancedmachines.lib.util.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockIterator {
	
	public static interface BlockChecker {
		public static enum Action { SCAN, SKIP, END };
		
		public Action checkBlock(World world, BlockPos pos);
	}
	
	public static interface BlockCallback {
		public void callback(World world, BlockPos pos);
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
}
