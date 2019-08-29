package jaminv.advancedmachines.machine.instance.stabilizer;

import jaminv.advancedmachines.lib.container.ContainerMachine;
import jaminv.advancedmachines.lib.container.layout.ItemLayoutGrid.HotbarLayout;
import jaminv.advancedmachines.lib.container.layout.ItemLayoutGrid.InventoryLayout;
import jaminv.advancedmachines.lib.container.layout.JeiLayoutManager;
import jaminv.advancedmachines.lib.container.layout.impl.BucketLayout;
import jaminv.advancedmachines.machine.TileMachineMultiblock;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.util.recipe.stabilizer.StabilizerManager;
import net.minecraft.inventory.IInventory;

public class TileMachineStabilizer extends TileMachineMultiblock {
	
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

	@Override
	public MachineType getMachineType() {
		return MachineType.STABILIZER;
	}

	@Override
	public ContainerMachine createContainer(IInventory playerInventory) {
		return new ContainerMachine(layout, storage, playerInventory, this.getSyncManager());
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
