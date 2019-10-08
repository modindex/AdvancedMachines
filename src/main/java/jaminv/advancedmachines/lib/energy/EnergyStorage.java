package jaminv.advancedmachines.lib.energy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * Energy Storage
 * 
 * Energy Storage is an extension of IEnergyStorage that provides additional methods
 * for ignoring transfer limits.
 * @author jamin
 *
 */
public interface EnergyStorage extends IEnergyStorage, EnergyObservable, INBTSerializable<NBTTagCompound> {
    public int receiveEnergyInternal(int maxReceive, boolean simulate);
    public int extractEnergyInternal(int maxExtract, boolean simulate);
}
