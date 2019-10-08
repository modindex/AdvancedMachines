package jaminv.advancedmachines.machine.instance.melter;

import jaminv.advancedmachines.AdvancedMachines;
import jaminv.advancedmachines.lib.container.ContainerMachine;
import jaminv.advancedmachines.lib.container.SyncManager;
import jaminv.advancedmachines.lib.container.layout.ILayoutManager;
import jaminv.advancedmachines.lib.container.layout.ItemLayoutGrid.HotbarLayout;
import jaminv.advancedmachines.lib.container.layout.ItemLayoutGrid.InventoryLayout;
import jaminv.advancedmachines.lib.container.layout.JeiLayoutManager;
import jaminv.advancedmachines.lib.container.layout.impl.BucketLayout;
import jaminv.advancedmachines.lib.dialog.fluid.DialogBucketToggle;
import jaminv.advancedmachines.lib.fluid.BucketHandler;
import jaminv.advancedmachines.lib.fluid.FluidTank;
import jaminv.advancedmachines.lib.inventory.ItemHandlerSeparated;
import jaminv.advancedmachines.lib.inventory.slot.SlotHandlerFluid;
import jaminv.advancedmachines.machine.TileMachine;
import jaminv.advancedmachines.machine.instance.grinder.GrinderManager;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.util.network.BucketStateMessage;
import net.minecraft.inventory.IInventory;

public class TileMachineMelter extends TileMachine implements DialogBucketToggle.Provider {
	
	/** Discrete Container class required for JEI */
	public static class ContainerMelter extends ContainerMachine {
		public ContainerMelter(ILayoutManager layout, ItemHandlerSeparated inventory, IInventory playerInventory,
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
		.setItemInputLayout(GrinderManager.getRecipeManager(), 53, 37)
		.addFluidOutputLayout(107, 21, 16, 48)
		.setItemAdditionalLayout(new BucketLayout(152, 59))
		.setInventoryLayout(new InventoryLayout(8, 84))
		.setHotbarLayout(new HotbarLayout(8, 142));
		
	public TileMachineMelter() {
		super(MelterManager.getRecipeManager());
		inventory.addInputSlots(1);
		inventory.addAdditionalSlots(1, new SlotHandlerFluid());	
		outputTanks.addTanks(1);
	}
	
	FluidTank getOutputTank() { return outputTanks.getTank(0); }
	
	@Override
	public MachineType getMachineType() {
		return MachineType.MELTER;
	}

	@Override
	public ContainerMachine createContainer(IInventory playerInventory) {
		return new ContainerMelter(layout, storage, playerInventory, this.getSyncManager());
	}

	// TODO: Melter output only 
	@Override
	protected boolean preProcess() {
		boolean didSomething = super.preProcess(); 
		return bucketHandler.handleBucket(inventory, inventory.getFirstAdditionalSlot(), outputTanks) || didSomething;
	}	
}
