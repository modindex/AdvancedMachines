package jaminv.advancedmachines.objects.blocks.machine.expansion.inventory;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.lib.container.ContainerInventory;
import jaminv.advancedmachines.lib.inventory.IItemObservable;
import jaminv.advancedmachines.lib.inventory.InventoryHelper;
import jaminv.advancedmachines.lib.inventory.ItemStackHandlerObservable;
import jaminv.advancedmachines.lib.machine.IMachineController;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogIOToggleButton;
import jaminv.advancedmachines.objects.blocks.machine.expansion.TileEntityMachineExpansionBase;
import jaminv.advancedmachines.util.interfaces.IDirectional;
import jaminv.advancedmachines.util.interfaces.IHasGui;
import jaminv.advancedmachines.util.message.IOStateMessage;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class TileEntityMachineInventory extends TileEntityMachineExpansionBase implements IHasGui, IMachineController.ISubController, IDirectional, DialogIOToggleButton.ISwitchableIO, IItemObservable.IObserver {
	
	public final int SIZE = 27;
	protected final ItemStackHandlerObservable inventory = new ItemStackHandlerObservable(SIZE); 	
	protected EnumFacing facing = EnumFacing.NORTH;
	protected boolean inputState = true;
	protected int priority = 0;
	protected IMachineController controller;
	
	public TileEntityMachineInventory() {
		super();
		inventory.addObserver(this);
	}

	@Override
	public Container createContainer(IInventory playerInventory) {
		return new ContainerInventory(DialogMachineInventory.layout, inventory, playerInventory);
	}
	
	/* IHasGui */

	@Override
	public GuiContainer createGui(IInventory playerInventory) {
		return new DialogMachineInventory(createContainer(playerInventory), this);
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
			Main.NETWORK.sendToServer(new IOStateMessage(this.getPos(), state, priority));
		}
		
		if (controller != null) { controller.wake(); }
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
				
		if (world.isRemote) {
			Main.NETWORK.sendToServer(new IOStateMessage(this.getPos(), inputState, priority));
		}
		
		if (controller != null) { controller.sortSubControllers(); }
	}
	
	boolean allowInput = false;
	boolean allowOutput = false;
	
	@Override public boolean canInput() { return allowInput; }
	@Override public boolean canOutput() { return allowOutput; }
	@Override public boolean hasController() { return controller == null; }

	@Override
	public void setController(IMachineController controller) {
		this.controller = controller;
		if (controller != null) {
			// TODO: Machine Input/Output determination
			//TileEntityMachineMultiblock te = MultiblockHelper.getParent(world, pos);
			//ItemStackHandlerObservable inv = te.getInventory();
			allowInput = true; //inv.canInsert();
			allowOutput = true; //inv.canExtract();
			
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
	public boolean preProcess(IMachineController controller) {
		if (inputState) {
			return InventoryHelper.moveAllToInput(inventory, controller.getInventory());
		} else {
			return InventoryHelper.moveOutputToAll(controller.getInventory(), inventory);
		}
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
}
