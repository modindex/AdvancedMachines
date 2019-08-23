package jaminv.advancedmachines.init.property;

import jaminv.advancedmachines.machine.expansion.tank.TileEntityMachineTank;
import jaminv.advancedmachines.machine.multiblock.MultiblockBorderType;
import jaminv.advancedmachines.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.machine.multiblock.face.MachineFace;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import net.minecraft.util.EnumFacing;

public class Properties {
	
	/* 
	 * Although many of these are finite, they need to be unlisted to prevent Minecraft from scanning through every possible combination
	 * There are tens of thousands of possible combinations for some blocks. 
	 */

	public static final UnlistedEnum<MultiblockBorderType> BORDER_TOP 		= new UnlistedEnum<MultiblockBorderType>("border_top", MultiblockBorderType.class);
	public static final UnlistedEnum<MultiblockBorderType> BORDER_BOTTOM 	= new UnlistedEnum<MultiblockBorderType>("border_bottom", MultiblockBorderType.class);
	public static final UnlistedEnum<MultiblockBorderType> BORDER_NORTH 	= new UnlistedEnum<MultiblockBorderType>("border_north", MultiblockBorderType.class);
	public static final UnlistedEnum<MultiblockBorderType> BORDER_SOUTH 	= new UnlistedEnum<MultiblockBorderType>("border_south", MultiblockBorderType.class);
	public static final UnlistedEnum<MultiblockBorderType> BORDER_EAST 		= new UnlistedEnum<MultiblockBorderType>("border_east", MultiblockBorderType.class);
	public static final UnlistedEnum<MultiblockBorderType> BORDER_WEST 		= new UnlistedEnum<MultiblockBorderType>("border_west", MultiblockBorderType.class);
	
	public static final UnlistedEnum<EnumFacing> FACING = new UnlistedEnum<EnumFacing>("facing", EnumFacing.class);
	public static final UnlistedBoolean ACTIVE = new UnlistedBoolean("active");
	
	public static final UnlistedBoolean INPUT = new UnlistedBoolean("input");
	
	public static final UnlistedEnum<MachineType> MACHINE_TYPE = new UnlistedEnum<MachineType>("machine_type", MachineType.class);
	public static final UnlistedEnum<MachineFace> MACHINE_FACE = new UnlistedEnum<MachineFace>("machine_face", MachineFace.class);
	
	public static final UnlistedFluidStack FLUID = new UnlistedFluidStack("fluid");
	public static final UnlistedInteger CAPACITY = new UnlistedInteger("capacity");
}
