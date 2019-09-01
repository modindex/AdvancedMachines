package jaminv.advancedmachines.machine.instance.press;

import jaminv.advancedmachines.lib.container.ContainerMachine;
import jaminv.advancedmachines.lib.container.ISyncManager;
import jaminv.advancedmachines.lib.container.layout.ILayoutManager;
import jaminv.advancedmachines.lib.container.layout.JeiLayoutManager;
import jaminv.advancedmachines.lib.inventory.IItemHandlerMachine;
import jaminv.advancedmachines.machine.TileMachineMultiblock;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.util.recipe.press.PressManager;
import net.minecraft.inventory.IInventory;

public class TileMachinePress extends TileMachineMultiblock {
	
	/** Discrete Container class required for JEI */
	public static class ContainerPress extends ContainerMachine {
		public ContainerPress(ILayoutManager layout, IItemHandlerMachine inventory, IInventory playerInventory,
				ISyncManager sync) {
			super(layout, inventory, playerInventory, sync);
		}
	}
	
	public static final JeiLayoutManager layout	= new JeiLayoutManager()
		.setItemInputLayout(PressManager.getRecipeManager(), 44, 19, 3, 1) 
		.setItemOutputLayout(98, 23)		
		.setInventoryLayout(8, 84)
		.setHotbarLayout(8, 142);
	
	public TileMachinePress() {
		super(PressManager.getRecipeManager());
		inventory.addInputSlots(3);
		inventory.addOutputSlots(1);
	}

	@Override
	public MachineType getMachineType() {
		return MachineType.PRESS;
	}

	@Override
	public ContainerMachine createContainer(IInventory playerInventory) {
		return new ContainerPress(layout, storage, playerInventory, this.getSyncManager());
	}
}
