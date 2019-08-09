package jaminv.advancedmachines.lib.inventory;

import net.minecraftforge.items.IItemHandler;

public interface IItemObservable extends IItemHandler {

	public static interface IObserver {
		public void onInventoryContentsChanged(int slot);
	}
	
	public IItemObservable addObserver(IObserver observer);
}
