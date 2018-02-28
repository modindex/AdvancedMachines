package jaminv.advancedmachines.objects.blocks.machine.multiblock;

import jaminv.advancedmachines.objects.blocks.machine.expansion.IMachineUpgrade;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

public class MultiblockBorders implements INBTSerializable<NBTTagCompound> {
	protected boolean top = true, bottom = true, north = true, south = true, east = true, west = true;
	
	public static final MultiblockBorders DEFAULT = new MultiblockBorders();
	
	public MultiblockBorders() { }
	
	public MultiblockBorders(BlockPos pos, BlockPos min, BlockPos max) {
		if (max.getY() == pos.getY()) { top = true; } else { top = false; }
		if (min.getY() == pos.getY()) { bottom = true; } else { bottom = false; }
		if (min.getZ() == pos.getZ()) { north = true; } else { north = false; }
		if (max.getZ() == pos.getZ()) { south = true; } else { south = false; }
		if (max.getX() == pos.getX()) { east = true; } else { east = false; }
		if (min.getX() == pos.getX()) { west = true; } else { west = false; }
	}
	
	public boolean getTop() { return top; }
	public boolean getBottom() { return bottom; }
	public boolean getNorth() { return north; }
	public boolean getSouth() { return south; }
	public boolean getEast() { return east; }
	public boolean getWest() { return west; }
	
    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean("top", top);
        nbt.setBoolean("bottom", bottom);
        nbt.setBoolean("north", north);
        nbt.setBoolean("south", south);
        nbt.setBoolean("east", east);
        nbt.setBoolean("west", west);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
    	if (nbt.hasKey("top")) { top = nbt.getBoolean("top"); }
    	if (nbt.hasKey("bottom")) { bottom = nbt.getBoolean("bottom"); }
    	if (nbt.hasKey("north")) { north = nbt.getBoolean("north"); }
    	if (nbt.hasKey("south")) { south = nbt.getBoolean("south"); }
    	if (nbt.hasKey("east")) { east = nbt.getBoolean("east"); }
    	if (nbt.hasKey("west")) { west = nbt.getBoolean("west"); }
    }	
}
