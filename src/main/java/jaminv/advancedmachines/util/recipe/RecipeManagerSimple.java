package jaminv.advancedmachines.util.recipe;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.util.recipe.machine.PurifierManager;
import jaminv.advancedmachines.util.recipe.machine.PurifierManager.PurifierRecipe;
import net.minecraft.item.ItemStack;

public abstract class RecipeManagerSimple<T extends RecipeBase> implements IRecipeManager<T> {  
	
	private Map<RecipeInput, T> recipes = new HashMap<RecipeInput, T>();
	
	protected void addRecipe(T recipe) {
		if (recipe.getInput(0).isEmpty()) { return; }
		for (int i = 0; i < recipe.getOutputCount(); i++) {
			if (recipe.getOutput(i).isEmpty()) { return; }
		}
		
		recipes.put(recipe.getInput(0), recipe);
	}
	
	/**
	 * Returns NULL if no recipe available
	 * @param stack
	 * @return PurifierRecipe
	 */
	public T getRecipe(ItemStack[] stack) {
		if (stack[0].isEmpty()) { return null; }
		RecipeInput item = new RecipeInput(stack[0]);
		return recipes.get(item);
	}
	
	public T getRecipe(RecipeInput[] input) {
		if (input == null) { return null; }
		return recipes.get(input[0]);
	}
	
	/**
	 * Returns a valid recipe only if the items *and* count match,
	 * otherwise returns null.
	 * 
	 * @param stack
	 * @return PurifierRecipe
	 */
	public T getRecipeMatch(RecipeInput[] input) {
		if (input[0].isEmpty()) { return null; }
		T recipe = recipes.get(input[0]);
		if (recipe == null) { return null; }
		
		if (!input[0].doesMatch(recipe.getInput(0))) { return null; }
		return recipe;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack, ItemStack[] other) {
		if (stack.isEmpty()) { return false; }
		return recipes.get(new RecipeInput(stack)) != null;
	}
}
