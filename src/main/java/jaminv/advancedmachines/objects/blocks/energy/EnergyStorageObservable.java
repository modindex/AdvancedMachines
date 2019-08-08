package jaminv.advancedmachines.objects.blocks.energy;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;

public class EnergyStorageObservable extends EnergyStorage {

	public static interface IObserver {
		/**
		 * Observer doesn't track every time energy changes, that would happen to often.
		 * Instead, it only tracks when energy is available, but previously wasn't.
		 */
		public void onEnergyChanged();
	}
	
	public EnergyStorageObservable(int capacity, int maxTransfer) {
		super(capacity, maxTransfer);
	}
	
	private List<IObserver> observers = new ArrayList<>();
	
	public void addObserver(IObserver obv) {
		observers.add(obv);
	}
	
	public void removeObserver(IObserver obv) {
		observers.remove(obv);
	}
	
	protected void onEnergyChanged() {
		for (IObserver obv : observers) {
			obv.onEnergyChanged();
		}
	}
	
	/* Storage */
	
	/**
	 * This is used for synchronization between server/client and shouldn't be used as a general purpose method.
	 * 
	 * It intentionally returns void to prevent the return from being useful for other purposes.
	 * @param value
	 * @return void 
	 */
	public void setEnergy(int value) {
		int old = this.getEnergyStored();
		
		this.energy = value;
		
		if (old != value) { this.onEnergyChanged(); }
	}
	
	public EnergyStorageObservable setCapacity(int capacity) {
		this.capacity = capacity;
		
		if (energy > capacity) { energy = capacity; }
		
		return this;
	}
	
	public EnergyStorageObservable setMaxTransfer(int maxTransfer) {
		setMaxReceive(maxTransfer);
		setMaxExtract(maxTransfer);
		return this;
	}
	
	public EnergyStorageObservable setMaxReceive(int maxReceive) {
		this.maxReceive = maxReceive;
		return this;
	}
	
	public EnergyStorageObservable setMaxExtract(int maxExtract) {
		this.maxExtract = maxExtract;
		return this;
	}
	
	/* IEnergyStorage */
	
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		int ret = super.receiveEnergy(maxReceive, simulate);
		if (ret > 0 && !simulate) { onEnergyChanged(); }
		return ret;
	}
	
	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		int ret = super.extractEnergy(maxExtract, simulate);
		if (ret > 0 && !simulate) { onEnergyChanged(); }
		return ret;
	}
	
	/* Internal */
	
    public int receiveEnergyInternal(int maxReceive, boolean simulate) {
        int energyReceived = Math.min(capacity - energy, maxReceive);
        if (!simulate) { energy += energyReceived; }
        
        if (energyReceived > 0 && !simulate) { onEnergyChanged(); }
        
        return energyReceived;
    }

    public int extractEnergyInternal(int maxExtract, boolean simulate) {
        int energyExtracted = Math.min(energy, maxExtract);
        if (!simulate) { energy -= energyExtracted; }
        
        if (energyExtracted > 0 && !simulate) { onEnergyChanged(); }
        
        return energyExtracted;
    }	

	/* NBT */
	  
	public void readFromNBT(NBTTagCompound compound) {
		this.energy = compound.getInteger("energy");
	}
    
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("energy", this.energy);		
		return compound;
	}	
}
