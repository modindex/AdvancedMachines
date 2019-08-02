package jaminv.advancedmachines.util.recipe;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import jaminv.advancedmachines.util.helper.ItemHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeInput implements Cloneable {
	
	private int oreId = -1;
	private Item item = Items.AIR;
	private int meta = -1;
	private int count = -1;
	private NBTTagCompound nbt = null;
	
	private boolean invalid = false;
		
	public static final RecipeInput EMPTY = new RecipeInput(Items.AIR, -1, -1);
	
	public RecipeInput(String oredictName, int count) {
		if(OreDictionary.doesOreNameExist(oredictName)) {
			oreId = OreDictionary.getOreID(oredictName);
			this.count = count;
		} else {
			invalid = true;
		}
		
		registerItems();
	}
	
	public RecipeInput(String oredictName) {
		this(oredictName, 1);
	}
	
	public RecipeInput(ItemStack stack) {
		if (stack == null || stack.isEmpty()) { invalid = true; return; }
		item = stack.getItem();
		meta = Items.DIAMOND.getDamage(stack);
		nbt = stack.getTagCompound();
		
		if (!stack.isEmpty()) {
			count = stack.getCount();
		}
		
		registerItems();
	}
	
	public RecipeInput(Item item, int count, int meta, NBTTagCompound nbt) {
		if (item == null) { invalid = true; return; }
		this.item = item;
		this.count = count;
		this.meta = meta;
		this.nbt = nbt;
		
		registerItems();
	}
	
	public RecipeInput(Item item, int count, int meta) {
		this(item, count, meta, null);
	}
	
	public RecipeInput() {
		this(Items.AIR, -1, -1);
	}
	
	public RecipeInput(Item item) {
		this(item, 1, 0);
	}
	
	List<ItemStack> itemlist = new ArrayList<ItemStack>();
	HashSet<ItemComparable> lookup = new HashSet<ItemComparable>();
	
	private void registerItems() {
		if (this.isEmpty()) { return; }
		if (oreId == -1) {
			ItemStack stack = this.toItemStack();
			itemlist.add(stack);
			lookup.add(new ItemComparable(stack));
		}
		
		for (ItemStack ore : OreDictionary.getOres(OreDictionary.getOreName(this.oreId), false)) {
			ItemStack copy = ore.copy();
			copy.setCount(this.count);
			itemlist.add(copy);
			lookup.add(new ItemComparable(copy));
		}
	}
	
	public int getCount() {
		return count;
	}
	
	public ItemStack toItemStack() {
		if (item == Items.AIR) { return ItemStack.EMPTY; }
		return new ItemStack(item, count, meta, nbt);
	}
	
	public boolean isEmpty() {
		return oreId == -1 && item == Items.AIR;
	}
	
	public boolean hasError() {
		return invalid;
	}

	/**
	 * Does input match requirement
	 * 
	 * Returns true if the items are the same and meets any nbt requirements. 
	 * 
	 * @param expected
	 * @return
	 */
	public boolean isValid(ItemStack stack) {
		return lookup.contains(new ItemComparable(stack));		
	}
	
	public boolean isValid(ItemComparable stack) {
		return lookup.contains(stack);
	}

	/**
	 * Item valid for recipe, including count
	 * 
	 * It returns true if the items match and stack.getCount() >= this.count.
	 * Meaning there must be at least enough items to meet the recipe input. 
	 * 
	 * @param expected
	 * @return
	 */
	public boolean isValidWithCount(ItemStack stack) {
		return isValid(stack) && stack.getCount() >= count;		
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
		return itemlist;
	}
	
	public int getQty(ItemStack stack) {
		return stack.getCount() / count;
	}
	
	public RecipeInput multiply(int factor) {
		RecipeInput ret;
		try {
			ret = (RecipeInput) this.clone();
		} catch (CloneNotSupportedException e) {
			return RecipeInput.EMPTY;
		}
		ret.count = ret.count * factor;
		return ret;
	}
}
