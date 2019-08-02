package jaminv.advancedmachines.util.recipe;

import jaminv.advancedmachines.util.ModConfig;
import jaminv.advancedmachines.util.Reference;
import jaminv.advancedmachines.util.helper.ItemHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Helper class for recipe output where the mod material may be disabled
 */
public class RecipeOutput implements Cloneable {
	
	private String ore = "";
	private Item item = Items.AIR;
	private int meta = -1;
	private int count = -1;
	private int chance = 100;
	private NBTTagCompound nbt = null;
	boolean invalid = false;
	
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
		if (stack == null || stack.isEmpty()) { invalid = true; return; }
		item = stack.getItem();
		meta = Items.DIAMOND.getDamage(stack);
		nbt = stack.getTagCompound();
		
		if (!stack.isEmpty()) {
			count = stack.getCount();
		}
	}
	
	public RecipeOutput(Item item, int count, int meta, NBTTagCompound nbt) {
		if (item == null) { invalid = true; return; }
		this.item = item;
		this.count = count;
		this.meta = meta;
		this.nbt = nbt;
	}
	
	public RecipeOutput(Item item, int count, int meta) {
		this(item, count, meta, null);
	}
	
	public RecipeOutput(Item item) {
		this.item = item;
		this.count = 1;
	}
	
	public boolean hasError() {
		return invalid;
	}
	
	private ItemStack itemstack = null;	
	public ItemStack toItemStack() {
		if (itemstack != null) { ItemStack ret = itemstack.copy(); ret.setCount(count); return ret; }
		
		if (ore != "") {
			NonNullList<ItemStack> list = OreDictionary.getOres(ore);
			if (list.size() == 0) { return ItemStack.EMPTY; }
			
			int min = Integer.MAX_VALUE;
			ItemStack minvalue = null;
			
			for (ItemStack item : list) {
				for (int i = 0; i < ModConfig.recipe.oreDictionaryPreference.length; i++) {
					if (item.getItem().getRegistryName().getResourceDomain().equals(ModConfig.recipe.oreDictionaryPreference[i])) {
						if (i < min) {
							min = i;
							minvalue = item;
						}
						break;
					}
				}
				if (minvalue == null) { minvalue = item; }
			}
			
			itemstack = minvalue.copy();
			if (count != -1) { itemstack.setCount(count); }
			if (ItemHelper.getMeta(itemstack) == OreDictionary.WILDCARD_VALUE) { itemstack.setItemDamage(0); }
			return itemstack.copy();
		}
		if (item == Items.AIR) { return ItemStack.EMPTY; }
		itemstack = new ItemStack(item, count, meta, nbt);
		return itemstack.copy();
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
	
	public RecipeOutput multiply(int factor) {
		RecipeOutput ret;
		try {
			ret = (RecipeOutput) this.clone();
		} catch (CloneNotSupportedException e) {
			return RecipeOutput.EMPTY;
		}
		ret.count = ret.count * factor;
		return ret;
	}	
}
