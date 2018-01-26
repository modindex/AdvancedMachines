package jaminv.advancedmachines.objects.blocks.machine.expansion.inventory;

import jaminv.advancedmachines.objects.blocks.inventory.DialogInventory;

public class DialogMachineInventory extends DialogInventory {
	
	public DialogMachineInventory() {
		super("textures/gui/machine_inventory.png", 24, 0, 176, 177);
		
		this.addLayout(new InventoryLayout(8, 30));
		
		this.setInventoryLayout(new InventoryLayout(8, 95));
		this.setHotbarLayout(new HotbarLayout(8, 153));
	}
}
