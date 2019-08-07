package jaminv.advancedmachines.objects.blocks.inventory;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.items.ItemStackHandler;

public class ItemStackHandlerObservable extends ItemStackHandler {
	
	public static interface IObserver {
		public void onInventoryContentsChanged(int slot);
	}
	
	public ItemStackHandlerObservable(int size) {
		super(size);
	}
	
	private List<IObserver> observers = new ArrayList<>();
	
	public void addObserver(IObserver obv) {
		observers.add(obv);
	}
	
	public void removeObserver(IObserver obv) {
		observers.remove(obv);
	}
	
	@Override
	protected void onContentsChanged(int slot) {
		for (IObserver obv : observers) {
			obv.onInventoryContentsChanged(slot);
		}
	}
	
	boolean allowInsert = true;
	boolean allowExtract = true;
	
	public void setCanInsert(boolean canInsert) { this.allowInsert = canInsert; }
	public void setCanExtract(boolean canExtract) { this.allowExtract = canExtract; }
	
	public boolean canInsert() { return allowInsert; } 
	public boolean canExtract() { return allowExtract; }
}
