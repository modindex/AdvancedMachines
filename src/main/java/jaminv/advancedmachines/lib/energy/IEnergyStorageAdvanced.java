package jaminv.advancedmachines.lib.energy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface IEnergyStorageAdvanced extends IEnergyStorageInternal, IEnergyObservable, INBTSerializable<NBTTagCompound> {
	public void setCapacity(int capacity);
	
	/** This is used for synchronization between server/client and shouldn't be used as a general purpose method. */
	public void setEnergy(int capacity);
}
