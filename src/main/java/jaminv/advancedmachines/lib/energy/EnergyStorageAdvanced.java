package jaminv.advancedmachines.lib.energy;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;

public class EnergyStorageAdvanced implements IEnergyStorageAdvanced {

	private List<IObserver> observers = new ArrayList<>();
	public void addObserver(IObserver obv) { observers.add(obv); }
	
	protected void onEnergyChanged() {
		for (IObserver obv : observers) {
			obv.onEnergyChanged();
		}
	}
	
    protected int energy;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;

    public EnergyStorageAdvanced(int capacity, int maxReceive, int maxExtract, int energy) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.energy = Math.max(0 , Math.min(capacity, energy));
    }

    public EnergyStorageAdvanced(int capacity) { this(capacity, capacity, capacity, 0); }
    public EnergyStorageAdvanced(int capacity, int maxTransfer) { this(capacity, maxTransfer, maxTransfer, 0); }
    public EnergyStorageAdvanced(int capacity, int maxReceive, int maxExtract) { this(capacity, maxReceive, maxExtract, 0); }

	/* IEnergyStorage */
	
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
    	if (!canReceive()) { return 0; }

        int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
        if (!simulate) { 
        	energy += energyReceived;
        	if (energyReceived > 0) { onEnergyChanged(); }
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract()) { return 0; }

        int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
        if (!simulate) {
            energy -= energyExtracted;
            if (energyExtracted > 0) { onEnergyChanged(); }
        }
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() { return energy; }

    @Override
    public int getMaxEnergyStored() { return capacity; }

    @Override
    public boolean canExtract() { return this.maxExtract > 0; }

    @Override
    public boolean canReceive() { return this.maxReceive > 0; }	
	
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
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
		if (energy > capacity) { energy = capacity; }
	}
	
	public EnergyStorageAdvanced setMaxTransfer(int maxTransfer) {
		setMaxReceive(maxTransfer);
		setMaxExtract(maxTransfer);
		return this;
	}
	
	public EnergyStorageAdvanced setMaxReceive(int maxReceive) {
		this.maxReceive = maxReceive;
		return this;
	}
	
	public EnergyStorageAdvanced setMaxExtract(int maxExtract) {
		this.maxExtract = maxExtract;
		return this;
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
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("energy", this.energy);
		return nbt;
	}
	
    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
    	this.energy = nbt.getInteger("energy");
    }
}
