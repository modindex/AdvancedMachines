package jaminv.advancedmachines.lib.recipe;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Helper class for recipe output where the mod material may be disabled
 */
public class RecipeOutput implements Cloneable, RecipeJei.Secondary {
	
	public static String[] oreDictionaryPreference = {};
	public static void setOreDictionaryPreference(String[] preference) {
		oreDictionaryPreference = preference;
	}
	
	private String ore = "";
	private Item item = Items.AIR;
	private Fluid fluid = null;
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
	
	public RecipeOutput(Fluid fluid, int amount, NBTTagCompound nbt) {
		// Member variable `count` is used interchangeably with 'amount'
		this.count = amount;
		this.fluid = fluid;
		this.nbt = nbt;
	}
	
	public RecipeOutput(Fluid fluid, int amount) { this(fluid, amount, null); }
	
	public RecipeOutput(FluidStack stack) {
		this(stack.getFluid(), stack.amount, stack.tag);
	}
	
	public boolean hasError() {
		return invalid;
	}
	
	private ItemStack itemstack = null;	
	public ItemStack toItemStack() {
		if (fluid != null) { return null; }
		if (itemstack != null) { ItemStack ret = itemstack.copy(); ret.setCount(count); return ret; }
		
		if (ore != "") {
			NonNullList<ItemStack> list = OreDictionary.getOres(ore);
			if (list.size() == 0) { return ItemStack.EMPTY; }
			
			int min = Integer.MAX_VALUE;
			ItemStack minvalue = null;
			
			for (ItemStack item : list) {
				for (int i = 0; i < oreDictionaryPreference.length; i++) {
					if (item.getItem().getRegistryName().getResourceDomain().equals(oreDictionaryPreference[i])) {
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
			if (Items.DIAMOND.getDamage(itemstack) == OreDictionary.WILDCARD_VALUE) { itemstack.setItemDamage(0); }
			return itemstack.copy();
		}
		if (item == Items.AIR) { return ItemStack.EMPTY; }
		itemstack = new ItemStack(item, count, meta, nbt);
		return itemstack.copy();
	}
	
	public FluidStack toFluidStack() {
		if (fluid == null) { return null; }
		return new FluidStack(fluid, count, nbt);
	}
	
	public RecipeOutput withChance(int chance) {
		this.chance = chance;
		return this;
	}
	
	public boolean isEmpty() {
		if (fluid != null) { return false; }
		return toItemStack().isEmpty();
	}
	
	public boolean isFluid() { return fluid != null; }
	public boolean isItem() { return !item.equals(Items.AIR); }
	
	@Override
	public String toString() {
		ToStringHelper helper = MoreObjects.toStringHelper(this);
		if (ore != "") {
			helper.add("ore", ore).add("count", count);
		} else if(fluid != null) {
			helper.add("fluid", toFluidStack());
		} else {
			helper.add("item", toItemStack());
		}
		return helper.toString();
	}
	
	public int getChance() {
		return chance;
	}
}
