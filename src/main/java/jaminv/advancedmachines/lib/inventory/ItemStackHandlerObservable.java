package jaminv.advancedmachines.lib.inventory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

public class ItemStackHandlerObservable extends ItemStackHandler implements IItemObservable {
	
	private List<IObserver> observers = new ArrayList<>();
	public ItemStackHandlerObservable addObserver(IObserver obv) { observers.add(obv); return this; }
	
	@Override
	protected void onContentsChanged(int slot) {
		for (IObserver obv : observers) {
			obv.onInventoryContentsChanged(slot);
		}
	}

    public ItemStackHandlerObservable() { super(); }
    public ItemStackHandlerObservable(int size) { super(size); }
    public ItemStackHandlerObservable(NonNullList<ItemStack> stacks) { super(stacks); }
	
	boolean allowInsert = true;
	boolean allowExtract = true;
	
	public void setCanInsert(boolean canInsert) { this.allowInsert = canInsert; }
	public void setCanExtract(boolean canExtract) { this.allowExtract = canExtract; }
	
	public boolean canInsert() { return allowInsert; } 
	public boolean canExtract() { return allowExtract; }
}
