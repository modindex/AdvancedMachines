package jaminv.advancedmachines.util.helper;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

public class ItemHelper {

	
	public static String getOreName(ItemStack stack) {
		int[] ids = OreDictionary.getOreIDs(stack);
		if (ids != null && ids.length >= 1) {
			return OreDictionary.getOreName(ids[0]);
		}
		return "";
	}
	
	public static boolean isBlock(ItemStack stack) {
		return getOreName(stack).startsWith("block");
	}
	
	public static boolean isOre(ItemStack stack) {
		return getOreName(stack).startsWith("ore");
	}
	
	public static boolean isDust(ItemStack stack) {
		return getOreName(stack).startsWith("dust");
	}
	
	public static boolean isIngot(ItemStack stack) {
		return getOreName(stack).startsWith("ingot");
	}
	
	/**
	 * This prevents an overridden getDamage() call from messing up metadata acquisition.
	 */
	public static int getItemDamage(ItemStack stack) {
		return Items.DIAMOND.getDamage(stack);
	}
	
	/**
	 * Determine if the damage of two ItemStacks is equal. Assumes both itemstacks are of type A.
	 */
	public static boolean itemsDamageEqual(ItemStack stackA, ItemStack stackB) {

		return (!stackA.getHasSubtypes() && stackA.getMaxDamage() == 0) || (getItemDamage(stackA) == getItemDamage(stackB));
	}
	
	public static boolean itemsEqual(Item itemA, Item itemB) {

		return itemA != null && itemB != null && (itemA == itemB || itemA.equals(itemB));
	}	
	
	/**
	 * Determine if two ItemStacks have the same Item.
	 */
	public static boolean itemsEqualWithoutMetadata(ItemStack stackA, ItemStack stackB) {

		return stackA != null && stackB != null && itemsEqual(stackA.getItem(), stackB.getItem());
	}

	/**
	 * Determine if two ItemStacks have the same Item and NBT.
	 */
	public static boolean itemsEqualWithoutMetadata(ItemStack stackA, ItemStack stackB, boolean checkNBT) {

		return itemsEqualWithoutMetadata(stackA, stackB) && (!checkNBT || doNBTsMatch(stackA.getTagCompound(), stackB.getTagCompound()));
	}

	/**
	 * Determine if two ItemStacks have the same Item and damage.
	 */
	public static boolean itemsEqualWithMetadata(ItemStack stackA, ItemStack stackB) {

		return itemsEqualWithoutMetadata(stackA, stackB) && itemsDamageEqual(stackA, stackB);
	}

	/**
	 * Determine if two ItemStacks have the same Item, damage, and NBT.
	 */
	public static boolean itemsEqualWithMetadata(ItemStack stackA, ItemStack stackB, boolean checkNBT) {

		return itemsEqualWithMetadata(stackA, stackB) && (!checkNBT || doNBTsMatch(stackA.getTagCompound(), stackB.getTagCompound()));
	}

	/**
	 * Determine if two ItemStacks have the same Item, identical damage, and NBT.
	 */
	public static boolean itemsIdentical(ItemStack stackA, ItemStack stackB) {

		return itemsEqualWithoutMetadata(stackA, stackB) && getItemDamage(stackA) == getItemDamage(stackB) && doNBTsMatch(stackA.getTagCompound(), stackB.getTagCompound());
	}

	/**
	 * Determine if two NBTTagCompounds are equal.
	 */
	public static boolean doNBTsMatch(NBTTagCompound nbtA, NBTTagCompound nbtB) {

		return nbtA == null && nbtB == null || nbtA != null && nbtB != null && nbtA.equals(nbtB);
	}	
}
