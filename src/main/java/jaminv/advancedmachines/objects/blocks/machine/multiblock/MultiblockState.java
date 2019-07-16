package jaminv.advancedmachines.objects.blocks.machine.multiblock;

import jaminv.advancedmachines.objects.blocks.machine.expansion.IMachineUpgrade;
import jaminv.advancedmachines.util.helper.BlockHelper.BlockChecker;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class MultiblockState {
	public abstract String toString();
	
	public static class MultiblockNull extends MultiblockState {
		@Override
		public String toString() {
			return "message.multiblock.null";
		}
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
		protected UpgradeManager upgrades;
		public MultiblockComplete(UpgradeManager upgrades) {
			this.upgrades = upgrades;
		}
		
		@Override
		public String toString() {
			String ret = I18n.format("message.multiblock.complete", 
				upgrades.get(IMachineUpgrade.UpgradeType.MULTIPLY),
				upgrades.get(IMachineUpgrade.UpgradeType.SPEED),
				upgrades.get(IMachineUpgrade.UpgradeType.PRODUCTIVITY)
			);
			
			return ret;
		}		
	}
}
