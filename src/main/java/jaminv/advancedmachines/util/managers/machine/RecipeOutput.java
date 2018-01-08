package jaminv.advancedmachines.util.managers.machine;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Helper class for recipe output where the mod material may be disabled
 */
public class RecipeOutput {
	
	private String ore = "";
	private Item item = Items.AIR;
	private int meta = -1;
	private int count = -1;
	private int chance = 100;
	
	RecipeOutput(String oredictName, int count) {
		if(OreDictionary.doesOreNameExist(oredictName)) {
			ore = oredictName;
			this.count = count;
		}
	}
	
	RecipeOutput(String oredictName) {
		this(oredictName, 1);
	}
	
	RecipeOutput(ItemStack stack) {
		item = stack.getItem();
		meta = Items.DIAMOND.getDamage(stack);
		
		if (!stack.isEmpty()) {
			count = stack.getCount();
		}
	}
	
	RecipeOutput(Item item, int count, int meta) {
		this.item = item;
		this.count = count;
		this.meta = meta;
	}
	
	RecipeOutput(Item item) {
		this.item = item;
		this.count = 1;
	}
	
	RecipeOutput(Item item, int count, int meta, boolean config, String oredict) {
		if (config) {
			this.item = item;
			this.count = count;
			this.meta = meta;
		} else {
			this.ore = oredict;
			this.count = count;
		}
	}
	
	public ItemStack toItemStack() {
		if (ore != "") {
			NonNullList<ItemStack> list = OreDictionary.getOres(ore);
			if (list.size() == 0) { return ItemStack.EMPTY; }
			ItemStack result = list.get(0).copy();
			if (count != -1) { result.setCount(count); }
			return result;
		}
		if (item == Items.AIR) { return ItemStack.EMPTY; }
		return new ItemStack(item, count, meta);
	}
	
	public RecipeOutput withChance(int chance) {
		this.chance = chance;
		return this;
	}
	
	public boolean isEmpty() {
		return toItemStack().isEmpty();
	}
	
	@Override
	public String toString() {
		String ret = "RecipeOutput(";
		if (ore != "") {
			return ret + "ore=" + ore + ", count=" + count + ")"; 
		} else {
			return ret + toItemStack() + ")";
		}

	}
	
	public int getChance() {
		return chance;
	}	
}
