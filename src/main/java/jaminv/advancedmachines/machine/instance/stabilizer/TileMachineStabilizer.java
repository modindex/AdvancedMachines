package jaminv.advancedmachines.machine.instance.stabilizer;

import jaminv.advancedmachines.lib.container.ContainerMachine;
import jaminv.advancedmachines.lib.container.SyncManager;
import jaminv.advancedmachines.lib.container.layout.ILayoutManager;
import jaminv.advancedmachines.lib.container.layout.ItemLayoutGrid.HotbarLayout;
import jaminv.advancedmachines.lib.container.layout.ItemLayoutGrid.InventoryLayout;
import jaminv.advancedmachines.lib.container.layout.JeiLayoutManager;
import jaminv.advancedmachines.lib.container.layout.impl.BucketLayout;
import jaminv.advancedmachines.lib.fluid.FluidTank;
import jaminv.advancedmachines.lib.inventory.ItemHandlerSeparated;
import jaminv.advancedmachines.machine.TileMachine;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import net.minecraft.inventory.IInventory;

public class TileMachineStabilizer extends TileMachine {
	
	public static class ContainerStabilizer extends ContainerMachine {
		public ContainerStabilizer(ILayoutManager layout, ItemHandlerSeparated inventory, IInventory playerInventory,
				SyncManager sync) {
			super(layout, inventory, playerInventory, sync);
		}
	}
	
	public static final JeiLayoutManager layout = new JeiLayoutManager()
		.addFluidInputLayout(53, 21, 16, 48)
		.setItemOutputLayout(107, 37)
		.setItemAdditionalLayout(new BucketLayout(152, 59))
		.setInventoryLayout(new InventoryLayout(8, 84))
		.setHotbarLayout(new HotbarLayout(8, 142));

	public TileMachineStabilizer() {
		super(StabilizerManager.getRecipeManager());
		inventory.addOutputSlots(1);
		inventory.addAdditionalSlots(1);
		inputTanks.addTanks(1);
	}
	
	FluidTank getInputTank() { return inputTanks.getTank(0); }

	@Override
	public MachineType getMachineType() {
		return MachineType.STABILIZER;
	}

	@Override
	public ContainerMachine createContainer(IInventory playerInventory) {
		return new ContainerStabilizer(layout, storage, playerInventory, this.getSyncManager());
	}

	@Override
	protected boolean preProcess() {
		boolean didSomething = super.preProcess();
		return this.bucketHandler.handleBucket(this.inventory, 1, this.inputTanks) || didSomething;
	}
	
	/*
	 * TODO: Stabilizer output into and out of bucket 
	@Override
	protected boolean preProcess() {
		boolean didSomething = super.preProcess();
		
		int i = this.getFirstOutputSlot();
		ItemStack stack = this.getInventory().getStackInSlot(i);
		if (stack != null) {
			FluidActionResult result = null;
			result = FluidUtil.tryFillContainer(stack, this.getTank(), 1000, null, true);
			
			if (result != null && result.success) {
				this.getInventory().extractItem(i, stack.getCount(), false);
				this.getInventory().insertItem(i, result.getResult(), false);
				didSomething = true;
			}
		}
		
		return didSomething;
	}
	*/
}
