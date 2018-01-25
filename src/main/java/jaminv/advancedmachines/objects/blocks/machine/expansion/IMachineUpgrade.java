package jaminv.advancedmachines.objects.blocks.machine.expansion;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IMachineUpgrade {

	public static enum UpgradeType { 
		MULTIPLY(0, "machine.upgrade.multiply"),
		SPEED(1, "machine.upgrade.speed");
		
		private final int id;
		private final String unlocalizedName;
		private UpgradeType(int id, String name) {
			this.id = id; this.unlocalizedName = name;
		}
	};
	
	public UpgradeType getUpgradeType();
	public int getUpgradeQty(World world, BlockPos pos);
}
