package jaminv.advancedmachines.objects.blocks.machine.multiblock;

import jaminv.advancedmachines.objects.blocks.machine.TileEntityMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.expansion.BlockMachineExpansion;
import jaminv.advancedmachines.util.helper.BlockHelper;
import jaminv.advancedmachines.util.helper.BlockHelper.BlockChecker;
import jaminv.advancedmachines.util.helper.BlockHelper.ScanResult;
import jaminv.advancedmachines.util.recipe.IRecipeManager;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;

public abstract class TileEntityMachineMultiblock extends TileEntityMachineBase {

	public TileEntityMachineMultiblock(IRecipeManager recipeManager) {
		super(recipeManager);
	}

	public static abstract class MultiblockState {
		public abstract String toString();
	}
	public static class MultiblockSimple extends MultiblockState {
		protected final String message;
		public MultiblockSimple(String message) {
			this.message = message;
		}
		
		@Override
		public String toString() {
			return I18n.format(message);
		}
	}
	public static class MultiblockIllegal extends MultiblockState {
		protected final String message;
		protected final String name;
		protected final BlockPos pos;
		public MultiblockIllegal(String message, String name, BlockPos pos) {
			this.message = message;
			this.name = name;
			this.pos = pos;
		}
		
		@Override
		public String toString() {
			return I18n.format(message, name, pos.getX(), pos.getY(), pos.getZ());
		}
	}
	
	public static class MultiblockComplete extends MultiblockState {
		@Override
		public String toString() {
			return I18n.format("message.multiblock.complete");
		}		
	}
	
	public class MultiblockChecker extends BlockChecker {
		@Override
		public Action checkBlock(World world, BlockPos pos) {
			Block block = world.getBlockState(pos).getBlock();
			if (block instanceof BlockMachineExpansion) { return Action.SCAN; }
			if (block instanceof BlockMachineMultiblock) { return Action.END; }
			return Action.SKIP;
		}
	}
	
	public MultiblockState scanMultiblock() {
		BlockPos pos = this.getPos();
		
		ScanResult result = BlockHelper.scanBlocks(world, pos, new MultiblockChecker());
		
		BlockPos end = result.getEnd();
		if (end != null) {
			return new MultiblockIllegal("message.multiblock.connected_machine", BlockHelper.getBlockName(world, end), end);
		}
		
		BlockPos min = result.getMin(), max = result.getMax();
		
		if (pos.equals(min) && pos.equals(max)) {
			return new MultiblockSimple("message.multiblock.absent");
		}
		
		MutableBlockPos check = new MutableBlockPos();
		for (int x = min.getX(); x <= max.getX(); x++) {
			for (int y = min.getY(); y <= max.getY(); y++) {
				for (int z = min.getZ(); z <= max.getZ(); z++) {
					check.setPos(x, y, z);
					if (pos.equals(check)) { continue; }
					Block block = world.getBlockState(check).getBlock();
					if (!(block instanceof BlockMachineExpansion)) {
						return new MultiblockIllegal("message.multiblock.illegal", block.getLocalizedName(), check.toImmutable());
					}
				}
			}
		}
		
		return new MultiblockComplete();
	}
	
	protected BlockPos scan(BlockPos pos, EnumFacing facing) {
		BlockPos temp = pos, ret;
		do {
			ret = temp;
			temp = temp.offset(facing);
		} while(world.getBlockState(temp).getBlock() instanceof BlockMachineExpansion);
		return ret;
	}	
}
