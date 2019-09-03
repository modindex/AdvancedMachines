package jaminv.advancedmachines.machine.instance.alloy;

import jaminv.advancedmachines.lib.container.ContainerMachine;
import jaminv.advancedmachines.lib.container.ISyncManager;
import jaminv.advancedmachines.lib.container.layout.ILayoutManager;
import jaminv.advancedmachines.lib.container.layout.JeiLayoutManager;
import jaminv.advancedmachines.lib.inventory.IItemHandlerMachine;
import jaminv.advancedmachines.machine.TileMachineMultiblock;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import net.minecraft.inventory.IInventory;

public class TileMachineAlloy extends TileMachineMultiblock {
	
	/* Discrete Container class required for JEI */
	public static class ContainerAlloy extends ContainerMachine {
		public ContainerAlloy(ILayoutManager layout, IItemHandlerMachine inventory, IInventory playerInventory,
				ISyncManager sync) {
			super(layout, inventory, playerInventory, sync);
		}
	}
	
	public static final JeiLayoutManager layout = new JeiLayoutManager()
			.setItemInputLayout(AlloyManager.getRecipeManager(), 35, 37, 1, 3) 
			.setItemOutputLayout(125, 37)		
			.setInventoryLayout(8, 84)
			.setHotbarLayout(8, 142);	
	
	public TileMachineAlloy() {
		super(AlloyManager.getRecipeManager());
		inventory.addInputSlots(3);
		inventory.addOutputSlots(1);
	}

	@Override
	public MachineType getMachineType() {
		return MachineType.ALLOY;
	}

	@Override
	public ContainerMachine createContainer(IInventory playerInventory) {
		return new ContainerAlloy(layout, storage, playerInventory, this.getSyncManager());
	}
}
