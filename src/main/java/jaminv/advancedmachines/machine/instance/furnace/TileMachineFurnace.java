package jaminv.advancedmachines.machine.instance.furnace;

import jaminv.advancedmachines.lib.container.ContainerMachine;
import jaminv.advancedmachines.lib.container.ISyncManager;
import jaminv.advancedmachines.lib.container.layout.ILayoutManager;
import jaminv.advancedmachines.lib.container.layout.ItemLayoutGrid.HotbarLayout;
import jaminv.advancedmachines.lib.container.layout.ItemLayoutGrid.InventoryLayout;
import jaminv.advancedmachines.lib.container.layout.JeiLayoutManager;
import jaminv.advancedmachines.lib.inventory.IItemHandlerMachine;
import jaminv.advancedmachines.machine.TileMachineMultiblock;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.util.recipe.FurnaceManager;
import net.minecraft.inventory.IInventory;

public class TileMachineFurnace extends TileMachineMultiblock {
	
	/* Discrete Container class required for JEI */
	public static class ContainerFurnace extends ContainerMachine {
		public ContainerFurnace(ILayoutManager layout, IItemHandlerMachine inventory, IInventory playerInventory,
				ISyncManager sync) {
			super(layout, inventory, playerInventory, sync);
		}
	}
	
	public static final JeiLayoutManager layout = new JeiLayoutManager()
			.setItemInputLayout(FurnaceManager.getRecipeManager(), 53, 37)
			.setItemOutputLayout(107, 37)
			.setInventoryLayout(new InventoryLayout(8, 84))
			.setHotbarLayout(new HotbarLayout(8, 142));
	
	public TileMachineFurnace() {
		super(FurnaceManager.getRecipeManager());
		inventory.addInputSlots(1);
		inventory.addOutputSlots(1);
	}
	
	@Override
	public MachineType getMachineType() {
		return MachineType.FURNACE;
	}

	@Override
	public ContainerMachine createContainer(IInventory playerInventory) {
		return new ContainerFurnace(layout, storage, playerInventory, this.getSyncManager());
	}
}
