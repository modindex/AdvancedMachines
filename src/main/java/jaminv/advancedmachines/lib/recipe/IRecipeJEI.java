package jaminv.advancedmachines.lib.recipe;

import net.minecraft.item.ItemStack;

public interface IRecipeJEI extends IRecipe {
	public ItemStack[] getRecipeItems(int index);
}
