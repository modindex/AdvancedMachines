package jaminv.advancedmachines.machine.multiblock;

import javax.annotation.Nullable;

import jaminv.advancedmachines.lib.machine.MachineControllerInterface.SubController;
import jaminv.advancedmachines.lib.util.helper.BlockIterator;
import jaminv.advancedmachines.lib.util.helper.BlockIterator.BlockChecker;
import jaminv.advancedmachines.lib.util.helper.BlockIterator.ScanResult;
import jaminv.advancedmachines.machine.BlockMachine;
import jaminv.advancedmachines.machine.expansion.MachineUpgrade;
import jaminv.advancedmachines.machine.expansion.MachineUpgradeTile;
import jaminv.advancedmachines.machine.multiblock.MultiblockMessage.MultiblockMessageComplete;
import jaminv.advancedmachines.machine.multiblock.MultiblockMessage.MultiblockMessageIllegal;
import jaminv.advancedmachines.machine.multiblock.MultiblockMessage.MultiblockMessageSimple;
import jaminv.advancedmachines.machine.multiblock.face.MachineFace;
import jaminv.advancedmachines.machine.multiblock.face.MachineFaceTile;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;

public class MultiblockBuilder {
	public static class MultiblockChecker implements BlockChecker {
		@Override
		public Action checkBlock(World world, BlockPos pos) {
			Block block = world.getBlockState(pos).getBlock();
			if (block instanceof BlockMachine) { return Action.END; }
			if (block instanceof MachineUpgrade) { return Action.SCAN; }
			return Action.SKIP;
		}
	}	
	
	public static MultiblockState scanMultiblock(World world, BlockPos parent, @Nullable BlockPos blockDestroyed) {
		ScanResult result = BlockIterator.scanBlocks(world, parent, new MultiblockChecker());
		
		// If the machine block was the one that was destroyed, there's no reason to scan, and there's no reason to return a valid MultiblockState
		if (parent.equals(blockDestroyed)) { return new MultiblockState(); }
		
		BlockPos end = result.getEnd();
		if (end != null) {
			return new MultiblockState(new MultiblockMessageIllegal("message.multiblock.connected_machine", 
					world.getBlockState(end).getBlock().getLocalizedName(), end));
		}
		
		BlockPos min = result.getMin(), max = result.getMax();
		
		if (parent.equals(min) && parent.equals(max)) {
			return new MultiblockState(new MultiblockMessageSimple("message.multiblock.absent"));
		}
		
		MultiblockUpgrades.Writable upgrades = new MultiblockUpgrades.Writable();
		
		MutableBlockPos check = new MutableBlockPos();
		for (int x = min.getX(); x <= max.getX(); x++) {
			for (int y = min.getY(); y <= max.getY(); y++) {
				for (int z = min.getZ(); z <= max.getZ(); z++) {
					check.setPos(x, y, z);
					Block block = world.getBlockState(check).getBlock();
					if (block instanceof MachineUpgrade && !check.equals(blockDestroyed)) {
						MachineUpgrade upgrade = (MachineUpgrade)block;
						upgrades.add(upgrade.getUpgradeType(), upgrade.getUpgradeQty(world, check));

						TileEntity te = world.getTileEntity(check);
						if (te instanceof SubController) {
							upgrades.addTool(new BlockPos(check));
						}
					} else {
						return new MultiblockState(new MultiblockMessageIllegal("message.multiblock.illegal", block.getLocalizedName(), check.toImmutable()));
					} 
				}
			}
		}
		
		// Tell all the upgrades that they are now part of a multiblock
		setMultiblock(min, max, true, world);
		
		return new MultiblockState(new MultiblockMessageComplete(upgrades), upgrades, min, max);
	}
	
	public static void setMultiblock(BlockPos min, BlockPos max, boolean isMultiblock, World world) {
		MutableBlockPos upgrade = new MutableBlockPos();
		for (int x = min.getX(); x <= max.getX(); x++) {
			for (int y = min.getY(); y <= max.getY(); y++) {
				for (int z = min.getZ(); z <= max.getZ(); z++) {
					upgrade.setPos(x, y, z);
					//if (pos.equals(upgrade)) { continue; }
					Block block = world.getBlockState(upgrade).getBlock();
					TileEntity te = world.getTileEntity(upgrade);
					
					if (te instanceof MachineUpgradeTile) { 
						MachineUpgradeTile tile = (MachineUpgradeTile)te;
						MultiblockBorders bord;
						
						if (!isMultiblock) { 
							bord = MultiblockBorders.DEFAULT;
							
							if (te instanceof MachineFaceTile) {
								((MachineFaceTile)te).setMachineFace(MachineFace.NONE, MachineType.NONE, EnumFacing.UP, null);
							}
						} else { bord = new MultiblockBorders(world, upgrade, min, max); }
						
						tile.setBorders(bord);
					}
				}
			}
		}
		world.markBlockRangeForRenderUpdate(min, max);
	}
}
