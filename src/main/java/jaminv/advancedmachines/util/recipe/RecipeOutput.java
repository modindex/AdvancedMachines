package jaminv.advancedmachines.util.recipe;

import jaminv.advancedmachines.util.Reference;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.GameRegistry;
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
	
	public static final RecipeOutput EMPTY = new RecipeOutput(Items.AIR, -1, -1);
	
	public RecipeOutput(String oredictName, int count) {
		if(OreDictionary.doesOreNameExist(oredictName)) {
			ore = oredictName;
			this.count = count;
		}
	}
	
	public RecipeOutput(String oredictName) {
		this(oredictName, 1);
	}
	
	public RecipeOutput(ItemStack stack) {
		item = stack.getItem();
		meta = Items.DIAMOND.getDamage(stack);
		
		if (!stack.isEmpty()) {
			count = stack.getCount();
		}
	}
	
	public RecipeOutput(Item item, int count, int meta) {
		this.item = item;
		this.count = count;
		this.meta = meta;
	}
	
	public RecipeOutput(Item item) {
		this.item = item;
		this.count = 1;
	}
	
	public ItemStack toItemStack() {
		if (ore != "") {
			NonNullList<ItemStack> list = OreDictionary.getOres(ore);
			if (list.size() == 0) { return ItemStack.EMPTY; }
			
			ItemStack toCopy = list.get(0);			
			for (ItemStack item : list) {
				if (item.getItem().getRegistryName().getResourceDomain().equals(Reference.MODID)) {
					toCopy = item;
					break;
				}
			}
			ItemStack result = toCopy.copy();
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
