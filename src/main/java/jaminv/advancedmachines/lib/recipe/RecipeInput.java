package jaminv.advancedmachines.lib.recipe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeInput implements Cloneable, Ingredient {
	
	private int oreId = -1;
	private Item item = Items.AIR;
	private int meta = -1;
	private int count = -1;
	private NBTTagCompound nbt = null;
	private Fluid fluid = null;
	private boolean extract = true;
	
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
		this.fluid = null;
		
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
	
	public RecipeInput(Fluid fluid, int amount, NBTTagCompound nbt) {
		this.item = Items.AIR;
		this.meta = -1;
		
		// Member variable `count` is used interchangeably with 'amount'
		this.count = amount;
		this.fluid = fluid;
		this.nbt = nbt;
	}
	
	public RecipeInput(Fluid fluid, int amount) { this(fluid, amount, null); }
	
	public RecipeInput(FluidStack stack) {
		this(stack.getFluid(), stack.amount, stack.tag);
		lookup.add(new ItemComparable(stack));
	}
	
	public RecipeInput setExtract(boolean extract) { this.extract = extract; return this; }
	public boolean getExtract() { return extract; } 
	
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
	
	private void registerFluids() {
		lookup.add(new ItemComparable(this.toFluidStack()));
	}
	
	public int getCount() { return count; }
	public int getAmount() { return count; }
	
	public ItemStack toItemStack() {
		if (item == Items.AIR) { return ItemStack.EMPTY; }
		return new ItemStack(item, count, meta, nbt);
	}
	
	public FluidStack toFluidStack() {
		return new FluidStack(fluid, count, nbt);
	}
	
	public boolean isEmpty() {
		return oreId == -1 && item == Items.AIR && fluid == null;
	}
	
	public boolean isFluid() { return fluid != null; }
	public boolean isItem() { return !item.equals(Items.AIR); }
	
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
	
	public boolean isValid(FluidStack stack) {
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
	
	public boolean isValidWithAmount(FluidStack stack) {
		return isValid(stack) && stack.amount >= count;
	}
	
	@Override
	public String toString() {
		ToStringHelper helper = MoreObjects.toStringHelper(this);
		if (oreId != -1) {
			helper.add("oreId", oreId).add("ore", OreDictionary.getOreName(oreId));
		} else if (fluid != null) {
			helper.add("fluid", toFluidStack());
		} else {
			helper.add("item", toItemStack());
		}
		return helper.toString();
	}
	
	public List<ItemStack> getItems() {
		return itemlist;
	}
	
	public int getQty(ItemStack stack) {
		return stack.getCount() / count;
	}
	public int getQty(FluidStack stack) {
		return stack.amount / count;
	}
}
