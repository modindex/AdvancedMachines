package jaminv.advancedmachines.machine.instance.grinder;

import jaminv.advancedmachines.lib.container.ContainerMachine;
import jaminv.advancedmachines.lib.container.ISyncManager;
import jaminv.advancedmachines.lib.container.layout.ILayoutManager;
import jaminv.advancedmachines.lib.container.layout.ItemLayoutGrid.HotbarLayout;
import jaminv.advancedmachines.lib.container.layout.ItemLayoutGrid.InventoryLayout;
import jaminv.advancedmachines.lib.container.layout.JeiLayoutManager;
import jaminv.advancedmachines.lib.inventory.IItemHandlerMachine;
import jaminv.advancedmachines.machine.TileMachineMultiblock;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.util.recipe.grinder.GrinderManager;
import net.minecraft.inventory.IInventory;

public class TileMachineGrinder extends TileMachineMultiblock {
	
	/* Discrete Container class required for JEI */
	public static class ContainerGrinder extends ContainerMachine {
		public ContainerGrinder(ILayoutManager layout, IItemHandlerMachine inventory, IInventory playerInventory,
				ISyncManager sync) {
			super(layout, inventory, playerInventory, sync);
		}
	}
	
	public static final JeiLayoutManager layout = new JeiLayoutManager()
		.setItemInputLayout(GrinderManager.getRecipeManager(), 53, 26)
		.setItemOutputLayout(107, 26)
		.setItemSecondaryLayout(107, 52)
		.setInventoryLayout(new InventoryLayout(8, 84))
		.setHotbarLayout(new HotbarLayout(8, 142));	
	
	public TileMachineGrinder() {
		super(GrinderManager.getRecipeManager());
		inventory.addInputSlots(1);
		inventory.addOutputSlots(1);
		inventory.addSecondarySlots(1);
	}
	
	@Override
	public MachineType getMachineType() {
		return MachineType.GRINDER;
	}

	@Override
	public ContainerMachine createContainer(IInventory playerInventory) {
		return new ContainerGrinder(layout, storage, playerInventory, this.getSyncManager());
	}
}
