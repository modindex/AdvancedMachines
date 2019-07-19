package jaminv.advancedmachines.util.recipe;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeInput implements Cloneable {
	
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

		getOreId();
	}
	
	public RecipeInput(Item item, int count, int meta, NBTTagCompound nbt) {
		if (item == null) { invalid = true; return; }
		this.item = item;
		this.count = count;
		this.meta = meta;
		this.nbt = nbt;
		getOreId();
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
		return new ItemStack(item, count, meta, nbt);
	}
	
	protected boolean isEqualWithoutNbt(RecipeInput comp) {
		if (comp == null) { return false; }
		
		if (oreId != -1 && oreId == comp.oreId) { return true; }
		
		if (item == comp.item && meta == comp.meta) { return true; }
		
		return false;
	}

	/**
	 * This function is commutative: a.isNbtMatch(b) == b.isNbtMatch(a)
	 */
	protected boolean isNbtMatch(RecipeInput comp) {
		return (nbt == null && comp.nbt == null) || (nbt != null && comp.nbt != null && nbt.equals(comp));		
	}
	
	/**
	 * Are items exactly equal
	 * 
	 * This function is commutative: a.isNbtMatch(b) == b.isNbtMatch(a)
	 * 
	 * This is the strictest requirement. All components, including nbt, must be the same.
	 */
	public boolean isEqual(RecipeInput comp) {
		return isEqualWithoutNbt(comp) && isNbtMatch(comp);
	}
	
	public boolean equals(RecipeInput comp) {
		return isEqual(comp);
	}
	
	/**
	 * This function is not commutative: a.isNbtValid(b) !n= b.isNbtValid(a)
	 * 
	 * This function returns is the NBT contents in a are valid for the requirement b.
	 * If the b has no requirement, then it will be valid even if a has an nbt structure.
	 * 
	 * @param comp
	 * @return
	 */
	protected boolean isNbtValidFor(RecipeInput require) {
		return require.nbt == null || nbt != null && nbt.equals(require.nbt);
	}
	
	public boolean isEmpty() {
		return oreId == -1 && item == Items.AIR;
	}
	
	public boolean isInvalid() {
		return invalid;
	}

	/**
	 * Does input match requirement
	 * 
	 * This function is not commutative: a.isValidFor(b) !n= b.isValidFor(a).
	 * 
	 * This is expected to be called on the actual recipe input with the expected
	 * recipe input as a parameter.  It returns true if the items are the same
	 * and input a meets any nbt requirements. 
	 * 
	 * @param expected
	 * @return
	 */
	public boolean isValidFor(RecipeInput require) {
		return isEqualWithoutNbt(require) && isNbtValidFor(require);		
	}	

	/**
	 * Does actual input match expects
	 * 
	 * This function is not commutative: a.isValidWithCountFor(b) !n= b.isValidWithCountFor(a).
	 * 
	 * This is expected to be called on the actual recipe input with the expected
	 * recipe input as a parameter.  It returns true if the items match
	 * and this.count >= expected.count.  Meaning there must be at least enough
	 * items to meet the expected recipe input. 
	 * 
	 * @param expected
	 * @return
	 */
	public boolean isValidWithCountFor(RecipeInput require) {
		return isValidFor(require) && count >= require.count;		
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
	
	public int getQty(RecipeInput expected) {
		return count / expected.count;
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
