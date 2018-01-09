package jaminv.advancedmachines.util.recipe;

import org.apache.logging.log4j.Level;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.util.recipe.machine.PurifierManager.PurifierRecipe;
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
	
	public boolean isItemValid(ItemStack stack, ItemStack[] other);
}
