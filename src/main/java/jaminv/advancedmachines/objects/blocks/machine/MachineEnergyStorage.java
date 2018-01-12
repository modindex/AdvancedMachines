package jaminv.advancedmachines.objects.blocks.machine;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.EnergyStorage;

public class MachineEnergyStorage extends EnergyStorage {

	public MachineEnergyStorage(int capacity, int maxTransfer) {
		super(capacity, maxTransfer);
	}
	
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		return super.receiveEnergy(maxReceive, simulate);
	}
	
	@Override
	public int getEnergyStored() {
		return super.getEnergyStored();
	}
	
  
	public void readFromNBT(NBTTagCompound compound) {
		this.energy = compound.getInteger("energy");
	}
    
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("energy", this.energy);		
		return compound;
	}    
}
