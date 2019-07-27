package jaminv.advancedmachines.util.recipe;

import java.util.Arrays;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

public class ItemComparable {
	
	private Item item = Items.AIR;
	private int meta = -1;
	private NBTTagCompound nbt = null;
	
	private ItemComparable() {}
	public static final ItemComparable EMPTY = new ItemComparable();	
	
	public ItemComparable(ItemStack stack) {
		item = stack.getItem();
		meta = Items.DIAMOND.getDamage(stack);
		nbt = stack.getTagCompound();		
	}
	
	public boolean isEmpty() {
		return item == Items.AIR;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ItemComparable)) { return false; }
		
		ItemComparable comp = (ItemComparable)obj;
		return (item.equals(comp.item))
			&& (meta == OreDictionary.WILDCARD_VALUE || comp.meta == OreDictionary.WILDCARD_VALUE || meta == comp.meta )
			&& ((nbt == null && comp.nbt == null) || (nbt != null && comp.nbt != null && nbt.equals(comp.nbt)));
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		int ret = item.getRegistryName().hashCode();
		if (nbt != null) { ret += nbt.hashCode(); }
		return ret;
	}
}
