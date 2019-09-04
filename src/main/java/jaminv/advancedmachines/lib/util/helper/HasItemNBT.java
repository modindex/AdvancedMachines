package jaminv.advancedmachines.lib.util.helper;

import net.minecraft.nbt.NBTTagCompound;

public interface HasItemNBT {
	public void readItemNBT(NBTTagCompound compound);
	public NBTTagCompound writeItemNBT(NBTTagCompound compound);
}
