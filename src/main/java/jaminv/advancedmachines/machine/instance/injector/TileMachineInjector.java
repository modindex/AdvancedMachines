package jaminv.advancedmachines.machine.instance.injector;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.lib.container.ContainerMachine;
import jaminv.advancedmachines.lib.dialog.control.enums.IOState;
import jaminv.advancedmachines.lib.inventory.slot.SlotHandlerFluid;
import jaminv.advancedmachines.machine.MachineHelper;
import jaminv.advancedmachines.machine.TileMachineMultiblock;
import jaminv.advancedmachines.machine.dialog.DialogBucketToggle.IBucketToggle;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.util.network.BucketStateMessage;
import jaminv.advancedmachines.util.recipe.injector.InjectorManager;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class TileMachineInjector extends TileMachineMultiblock implements IBucketToggle {
	public TileMachineInjector() {
		super(InjectorManager.getRecipeManager());
		inventory.addInputSlots(1);
		inventory.addOutputSlots(1);
		inventory.addAdditionalSlots(1, new SlotHandlerFluid());
		inputTanks.addTanks(1);
	}

	@Override
	public MachineType getMachineType() {
		return MachineType.INJECTOR;
	}

	@Override
	public ContainerMachine createContainer(IInventory playerInventory) {
		return new ContainerMachine(DialogMachineInjector.layout, storage, playerInventory, this.getSyncManager());
	}
	
	@Override
	public GuiContainer createGui(IInventory inventory) {
		return new DialogMachineInjector(createContainer(inventory), this);
	}
	
	// IBucketToggle
	
	protected IOState bucketState = IOState.INPUT;
	@Override public IOState getBucketState() { return bucketState; }

	@Override
	public void setBucketState(IOState state) {
		bucketState = state;
		controller.wake();
		
		if (world.isRemote) {
			Main.NETWORK.sendToServer(new BucketStateMessage(this.getPos(), state));
		}
	}


	@Override
	protected boolean preProcess() {
		return super.preProcess() || 
			MachineHelper.handleBucket(inventory, inventory.getFirstAdditionalSlot(), inputTanks, bucketState);
	}
}
