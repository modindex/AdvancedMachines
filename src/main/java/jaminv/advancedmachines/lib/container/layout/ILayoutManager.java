package jaminv.advancedmachines.lib.container.layout;

import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.IItemHandler;

public interface ILayoutManager {

	void addInventorySlots(ILayoutUser container, IItemHandler inventory);

	void addPlayerSlots(ILayoutUser container, IInventory playerInventory);

}