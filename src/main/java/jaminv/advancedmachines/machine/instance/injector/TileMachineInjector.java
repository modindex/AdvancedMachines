package jaminv.advancedmachines.machine.instance.injector;

import jaminv.advancedmachines.AdvancedMachines;
import jaminv.advancedmachines.lib.container.ContainerMachine;
import jaminv.advancedmachines.lib.container.ISyncManager;
import jaminv.advancedmachines.lib.container.layout.ILayoutManager;
import jaminv.advancedmachines.lib.container.layout.ItemLayoutGrid.HotbarLayout;
import jaminv.advancedmachines.lib.container.layout.ItemLayoutGrid.InventoryLayout;
import jaminv.advancedmachines.lib.container.layout.JeiLayoutManager;
import jaminv.advancedmachines.lib.container.layout.impl.BucketLayout;
import jaminv.advancedmachines.lib.container.layout.impl.OutputLayout;
import jaminv.advancedmachines.lib.dialog.control.enums.IOState;
import jaminv.advancedmachines.lib.inventory.IItemHandlerMachine;
import jaminv.advancedmachines.lib.inventory.slot.SlotHandlerFluid;
import jaminv.advancedmachines.machine.MachineHelper;
import jaminv.advancedmachines.machine.TileMachineMultiblock;
import jaminv.advancedmachines.machine.dialog.DialogBucketToggle.IBucketToggle;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.util.network.BucketStateMessage;
import net.minecraft.inventory.IInventory;

public class TileMachineInjector extends TileMachineMultiblock implements IBucketToggle {
	
	public static class ContainerInjector extends ContainerMachine {
		public ContainerInjector(ILayoutManager layout, IItemHandlerMachine inventory, IInventory playerInventory,
				ISyncManager sync) {
			super(layout, inventory, playerInventory, sync);
		}
	}

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
	
	// IBucketToggle
	
	protected IOState bucketState = IOState.INPUT;
	@Override public IOState getBucketState() { return bucketState; }

	@Override
	public void setBucketState(IOState state) {
		bucketState = state;
		controller.wake();
		
		if (world.isRemote) {
			AdvancedMachines.NETWORK.sendToServer(new BucketStateMessage(this.getPos(), state));
		}
	}

	@Override
	protected boolean preProcess() {
		return super.preProcess() || 
			MachineHelper.handleBucket(inventory, inventory.getFirstAdditionalSlot(), inputTanks, bucketState);
	}
}
