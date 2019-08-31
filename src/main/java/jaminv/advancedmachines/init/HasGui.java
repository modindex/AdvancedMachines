package jaminv.advancedmachines.init;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;

public interface HasGui {
	public Container createContainer(IInventory inventory);
}
