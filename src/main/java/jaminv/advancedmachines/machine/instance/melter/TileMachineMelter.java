package jaminv.advancedmachines.machine.instance.melter;

import jaminv.advancedmachines.lib.container.ContainerMachine;
import jaminv.advancedmachines.lib.container.SyncManager;
import jaminv.advancedmachines.lib.container.layout.ILayoutManager;
import jaminv.advancedmachines.lib.container.layout.ItemLayoutGrid.HotbarLayout;
import jaminv.advancedmachines.lib.container.layout.ItemLayoutGrid.InventoryLayout;
import jaminv.advancedmachines.lib.container.layout.JeiLayoutManager;
import jaminv.advancedmachines.lib.container.layout.impl.BucketLayout;
import jaminv.advancedmachines.lib.inventory.ItemHandlerSeparated;
import jaminv.advancedmachines.machine.TileMachine;
import jaminv.advancedmachines.machine.instance.grinder.GrinderManager;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import net.minecraft.inventory.IInventory;

public class TileMachineMelter extends TileMachine {
	
	/** Discrete Container class required for JEI */
	public static class ContainerMelter extends ContainerMachine {
		public ContainerMelter(ILayoutManager layout, ItemHandlerSeparated inventory, IInventory playerInventory,
				SyncManager sync) {
			super(layout, inventory, playerInventory, sync);
		}
	}
	
	public static final JeiLayoutManager layout = new JeiLayoutManager()
		.setItemInputLayout(GrinderManager.getRecipeManager(), 53, 37)
		.addFluidOutputLayout(107, 21, 16, 48)
		.setItemAdditionalLayout(new BucketLayout(152, 59))
		.setInventoryLayout(new InventoryLayout(8, 84))
		.setHotbarLayout(new HotbarLayout(8, 142));
	
	public TileMachineMelter() {
		super(MelterManager.getRecipeManager());
		inventory.addInputSlots(1);
		inventory.addAdditionalSlots(1);
		outputTanks.addTanks(1);
	}

	@Override
	public MachineType getMachineType() {
		return MachineType.MELTER;
	}

	@Override
	public ContainerMachine createContainer(IInventory playerInventory) {
		return new ContainerMelter(layout, storage, playerInventory, this.getSyncManager());
	}

	/*
	 * TODO: Melter output to bucket 
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
