package jaminv.advancedmachines.proxy;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;

public interface HasGui {
	public Container createContainer(IInventory inventory);
}
