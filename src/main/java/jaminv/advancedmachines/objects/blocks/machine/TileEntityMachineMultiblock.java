package jaminv.advancedmachines.objects.blocks.machine;

import org.apache.logging.log4j.Level;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.objects.blocks.machine.expansion.BlockMachineExpansion;
import jaminv.advancedmachines.util.recipe.IRecipeManager;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;

public abstract class TileEntityMachineMultiblock extends TileEntityMachineBase {

	public TileEntityMachineMultiblock(IRecipeManager recipeManager) {
		super(recipeManager);
	}

	public static abstract class MultiblockState {
		public abstract String toString();
	}
	public static class MultiblockAbsent extends MultiblockState {
		@Override
		public String toString() {
			return I18n.format("message.multiblock.absent");
		}
	}
	public static class MultiblockIncomplete extends MultiblockState {
		@Override
		public String toString() {
			return I18n.format("message.multiblock.incomplete");
		}
	}
	public static class MultiblockIllegal extends MultiblockState {
		protected String name;
		protected BlockPos pos;
		public MultiblockIllegal(String name, BlockPos pos) {
			this.name = name; this.pos = pos;
		}
		
		@Override
		public String toString() {
			return I18n.format("message.multiblock.illegal", name, pos.getX(), pos.getY(), pos.getZ());
		}
	}
	
	public static class MultiblockComplete extends MultiblockState {
		@Override
		public String toString() {
			return I18n.format("message.multiblock.complete");
		}		
	}
	
	public MultiblockState scanMultiblock() {
		BlockPos pos = this.getPos();
		
		// Up, south, and east are all positive
		BlockPos corner1 = scan(pos, EnumFacing.UP);
		corner1 = scan(corner1, EnumFacing.SOUTH);
		corner1 = scan(corner1, EnumFacing.EAST);
		
		BlockPos corner2 = scan(corner1, EnumFacing.DOWN);
		corner2 = scan(corner2, EnumFacing.NORTH);
		corner2 = scan(corner2, EnumFacing.WEST);
		
		if (pos.equals(corner1) && pos.equals(corner2)) {
			return new MultiblockAbsent();
		}
		if (pos.getX() > corner1.getX() || pos.getX() < corner2.getX()
			|| pos.getY() > corner1.getY() || pos.getY() < corner2.getY()
			|| pos.getZ() > corner1.getZ() || pos.getZ() < corner2.getZ()
		) {
			return new MultiblockIncomplete();
		}
		
		MutableBlockPos check = new MutableBlockPos();
		for (int x = corner2.getX(); x <= corner1.getX(); x++) {
			for (int y = corner2.getY(); y <= corner1.getY(); y++) {
				for (int z = corner2.getZ(); z <= corner1.getZ(); z++) {
					check.setPos(x, y, z);
					if (pos.equals(check)) { continue; }
					Block block = world.getBlockState(check).getBlock();
					if (!(block instanceof BlockMachineExpansion)) {
						return new MultiblockIllegal(block.getLocalizedName(), check.toImmutable());
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
