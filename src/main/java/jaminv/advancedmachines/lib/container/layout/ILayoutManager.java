package jaminv.advancedmachines.lib.container.layout;

import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.IItemHandler;

public interface ILayoutManager {
	public void addInventorySlots(ILayoutUser container, IItemHandler inventory);
	public void addPlayerSlots(ILayoutUser container, IInventory playerInventory);
	
	public int getInventorySlots();
	public int getPlayerSlots();
}