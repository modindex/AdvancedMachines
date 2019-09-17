package jaminv.advancedmachines.machine.instance.injector;

import jaminv.advancedmachines.AdvancedMachines;
import jaminv.advancedmachines.lib.container.ContainerMachine;
import jaminv.advancedmachines.lib.container.SyncManager;
import jaminv.advancedmachines.lib.container.layout.ILayoutManager;
import jaminv.advancedmachines.lib.container.layout.ItemLayoutGrid.HotbarLayout;
import jaminv.advancedmachines.lib.container.layout.ItemLayoutGrid.InventoryLayout;
import jaminv.advancedmachines.lib.container.layout.JeiLayoutManager;
import jaminv.advancedmachines.lib.container.layout.impl.BucketLayout;
import jaminv.advancedmachines.lib.container.layout.impl.OutputLayout;
import jaminv.advancedmachines.lib.dialog.fluid.DialogBucketToggle;
import jaminv.advancedmachines.lib.fluid.BucketHandler;
import jaminv.advancedmachines.lib.inventory.ItemHandlerSeparated;
import jaminv.advancedmachines.lib.inventory.slot.SlotHandlerFluid;
import jaminv.advancedmachines.machine.TileMachine;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.util.network.BucketStateMessage;
import net.minecraft.inventory.IInventory;

public class TileMachineInjector extends TileMachine implements DialogBucketToggle.Provider {

	public static class ContainerInjector extends ContainerMachine {
		public ContainerInjector(ILayoutManager layout, ItemHandlerSeparated inventory, IInventory playerInventory,
				SyncManager sync) {
			super(layout, inventory, playerInventory, sync);
		}
	}
	
	protected BucketHandler bucketHandler = new BucketHandler().setCallback(state -> {
		controller.wake();		
		if (world.isRemote) {
			AdvancedMachines.NETWORK.sendToServer(new BucketStateMessage(this.getPos(), state));
		}
	});
	
	public BucketHandler getBucketToggle() { return bucketHandler; }

	public static final JeiLayoutManager layout = new JeiLayoutManager()
		.setItemInputLayout(InjectorManager.getRecipeManager(), 107, 21)
		.addFluidInputLayout(53, 21, 16, 48)
		.setItemOutputLayout(new OutputLayout(107, 53))
		.setItemAdditionalLayout(new BucketLayout(152, 59))
		.setInventoryLayout(new InventoryLayout(8, 84))
		.setHotbarLayout(new HotbarLayout(8, 142));

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
		return new ContainerInjector(layout, storage, playerInventory, this.getSyncManager());
	}

	@Override
	protected boolean preProcess() {
		return super.preProcess() || 
			bucketHandler.handleBucket(inventory, inventory.getFirstAdditionalSlot(), inputTanks);
	}
}
