package jaminv.advancedmachines.util.recipe;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

/**
 * Recipe Manager Interface Definition
 * 
 * `RecipeManager` is the de facto implementation of this interface, and should work for most purposes.
 * This still exists as a generic interface, however, in case there is something that it can't handle.
 * Auto-crafting machines, for example, can't be handled by `RecipeManager`.
 * 
 * External code should use `IRecipeManger` as the variable type rather than a specific implementation.
 * 
 * @author jamin
 * @param <T>
 */
public interface IRecipeManager<T extends RecipeBase> {
	public T getRecipe(ItemComparableList input);
	public default T getRecipe(ItemStack[] input) {	return getRecipe(new ItemComparableList(input)); }

	public default boolean isItemValid(ItemStack stack) {
		return isItemValid(new ItemComparable(stack), (ItemComparableList)null);
	}
	public default boolean isItemValid(ItemStack stack, @Nullable ItemStack[] other) {
		return isItemValid(new ItemComparable(stack), new ItemComparableList(other));
	}
	public boolean isItemValid(ItemComparable item, ItemComparableList iclist);	
}
