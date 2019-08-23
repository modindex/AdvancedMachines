package jaminv.advancedmachines.machine.instance.stabilizer;

import jaminv.advancedmachines.lib.container.ContainerMachine;
import jaminv.advancedmachines.machine.TileEntityMachineMultiblock;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.util.recipe.stabilizer.StabilizerManager;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;

public class TileEntityMachineStabilizer extends TileEntityMachineMultiblock {
	public TileEntityMachineStabilizer() {
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
		return new ContainerMachine(DialogMachineStabilizer.layout, storage, playerInventory, this.getSyncManager());
	}
	
	@Override
	public GuiContainer createGui(IInventory inventory) {
		return new DialogMachineStabilizer(createContainer(inventory), this);
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
