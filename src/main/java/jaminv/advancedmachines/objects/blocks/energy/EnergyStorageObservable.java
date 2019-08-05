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
		public void onEnergyAvailable();
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
	
	protected void onEnergyAvailable() {
		for (IObserver obv : observers) {
			obv.onEnergyAvailable();
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
		
		if (old == 0 && value != 0) { this.onEnergyAvailable(); }
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
		int energy = this.getEnergyStored();

		int ret = super.receiveEnergy(maxReceive, simulate);

		if (energy == 0 && ret > 0 && !simulate) {
			onEnergyAvailable();
		}
		
		return ret;
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
