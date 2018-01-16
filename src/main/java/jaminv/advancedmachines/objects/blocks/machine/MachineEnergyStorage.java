package jaminv.advancedmachines.objects.blocks.machine;

import mezz.jei.api.JEIPlugin;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MachineEnergyStorage extends EnergyStorage {

	public MachineEnergyStorage(int capacity, int maxTransfer) {
		super(capacity, maxTransfer); 
	}
	
	public void setEnergy(int energy) {
		this.energy = energy;
	}
	
	public void useEnergy(int energy) {
		if (energy < 0) { return; }
		this.energy -= energy;
		if (this.energy < 0) { this.energy = 0; }		
	}
  
	public void readFromNBT(NBTTagCompound compound) {
		this.energy = compound.getInteger("energy");
	}
    
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("energy", this.energy);		
		return compound;
	}    
}
