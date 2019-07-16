package jaminv.advancedmachines.objects.blocks.machine.expansion;

import java.util.HashMap;
import java.util.Map;

import jaminv.advancedmachines.objects.blocks.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.TileEntityMachineMultiblock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IMachineUpgrade {

	public static enum UpgradeType { 
		MULTIPLY(0, "multiply", "machine.upgrade.multiply"),
		SPEED(1, "speed", "machine.upgrade.speed"),
		PRODUCTIVITY(2, "productivity", "machine.upgrade.productivity");
		
		private final int id;
		private final String unlocalizedName;
		private final String name;
		
		public String getName() { return name; }
		
		private static Map<String, UpgradeType>lookup = new HashMap<>();
		public static UpgradeType getType(String name) { return lookup.get(name); }
		
		private UpgradeType(int id, String name, String unlocalizedName) {
			this.id = id; this.name = name; this.unlocalizedName = unlocalizedName;
		}
		
		static {
			for (UpgradeType type : values()) {
				lookup.put(type.name, type);
			}
		}
				
	};
	
	public UpgradeType getUpgradeType();
	public int getUpgradeQty(World world, BlockPos pos);	
	
	public void setMultiblock(World world, BlockPos pos, BlockPos parent, MultiblockBorders borders);
}
