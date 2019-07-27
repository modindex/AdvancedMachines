package jaminv.advancedmachines.util.recipe;

import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import net.minecraft.item.ItemStack;

public class ItemComparableList {
	protected ArrayList<ItemComparable> list = new ArrayList<ItemComparable>();
	boolean sorted = false;
	
	public static class ItemCompare implements Comparator<ItemComparable> {
		@Override
		public int compare(ItemComparable arg0, ItemComparable arg1) {
			return arg0.hashCode() - arg1.hashCode();
		}		
	}	
	
	public ItemComparableList() {}
	
	public ItemComparableList(ItemStack[] stack) {
		if (stack == null) { return; }
		for (ItemStack item : stack) {
			add(new ItemComparable(item));
		}
		sort();
	}
	
	public boolean add(ItemComparable item) {
		if (item.isEmpty()) { return false; }
		sorted = false;
		return list.add(item);
	}
	
	public void sort() {
		if (sorted) { return; }
		list.sort(new ItemCompare());
		sorted = true;
	}
	
	public int size() {
		return list.size();
	}
	
	public ItemComparable get(int index) {
		return list.get(index);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ItemComparableList)) { return false; }
		
		ItemComparableList comp = (ItemComparableList)obj;
		if (comp.list.size() != list.size()) { return false; }
		
		sort(); comp.sort();
		
		for (int i = 0; i < list.size(); i++) {
			if (!list.get(i).equals(comp.list.get(i))) { return false; }
		}
		
		return true;
	}
}
