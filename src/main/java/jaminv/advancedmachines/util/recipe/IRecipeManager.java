package jaminv.advancedmachines.util.recipe;

import java.util.List;

import net.minecraft.item.ItemStack;

public interface IRecipeManager<T extends RecipeBase> {
	
	public T getRecipe(ItemStack[] stack);
	
	public T getRecipe(RecipeInput[] input);
	
	/**
	 * Returns a valid recipe only if the items *and* count match,
	 * otherwise returns null.
	 * 
	 * @param stack
	 * @return T
	 */
	public T getRecipeMatch(RecipeInput[] input);
	
	/**
	 * This method doesn't make any steps to make sure the recipe
	 * matches, it only compares quantities.  It assumes that the
	 * match has already been made.
	 * 
	 * @param recipe
	 * @param input
	 * @return number of output items that can be crafted
	 */
	public int getRecipeQty(T recipe, RecipeInput[] input);
	
	public int getOutputQty(T recipe, ItemStack[] output);
	
	/**
	 * Returns -1 if invalid
	 * 
	 * @param stack
	 * @param other
	 * @param slot
	 * @return
	 */
	public boolean isItemValid(ItemStack stack, RecipeInput[] other);
	
	public List<T> getRecipeList();
}
