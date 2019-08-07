package jaminv.advancedmachines.util.recipe;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidTank;

public interface IRecipeManager<T extends RecipeBase> {
	
	public T getRecipe(ItemStack[] stack);
	
	//public T getRecipe(RecipeInput[] input);
	
	/**
	 * Returns a valid recipe only if the items *and* count match,
	 * otherwise returns null.
	 * 
	 * @param stack
	 * @return T
	 */
	public T getRecipeMatch(ItemStack[] input);
	
	/**
	 * This method doesn't make any steps to make sure the recipe
	 * matches, it only compares quantities.  It assumes that the
	 * match has already been made.
	 * 
	 * @param recipe
	 * @param input
	 * @return number of output items that can be crafted
	 */
	public int getRecipeQty(T recipe, ItemStack[] input);
	
	public int getOutputQty(T recipe, ItemStack[] output);
	public int getOutputQty(T recipe, FluidTank output);
	
	/**
	 * Returns -1 if invalid
	 * 
	 * @param stack
	 * @param other
	 * @param slot
	 * @return
	 */
	public default boolean isItemValid(ItemStack stack, ItemStack[] other) {
		return isItemValid(new ItemComparable(stack), new ItemComparableList(other));
	}
	
	public boolean isItemValid(ItemComparable item, ItemComparableList other);
	
	public List<T> getRecipeList();
}
