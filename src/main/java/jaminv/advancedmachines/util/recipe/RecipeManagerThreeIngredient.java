package jaminv.advancedmachines.util.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.util.recipe.machine.PurifierManager;
import jaminv.advancedmachines.util.recipe.machine.PurifierManager.PurifierRecipe;
import net.minecraft.item.ItemStack;

public abstract class RecipeManagerThreeIngredient<T extends RecipeBase> implements IRecipeManager<T> {  
	
	private Map<RecipeInput, HashMap<RecipeInput, HashMap<RecipeInput, T>>> recipes = new HashMap<RecipeInput, HashMap<RecipeInput, HashMap<RecipeInput, T>>>();
	private Map<RecipeInput, Boolean> validInput = new HashMap<RecipeInput, Boolean>();
	
	protected void addRecipe(T recipe) {
		RecipeInput[] input = recipe.getSortedInput();
		
		boolean valid = false;
		for (RecipeInput item : input) {
			if (item.isInvalid()) { return; }
			if (!item.isEmpty()) { valid = true; }
		}
		if (!valid) { return; }
		
		for (int i = 0; i < recipe.getOutputCount(); i++) {
			if (recipe.getOutput(i).isEmpty()) { return; }
		}
		
		HashMap<RecipeInput, HashMap<RecipeInput, T>> second = recipes.get(input[0]);
		if (second == null) {
			second = new HashMap<RecipeInput, HashMap<RecipeInput, T>>();
			recipes.put(input[0], second);
		}
		
		HashMap<RecipeInput, T> third = second.get(input[1]);
		if (third == null) {
			third = new HashMap<RecipeInput, T>();
			second.put(input[1], third);
		}
		
		third.put(input[2], recipe);
		
		for (RecipeInput item : input) {
			if (!item.isEmpty()) {
				validInput.put(item, true);
			}
		}
	}
	
	/**
	 * Returns NULL if no recipe available
	 * @param stack
	 * @return PurifierRecipe
	 */
	public T getRecipe(ItemStack[] stack) {
		RecipeInput[] input = new RecipeInput[stack.length];
		for (int i = 0; i < stack.length; i++) {
			input[i] = new RecipeInput(stack[i]);
		}
		return this.getRecipe(input);		
	}
	
	public T getRecipe(RecipeInput[] input) {
		for (RecipeInput item : input) {
			if (input == null) { return null; }
		}
		RecipeInput[] copy = Arrays.copyOf(input, input.length);
		Arrays.sort(copy, new RecipeInput.InputCompare());
		
		HashMap<RecipeInput, HashMap<RecipeInput, T>> second = recipes.get(copy[0]);
		if (second == null) { return null; }
		
		HashMap<RecipeInput, T> third = second.get(copy[1]);
		if (third == null) { return null; }
		
		return third.get(copy[2]);
	}
	
	/**
	 * Returns a valid recipe only if the items *and* count match,
	 * otherwise returns null.
	 * 
	 * @param stack
	 * @return PurifierRecipe
	 */
	public T getRecipeMatch(RecipeInput[] input) {
		T recipe = getRecipe(input);
		if (recipe == null) { return null; }
		
		RecipeInput[] copy = Arrays.copyOf(input, input.length);
		Arrays.sort(copy, new RecipeInput.InputCompare());		
		
		for (int i = 0; i < recipe.getInputCount(); i++) {
			if (!copy[i].doesMatch(recipe.getInput(i))) { return null; }
		}
		return recipe;
	}
	
	@Override
	public int getRecipeQty(T recipe, RecipeInput[] input) {
		RecipeInput[] copy = Arrays.copyOf(input, input.length);
		Arrays.sort(copy, new RecipeInput.InputCompare());
		
		int min = -1;
		for (int i = 0; i < recipe.getInputCount(); i++) {
			int qty = input[i].getQty(recipe.getInput(i));
			if (min == -1 || qty < min) {
				min = qty;
			}
		}
		return min;
	}
	
	@Override
	public int getOutputQty(T recipe, ItemStack[] output) {
		return recipe.getOutputQty(output);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack, ItemStack[] other) {
		if (stack.isEmpty()) { return false; }
		return validInput.get(new RecipeInput(stack)) != null;
	}
	
	@Override
	public List<T> getRecipeList() {
		ArrayList<T> ret = new ArrayList<T>();
		
		for (Map.Entry<RecipeInput, HashMap<RecipeInput, HashMap<RecipeInput, T>>> level1 : this.recipes.entrySet()) {
			for (Map.Entry<RecipeInput, HashMap<RecipeInput, T>> level2 : level1.getValue().entrySet()) {
				for (Map.Entry<RecipeInput, T> entry : level2.getValue().entrySet()) {
					ret.add(entry.getValue());
				}
			}
		}
		return ret;
	}	
}
