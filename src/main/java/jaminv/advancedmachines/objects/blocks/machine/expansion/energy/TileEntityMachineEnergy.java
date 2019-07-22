package jaminv.advancedmachines.objects.blocks.machine.expansion.energy;

import javax.annotation.Nullable;

import jaminv.advancedmachines.objects.blocks.inventory.ContainerInventory;
import jaminv.advancedmachines.objects.blocks.inventory.TileEntityInventory;
import jaminv.advancedmachines.objects.blocks.machine.ContainerMachine;
import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.IMachineEnergy;
import jaminv.advancedmachines.objects.blocks.machine.MachineEnergyStorage;
import jaminv.advancedmachines.objects.blocks.machine.expansion.IMachineUpgradeTool;
import jaminv.advancedmachines.objects.blocks.machine.expansion.TileEntityMachineExpansionBase;
import jaminv.advancedmachines.objects.blocks.machine.instance.alloy.TileEntityMachineAlloy.GuiMachineAlloy;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.TileEntityMachineMultiblock;
import jaminv.advancedmachines.util.dialog.container.EmptyContainer;
import jaminv.advancedmachines.util.dialog.container.IContainerUpdate;
import jaminv.advancedmachines.util.dialog.container.IContainerUpdate;
import jaminv.advancedmachines.util.dialog.gui.GuiContainerObservable;
import jaminv.advancedmachines.util.dialog.gui.GuiScreenObservable;
import jaminv.advancedmachines.util.interfaces.IHasGui;
import jaminv.advancedmachines.util.interfaces.IHasGui;
import jaminv.advancedmachines.util.recipe.IRecipeManager;
import jaminv.advancedmachines.util.recipe.machine.AlloyManager;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

public class TileEntityMachineEnergy extends TileEntityMachineExpansionBase implements IHasGui, IContainerUpdate, IMachineEnergy, IMachineUpgradeTool {
	
	protected EnumFacing facing = EnumFacing.NORTH;
	protected BlockPos parent;
	
	public void setFacing(EnumFacing facing) {
		this.facing = facing;
	}
	public EnumFacing getFacing() {
		return facing;
	}

	public MachineEnergyStorage energy;
	
	public float getEnergyPercent() { return (float)energy.getEnergyStored() / energy.getMaxEnergyStored(); }
	public int getEnergyStored() { return energy.getEnergyStored(); }
	public int getMaxEnergyStored() { return energy.getMaxEnergyStored(); }
	
	public TileEntityMachineEnergy() {
		super();
		this.energy = new MachineEnergyStorage(50000, 200);
	}
	
	private final DialogMachineEnergy dialog = new DialogMachineEnergy(this);
	
	public class GuiMachineEnergy extends GuiContainerObservable {
		public GuiMachineEnergy(EmptyContainer container, DialogMachineEnergy dialog) {
			super(container, dialog.getW(), dialog.getH());
			this.addObserver(dialog);
		}
	}
	
	@Override
	public EmptyContainer createContainer(IInventory inventory) {
		return new EmptyContainer(this);
	}

	@Override
	public GuiScreen createGui(IInventory inventory) {
		return new GuiMachineEnergy(createContainer(inventory), dialog);
	}
	
	@Override
	public void tickUpdate(TileEntityMachineMultiblock te) {
		this.transferEnergy(te.getEnergy());
	}
	
	public void transferEnergy(MachineEnergyStorage storage) {
		int transfer = Math.min(storage.getMaxEnergyStored() - storage.getEnergyStored(), energy.getEnergyStored());	
		energy.setEnergy(energy.getEnergyStored() - transfer);
		storage.setEnergy(storage.getEnergyStored() + transfer);
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
			energy.setEnergy(value); return;
		}
	}
	
	public boolean canInteractWith(EntityPlayer playerIn) {
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}
}
