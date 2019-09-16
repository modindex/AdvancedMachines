package jaminv.advancedmachines.machine.instance.press;

import jaminv.advancedmachines.lib.container.ContainerMachine;
import jaminv.advancedmachines.lib.container.SyncManager;
import jaminv.advancedmachines.lib.container.layout.ILayoutManager;
import jaminv.advancedmachines.lib.container.layout.ItemLayoutGrid;
import jaminv.advancedmachines.lib.container.layout.JeiLayoutManager;
import jaminv.advancedmachines.lib.inventory.IItemHandlerMachine;
import jaminv.advancedmachines.lib.inventory.slot.ISlotHandler;
import jaminv.advancedmachines.machine.TileMachine;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class TileMachinePress extends TileMachine {
	
	/** Discrete Container class required for JEI */
	public static class ContainerPress extends ContainerMachine {
		public ContainerPress(ILayoutManager layout, IItemHandlerMachine inventory, IInventory playerInventory,
				SyncManager sync) {
			super(layout, inventory, playerInventory, sync);
		}
	}
	
	public class SlotHandlerPress implements ISlotHandler {

		@Override public boolean canInsert(int slot, ItemStack stack) { 
			if (!stack.getItem().getRegistryName().toString().equals("appliedenergistics2:material")) {
				return false;
			}
			if (stack.getMetadata() != 13 && stack.getMetadata() != 14 && stack.getMetadata() != 15 && stack.getMetadata() != 19) {
				return false;
			}
			return true;
		}

		@Override public boolean canExtract(int slot, int amount, ItemStack contents) { return true; }

		@Override
		public int getStackLimit(int slot, int defaultLimit) {
			return 1;
		}
	}
	
	public static final JeiLayoutManager layout	= new JeiLayoutManager()
		.setItemInputLayout(PressManager.getRecipeManager(), 53, 19, 3, 1) 
		.setItemOutputLayout(116, 37)
		.setItemAdditionalLayout(new ItemLayoutGrid(6-24, 6, 4, 1))
		.setInventoryLayout(8, 84)
		.setHotbarLayout(8, 142);
	
	public TileMachinePress() {
		super(PressManager.getRecipeManager());
		inventory.addInputSlots(3);
		inventory.addOutputSlots(1);
		inventory.addAdditionalSlots(4, new SlotHandlerPress());
		controller.includeAdditional(true);
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
