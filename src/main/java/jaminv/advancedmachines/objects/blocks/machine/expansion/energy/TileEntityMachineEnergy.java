package jaminv.advancedmachines.objects.blocks.machine.expansion.energy;

import jaminv.advancedmachines.objects.blocks.energy.EnergyStorageObservable;
import jaminv.advancedmachines.objects.blocks.machine.IMachineEnergy;
import jaminv.advancedmachines.objects.blocks.machine.MachineEnergyStorage;
import jaminv.advancedmachines.objects.blocks.machine.expansion.IMachineUpgradeTool;
import jaminv.advancedmachines.objects.blocks.machine.expansion.TileEntityMachineExpansionBase;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.TileEntityMachineMultiblock;
import jaminv.advancedmachines.objects.material.MaterialExpansion;
import jaminv.advancedmachines.util.dialog.container.EmptyContainer;
import jaminv.advancedmachines.util.dialog.container.IContainerUpdate;
import jaminv.advancedmachines.util.interfaces.IDirectional;
import jaminv.advancedmachines.util.interfaces.IHasGui;
import jaminv.advancedmachines.util.interfaces.IHasMetadata;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityMachineEnergy extends TileEntityMachineExpansionBase implements IHasGui, IContainerUpdate, IMachineEnergy, IMachineUpgradeTool, IHasMetadata, IDirectional, EnergyStorageObservable.IObserver {
	
	protected EnumFacing facing = EnumFacing.NORTH;
	protected BlockPos parent;
	
	public void setFacing(EnumFacing facing) {
		this.facing = facing;
	}
	public EnumFacing getFacing() {
		return facing;
	}
	
	@Override
	public void setMeta(int meta) {
		super.setMeta(meta);
		energy.setMaterial(getMaterial());
	}

	public MachineEnergyStorage energy;
	
	public float getEnergyPercent() { return (float)energy.getEnergyStored() / energy.getMaxEnergyStored(); }
	public int getEnergyStored() { return energy.getEnergyStored(); }
	public int getMaxEnergyStored() { return energy.getMaxEnergyStored(); }
	
	public TileEntityMachineEnergy() {
		super();
		this.energy = new MachineEnergyStorage();
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
	public void onEnergyAvailable() {
		if (parent == null) { return; }
		TileEntity te = world.getTileEntity(parent);
		if (te instanceof TileEntityMachineMultiblock) {
			((TileEntityMachineMultiblock)te).wake();
		}
	}
	
	@Override
	public boolean tickUpdate(TileEntityMachineMultiblock te) {
		return this.transferEnergy(te.getEnergy()) > 0;
	}
	
	public int transferEnergy(IEnergyStorage storage) {
		int transfer;
		
		transfer = energy.extractEnergy(Integer.MAX_VALUE, true);
		transfer = storage.receiveEnergy(transfer, true);
		
		if (transfer > 0) {
			energy.extractEnergy(transfer, false);
			storage.receiveEnergy(transfer, false);
		}
		return transfer;
	}
	
	@Override
	public void setParent(BlockPos pos) {
		parent = pos;
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
			energy.readFromNBT(compound);
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("facing", facing.getName());
		energy.writeToNBT(compound);
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
