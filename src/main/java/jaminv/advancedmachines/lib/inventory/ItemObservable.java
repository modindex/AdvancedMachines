package jaminv.advancedmachines.lib.inventory;

import net.minecraftforge.items.IItemHandler;

public interface ItemObservable extends IItemHandler {

	public static interface IObserver {
		public void onInventoryContentsChanged(int slot);
	}
	
	public ItemObservable addObserver(IObserver observer);
}
