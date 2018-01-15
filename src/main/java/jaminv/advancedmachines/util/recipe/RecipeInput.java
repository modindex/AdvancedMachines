package jaminv.advancedmachines.util.recipe;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeInput {
	
	public static class InputCompare implements Comparator<RecipeInput> {
		@Override
		public int compare(RecipeInput arg0, RecipeInput arg1) {
			return arg0.hashCode() - arg1.hashCode();
		}		
	}
	
	private int oreId = -1;
	private Item item = Items.AIR;
	private int meta = -1;
	private int count = -1;
	
	private boolean invalid = false;
	
	public static final RecipeInput EMPTY = new RecipeInput(Items.AIR, -1, -1);
	
	public RecipeInput(String oredictName, int count) {
		if(OreDictionary.doesOreNameExist(oredictName)) {
			oreId = OreDictionary.getOreID(oredictName);
			this.count = count;
		} else {
			invalid = true;
		}
	}
	
	public RecipeInput(String oredictName) {
		this(oredictName, 1);
	}
	
	public RecipeInput(ItemStack stack) {
		if (stack == null || stack.isEmpty()) { invalid = true; return; }
		item = stack.getItem();
		meta = Items.DIAMOND.getDamage(stack);
		
		if (!stack.isEmpty()) {
			count = stack.getCount();
		}

		getOreId();
	}
	
	public RecipeInput(Item item, int count, int meta) {
		if (item == null) { invalid = true; return; }
		this.item = item;
		this.count = count;
		this.meta = meta;
		getOreId();
	}
	
	public RecipeInput() {
		this(Items.AIR, -1, -1);
	}
	
	public RecipeInput(Item item) {
		this(item, 1, 0);
	}
	
	private void getOreId() {
		if (isEmpty()) { oreId = -1; return; }
		int[] ids = OreDictionary.getOreIDs(this.toItemStack());
		if (ids.length == 0) { oreId = -1; return; }
		oreId = ids[0];
	}
	
	public int getCount() {
		return count;
	}
	
	public ItemStack toItemStack() {
		if (item == Items.AIR) { return ItemStack.EMPTY; }
		return new ItemStack(item, count, meta);
	}
	
	public boolean isEqual(RecipeInput comp) {
		if (comp == null) { return false; }
		
		if (oreId != -1 && oreId == comp.oreId) { return true; }
		
		if (item == comp.item && meta == comp.meta) { return true; }
		
		return false;
	}
	
	public boolean isEmpty() {
		return oreId == -1 && item == Items.AIR;
	}
	
	public boolean isInvalid() {
		return invalid;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof RecipeInput && isEqual((RecipeInput)obj);
	}

	/**
	 * Does actual input match expects
	 * 
	 * This function is not commutative: a.doesMatch(b) != b.doesMatch(a).
	 * 
	 * This is expected to be called on the actual recipe input with the expected
	 * recipe input as a parameter.  It returns true if the items match
	 * and this.count >= expected.count.  Meaning there must be at least enough
	 * items to meet the expected recipe input. 
	 * 
	 * @param expected
	 * @return
	 */
	public boolean doesMatch(RecipeInput expected) {
		return isEqual(expected) && count >= expected.count;		
	}
	
	@Override
	public int hashCode() {
		if (oreId != -1) { return oreId; }
		return item.getIdFromItem(item) | (meta & 0xFFFF);
	}
	
	@Override
	public String toString() {
		String ret = "RecipeInput(";
		if (oreId != -1) {
			return ret + "oreId=" + oreId + ", ore=" + OreDictionary.getOreName(oreId) + ")";
		} else {
			return ret + toItemStack() + ")";
		}
	}
	
	public List<ItemStack> getItems() {
		List<ItemStack> ret = new ArrayList<ItemStack>();
		
		if (this.isEmpty()) { return ret; }
		if (oreId == -1) {
			ret.add(this.toItemStack());
			return ret;
		}
		
		for (ItemStack ore : OreDictionary.getOres(OreDictionary.getOreName(this.oreId), false)) {
			ItemStack copy = ore.copy();
			copy.setCount(this.count);
			ret.add(copy);
		}
		
		return ret;
	}
}
