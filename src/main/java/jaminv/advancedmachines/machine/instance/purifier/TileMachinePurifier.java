package jaminv.advancedmachines.machine.instance.purifier;

import jaminv.advancedmachines.lib.container.ContainerMachine;
import jaminv.advancedmachines.lib.container.SyncManager;
import jaminv.advancedmachines.lib.container.layout.ILayoutManager;
import jaminv.advancedmachines.lib.container.layout.ItemLayoutGrid.HotbarLayout;
import jaminv.advancedmachines.lib.container.layout.ItemLayoutGrid.InventoryLayout;
import jaminv.advancedmachines.lib.container.layout.JeiLayoutManager;
import jaminv.advancedmachines.lib.inventory.ItemHandlerSeparated;
import jaminv.advancedmachines.machine.TileMachine;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import net.minecraft.inventory.IInventory;

public class TileMachinePurifier extends TileMachine {
	
	/** Discrete Container class required by JEI */
	public static class ContainerPurifier extends ContainerMachine {
		public ContainerPurifier(ILayoutManager layout, ItemHandlerSeparated inventory, IInventory playerInventory,
				SyncManager sync) {
			super(layout, inventory, playerInventory, sync);
		}
	}

	public static final JeiLayoutManager layout = new JeiLayoutManager()
		.setItemInputLayout(PurifierManager.getRecipeManager(), 53, 23)
		.setItemOutputLayout(107, 23)
		.setItemSecondaryLayout(35, 55, 1, 6)
		.setInventoryLayout(new InventoryLayout(8, 84))
		.setHotbarLayout(new HotbarLayout(8, 142));
	
	public TileMachinePurifier() {
		super(PurifierManager.getRecipeManager());
		inventory.addInputSlots(1);
		inventory.addOutputSlots(1);
		inventory.addSecondarySlots(6);
	}
	
	@Override
	public MachineType getMachineType() {
		return MachineType.PURIFIER;
	}

	@Override
	public ContainerMachine createContainer(IInventory playerInventory) {
		return new ContainerPurifier(layout, storage, playerInventory, this.getSyncManager());
	}

}
