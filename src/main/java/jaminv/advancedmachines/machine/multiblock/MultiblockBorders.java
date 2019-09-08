package jaminv.advancedmachines.machine.multiblock;

import jaminv.advancedmachines.machine.BlockMachineMultiblock;
import jaminv.advancedmachines.machine.expansion.MachineUpgrade;
import jaminv.advancedmachines.objects.blocks.Properties;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.util.INBTSerializable;

public class MultiblockBorders implements INBTSerializable<NBTTagCompound> {
	protected MultiblockBorderType top = MultiblockBorderType.SOLID,
		bottom = MultiblockBorderType.SOLID,
		north = MultiblockBorderType.SOLID,
		south = MultiblockBorderType.SOLID,
		east = MultiblockBorderType.SOLID,
		west = MultiblockBorderType.SOLID;
	
	public static final MultiblockBorders DEFAULT = new MultiblockBorders();
	
	public MultiblockBorders() { }
	
	public MultiblockBorders(IExtendedBlockState state) {
		bottom = state.getValue(Properties.BORDER_BOTTOM);
		top = state.getValue(Properties.BORDER_TOP);
		north = state.getValue(Properties.BORDER_NORTH);
		south = state.getValue(Properties.BORDER_SOUTH);
		east = state.getValue(Properties.BORDER_EAST);
		west = state.getValue(Properties.BORDER_WEST);		
	}
	
	public MultiblockBorders(World world, BlockPos pos, BlockPos min, BlockPos max) {
		if (max.getY() == pos.getY()) { top = MultiblockBorderType.SOLID; } else { top = MultiblockBorderType.NONE; }
		if (min.getY() == pos.getY()) { bottom = MultiblockBorderType.SOLID; } else { bottom = MultiblockBorderType.NONE; }
		if (min.getZ() == pos.getZ()) { north = MultiblockBorderType.SOLID; } else { north = MultiblockBorderType.NONE; }
		if (max.getZ() == pos.getZ()) { south = MultiblockBorderType.SOLID; } else { south = MultiblockBorderType.NONE; }
		if (max.getX() == pos.getX()) { east = MultiblockBorderType.SOLID; } else { east = MultiblockBorderType.NONE; }
		if (min.getX() == pos.getX()) { west = MultiblockBorderType.SOLID; } else { west = MultiblockBorderType.NONE; }
		
		Block current = world.getBlockState(pos).getBlock();
		if (current instanceof VariantExpansion.HasVariant) {
			VariantExpansion variant = ((VariantExpansion.HasVariant)current).getVariant();
			
			// Check for single borders
			
			for (EnumFacing facing : EnumFacing.VALUES) {
				Block check = world.getBlockState(pos.offset(facing)).getBlock();
				
				// If block isn't a machine or upgrade, there's no chance of a single border.
				if (!(check instanceof MachineUpgrade) && !(check instanceof BlockMachineMultiblock)) { continue; }
				// They all should be MaterialType.EXPANSION, but better to make sure.
				if (!(check instanceof VariantExpansion.HasVariant)) { continue; }
				
				MultiblockBorderType border = MultiblockBorderType.NONE;

				// Variant types don't match 
				if (((VariantExpansion.HasVariant)check).getVariant() != variant) {
					border = MultiblockBorderType.SINGLE;
				}

				// Upgrade types don't match								
				if (check instanceof MachineUpgrade && current instanceof MachineUpgrade) {
					if (((MachineUpgrade)current).getUpgradeType() != ((MachineUpgrade)check).getUpgradeType()) { border = MultiblockBorderType.SINGLE; }
				}				
				
				// One block is a machine and the other isn't a MULTIPLY-type expansion				
				if (check instanceof MachineUpgrade && !(current instanceof MachineUpgrade)) {
					if (((MachineUpgrade)check).getUpgradeType() != MachineUpgrade.UpgradeType.MULTIPLY) { border = MultiblockBorderType.SINGLE; }
				}
				
				if (current instanceof MachineUpgrade && !(check instanceof MachineUpgrade)) {
					if (((MachineUpgrade)current).getUpgradeType() != MachineUpgrade.UpgradeType.MULTIPLY) { border = MultiblockBorderType.SINGLE; }
				}
					
				if (border != MultiblockBorderType.NONE) {		
					switch (facing) {
					case UP:
						top = border; break;
					case DOWN:
						bottom = border; break;
					case NORTH:
						north = border; break;
					case SOUTH:
						south = border; break;
					case WEST:
						west = border; break;
					case EAST:
						east = border; break;							
					}
				}
			}
		}
	}
	
	public MultiblockBorderType getTop() { return top; }
	public MultiblockBorderType getBottom() { return bottom; }
	public MultiblockBorderType getNorth() { return north; }
	public MultiblockBorderType getSouth() { return south; }
	public MultiblockBorderType getEast() { return east; }
	public MultiblockBorderType getWest() { return west; }
	
	public MultiblockBorderType get(EnumFacing dir) {
		switch (dir) {
		case UP: return top;
		case DOWN: return bottom;
		case NORTH: return north;
		case SOUTH: return south;
		case EAST: return east;
		case WEST: return west;
		default: return top;
		}
	}
	
    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("top", top.getName());
        nbt.setString("bottom", bottom.getName());
        nbt.setString("north", north.getName());
        nbt.setString("south", south.getName());
        nbt.setString("east", east.getName());
        nbt.setString("west", west.getName());
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
    	if (nbt.hasKey("top")) { top = MultiblockBorderType.lookup(nbt.getString("top")); }
    	if (nbt.hasKey("bottom")) { bottom = MultiblockBorderType.lookup(nbt.getString("bottom")); }
    	if (nbt.hasKey("north")) { north = MultiblockBorderType.lookup(nbt.getString("north")); }
    	if (nbt.hasKey("south")) { south = MultiblockBorderType.lookup(nbt.getString("south")); }
    	if (nbt.hasKey("east")) { east = MultiblockBorderType.lookup(nbt.getString("east")); }
    	if (nbt.hasKey("west")) { west = MultiblockBorderType.lookup(nbt.getString("west")); }
    	
    	if (top == null) { top = MultiblockBorderType.SOLID; }
    	if (bottom == null) { bottom = MultiblockBorderType.SOLID; }
    	if (north == null) { north = MultiblockBorderType.SOLID; }
    	if (south == null) { south = MultiblockBorderType.SOLID; }
    	if (east == null) { east = MultiblockBorderType.SOLID; }
    	if (west == null) { west = MultiblockBorderType.SOLID; }
    }
}
