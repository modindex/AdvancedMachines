package jaminv.advancedmachines.machine.multiblock;

import javax.annotation.Nullable;

import jaminv.advancedmachines.machine.expansion.MachineUpgrade;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface MultiblockState {
	@SideOnly (Side.CLIENT)
	public abstract String getMultiblockString();
	
	public default @Nullable BlockPos getMultiblockMin() { return null; }
	public default @Nullable BlockPos getMultiblockMax() { return null; }
	public default MultiblockUpgrades getUpgrades() { return MultiblockUpgrades.EMPTY; }
	
	public default boolean isValid() { return false; }
	
	public static class MultiblockNull implements MultiblockState {
		@Override
		public String getMultiblockString() {
			return I18n.format("message.multiblock.null");
		}
	}

	public static class MultiblockSimple implements MultiblockState {
		protected final String message;
		public MultiblockSimple(String message) {
			this.message = message;
		}
		
		@Override
		public String getMultiblockString() {
			return I18n.format(message);
		}
	}
	
	public static class MultiblockIllegal implements MultiblockState {
		protected final String message;
		protected final String name;
		protected final BlockPos pos;
		public MultiblockIllegal(String message, String name, BlockPos pos) {
			this.message = message;
			this.name = name;
			this.pos = pos;
		}
		
		@Override
		public String getMultiblockString() {
			return I18n.format(message, name, pos.getX(), pos.getY(), pos.getZ());
		}
	}
	
	public static class MultiblockComplete implements MultiblockState {
		protected MultiblockUpgrades upgrades;
		protected BlockPos multiblockMin = null, multiblockMax = null;
		
		public MultiblockComplete(MultiblockUpgrades upgrades, BlockPos multiblockMin, BlockPos multiblockMax) {
			this.upgrades = upgrades;
			this.multiblockMin = multiblockMin;
			this.multiblockMax = multiblockMax;
		}
		
		@Override
		public String getMultiblockString() {
			String ret = I18n.format("message.multiblock.complete", 
				upgrades.get(MachineUpgrade.UpgradeType.MULTIPLY),
				upgrades.get(MachineUpgrade.UpgradeType.SPEED),
				upgrades.get(MachineUpgrade.UpgradeType.PRODUCTIVITY)
			);
			
			return ret;
		}
		
		public boolean isValid() { return true; }

		@Override public BlockPos getMultiblockMin() { return multiblockMin; }
		@Override public BlockPos getMultiblockMax() { return multiblockMax; }

		@Override
		public MultiblockUpgrades getUpgrades() {
			return upgrades;
		}
	}
}