package jaminv.advancedmachines.machine.expansion.inventory;

import jaminv.advancedmachines.AdvancedMachines;
import jaminv.advancedmachines.init.HasGui;
import jaminv.advancedmachines.lib.container.ContainerInventory;
import jaminv.advancedmachines.lib.container.layout.ILayoutManager;
import jaminv.advancedmachines.lib.container.layout.ItemLayoutGrid;
import jaminv.advancedmachines.lib.container.layout.LayoutManager;
import jaminv.advancedmachines.lib.inventory.InventoryHelper;
import jaminv.advancedmachines.lib.inventory.ItemObservable;
import jaminv.advancedmachines.lib.inventory.ItemStackHandlerObservable;
import jaminv.advancedmachines.lib.machine.MachineControllerInterface;
import jaminv.advancedmachines.lib.util.blocks.HasItemNBT;
import jaminv.advancedmachines.lib.util.helper.HasFacing;
import jaminv.advancedmachines.machine.dialog.DialogIOToggle;
import jaminv.advancedmachines.machine.expansion.TileMachineExpansion;
import jaminv.advancedmachines.util.network.IOStateMessage;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileMachineInventory extends TileMachineExpansion implements HasGui, MachineControllerInterface.SubController, HasFacing, HasItemNBT, DialogIOToggle.ISwitchableIO, ItemObservable.IObserver {

	public static final ILayoutManager layout = new LayoutManager()
		.addLayout(new ItemLayoutGrid.InventoryLayout(8, 38))
		.setInventoryLayout(8, 103)
		.setHotbarLayout(8, 161);	
	
	public final int SIZE = 27;
	protected final ItemStackHandlerObservable inventory = new ItemStackHandlerObservable(SIZE); 	
	protected EnumFacing facing = EnumFacing.NORTH;
	protected boolean inputState = true;
	protected int priority = 0;
	protected MachineControllerInterface controller;
	
	public TileMachineInventory() {
		super();
		inventory.addObserver(this);
	}

	/* IHasGui */
	
	@Override
	public Container createContainer(IInventory playerInventory) {
		return new ContainerInventory(layout, inventory, playerInventory);
	}
	
	/* IDirectional */
	
	public void setFacing(EnumFacing facing) { this.facing = facing; }
	public EnumFacing getFacing() {	return facing; }
	public boolean getInputState() { return inputState;	}
	
	/* ISwitchableIO */
	
	public void setInputState(boolean state) {
		this.inputState = state;
		world.markBlockRangeForRenderUpdate(this.pos, this.pos);
		
		if (world.isRemote) {
			AdvancedMachines.NETWORK.sendToServer(new IOStateMessage(this.getPos(), state, priority));
		}
		
		if (controller != null) { controller.wake(); }
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
				
		if (world.isRemote) {
			AdvancedMachines.NETWORK.sendToServer(new IOStateMessage(this.getPos(), inputState, priority));
		}
		
		if (controller != null) { controller.sortSubControllers(); }
	}
	
	boolean allowInput = false;
	boolean allowOutput = false;
	
	@Override public boolean canInput() { return allowInput; }
	@Override public boolean canOutput() { return allowOutput; }
	@Override public boolean hasController() { return controller == null; }

	@Override
	public void setController(MachineControllerInterface controller) {
		this.controller = controller;
		if (controller != null) {
			allowInput = controller.getInventory().getInputSlotCount() > 0;
			allowOutput = controller.getInventory().getOutputSlotCount() > 0;
			
			if (allowInput && !allowOutput) { this.setInputState(true); }
			if (allowOutput && !allowInput) { this.setInputState(false); }
		} else {
			allowInput = false;
			allowOutput = false;
		}
	}	
	
	@Override
	public int getPriority() {
		return priority;
	}
	
	@Override
	public void onInventoryContentsChanged(int slot) {
		if (controller != null) { controller.wake(); }
	}

	@Override
	public boolean preProcess(MachineControllerInterface controller) {
		if (inputState) {
			return InventoryHelper.moveAllToInput(inventory, controller.getInventory());
		} else {
			return InventoryHelper.moveOutputToAll(controller.getInventory(), inventory);
		}
	}
	
	/* Capabilities */

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
		}
		return super.getCapability(capability, facing);
	}	
	
	/* NBT */
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("facing")) {
			facing = EnumFacing.byName(compound.getString("facing"));
		}
		inputState = compound.getBoolean("inputState");
		priority = compound.getInteger("priority");
		allowInput = compound.getBoolean("allowInput");
		allowOutput = compound.getBoolean("allowOutput");
		inventory.deserializeNBT(compound.getCompoundTag("inventory"));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("facing", facing.getName());
		compound.setBoolean("inputState", inputState);
		compound.setInteger("priority", priority);
		compound.setBoolean("allowInput", allowInput);
		compound.setBoolean("allowOutput", allowOutput);
		compound.setTag("inventory", inventory.serializeNBT());
		return compound;
	}
	
	@Override
	public void readItemNBT(NBTTagCompound compound) {
		inventory.deserializeNBT(compound.getCompoundTag("inventory"));
	}
	
	@Override
	public NBTTagCompound writeItemNBT(NBTTagCompound compound) {
		compound.setTag("inventory", inventory.serializeNBT());
		return compound;
	}
}
