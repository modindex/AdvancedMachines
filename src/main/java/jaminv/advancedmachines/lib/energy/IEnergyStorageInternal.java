package jaminv.advancedmachines.lib.energy;

import net.minecraftforge.energy.IEnergyStorage;

public interface IEnergyStorageInternal extends IEnergyStorage {
    public int receiveEnergyInternal(int maxReceive, boolean simulate);
    public int extractEnergyInternal(int maxExtract, boolean simulate);
}
