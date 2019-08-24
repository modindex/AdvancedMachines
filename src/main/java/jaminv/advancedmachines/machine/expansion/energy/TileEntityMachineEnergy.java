package jaminv.advancedmachines.machine.expansion.energy;

import jaminv.advancedmachines.lib.dialog.container.EmptyContainer;
import jaminv.advancedmachines.lib.dialog.container.IContainerUpdate;
import jaminv.advancedmachines.lib.energy.EnergyStorageAdvanced;
import jaminv.advancedmachines.lib.energy.IEnergyObservable;
import jaminv.advancedmachines.lib.energy.IEnergyStorageInternal;
import jaminv.advancedmachines.lib.machine.IMachineController;
import jaminv.advancedmachines.machine.expansion.TileEntityMachineExpansionType;
import jaminv.advancedmachines.objects.material.MaterialExpansion;
import jaminv.advancedmachines.util.ModConfig;
import jaminv.advancedmachines.util.interfaces.IDirectional;
import jaminv.advancedmachines.util.interfaces.IHasGui;
import jaminv.advancedmachines.util.interfaces.IHasMetadata;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityMachineEnergy extends TileEntityMachineExpansionType implements IHasGui, IContainerUpdate, IMachineController.ISubController, IHasMetadata, IDirectional, IEnergyObservable.IObserver {
	
	protected EnumFacing facing = EnumFacing.NORTH;
	protected IMachineController controller;
	
	public void setFacing(EnumFacing facing) {
		this.facing = facing;
	}
	public EnumFacing getFacing() {
		return facing;
	} 
	
	@Override
	public void setMeta(int meta) {
		super.setMeta(meta);
		energy.setEnergyCapacity(ModConfig.general.defaultMachineEnergyCapacity * this.getMultiplier());
	}

	public EnergyStorageAdvanced energy;
	
	public IEnergyStorage getEnergy() { return energy; }
	
	public TileEntityMachineEnergy() {
		super();
		energy = new EnergyStorageAdvanced(ModConfig.general.defaultMachineEnergyCapacity * MaterialExpansion.maxMultiplier);
		energy.addObserver(this);
	}
	
	@Override
	public EmptyContainer createContainer(IInventory inventory) {
		return new EmptyContainer(this);
	}

	@Override
	public GuiScreen createGui(IInventory inventory) {
		return new DialogMachineEnergy(createContainer(inventory), this);
	}
	
	@Override
	public void onEnergyChanged() {
		if (controller != null) { controller.wake(); }
	}

	@Override
	public void setController(IMachineController controller) {
		this.controller = controller;
	}
	
	@Override
	public boolean preProcess(IMachineController controller) {
		return transferEnergy(controller.getEnergy()) > 0;
	}
	
	public int transferEnergy(IEnergyStorageInternal storage) {
		int transfer;
		
		transfer = energy.extractEnergyInternal(Integer.MAX_VALUE, true);
		transfer = storage.receiveEnergyInternal(transfer, true);
		
		if (transfer > 0) {
			energy.extractEnergyInternal(transfer, false);
			storage.receiveEnergyInternal(transfer, false);
		}
		return transfer;
	}
	
	@Override
	public int getPriority() {
		return 0;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(this.energy);
		}
		return super.getCapability(capability, facing);
	}	
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("facing")) {
			facing = EnumFacing.byName(compound.getString("facing"));
		}
		if (compound.hasKey("energy")) {
			energy.deserializeNBT(compound.getCompoundTag("energy"));
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("facing", facing.getName());
		compound.setTag("energy", energy.serializeNBT());
		return compound;
	}
	
	public int getFieldCount() { return 1; }
	public int getField(int id) {
		switch(id) {
		case 0:
			return energy.getEnergyStored();
		}
		return 0;
	}
	
	public void setField(int id, int value) {
		switch(id) {
		case 0:
			energy.setEnergy(value);
		}
	}
	
	public boolean canInteractWith(EntityPlayer playerIn) {
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}

}
