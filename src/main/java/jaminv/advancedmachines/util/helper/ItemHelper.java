package jaminv.advancedmachines.util.helper;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemHelper {
	public static int getItemDamage(ItemStack stack) {
		return Items.DIAMOND.getDamage(stack);
	}
	
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
	
}
