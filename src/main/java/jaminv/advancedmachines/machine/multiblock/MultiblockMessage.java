package jaminv.advancedmachines.machine.multiblock;

import jaminv.advancedmachines.machine.expansion.MachineUpgrade;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;

public interface MultiblockMessage {
	
	public String toString();	
	
	public static class MultiblockMessageSimple implements MultiblockMessage {
		protected final String message;
		public MultiblockMessageSimple(String message) {
			this.message = message;
		}
		
		@Override
		public String toString() {
			return I18n.format(message);
		}
	}
	
	public static class MultiblockMessageIllegal implements MultiblockMessage {
		protected final String message;
		protected final String name;
		protected final BlockPos pos;
		public MultiblockMessageIllegal(String message, String name, BlockPos pos) {
			this.message = message;
			this.name = name;
			this.pos = pos;
		}
		
		@Override
		public String toString() {
			return I18n.format(message, name, pos.getX(), pos.getY(), pos.getZ());
		}
	}
	
	public static class MultiblockMessageComplete implements MultiblockMessage {
		protected MultiblockUpgrades upgrades;
		
		public MultiblockMessageComplete(MultiblockUpgrades upgrades) {
			this.upgrades = upgrades;
		}
		
		@Override
		public String toString() {
			String ret = I18n.format("message.multiblock.complete", 
				upgrades.get(MachineUpgrade.UpgradeType.MULTIPLY),
				upgrades.get(MachineUpgrade.UpgradeType.SPEED),
				upgrades.get(MachineUpgrade.UpgradeType.PRODUCTIVITY)
			);
			
			return ret;
		}
	}
}
