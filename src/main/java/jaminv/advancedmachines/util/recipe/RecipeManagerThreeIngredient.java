package jaminv.advancedmachines.util.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.util.helper.ItemHelper;
import jaminv.advancedmachines.util.recipe.machine.PurifierManager;
import jaminv.advancedmachines.util.recipe.machine.PurifierManager.PurifierRecipe;
import net.minecraft.item.ItemStack;

public abstract class RecipeManagerThreeIngredient<T extends RecipeBase> implements IRecipeManager<T> {  
	
	private Map<RecipeInput, HashMap<RecipeInput, HashMap<RecipeInput, T>>> recipes = new HashMap<RecipeInput, HashMap<RecipeInput, HashMap<RecipeInput, T>>>();
	private Map<RecipeInput, ArrayList<T>> validInput = new HashMap<RecipeInput, ArrayList<T>>();
	
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
				ArrayList<T> recipelist = validInput.get(item);
				if (recipelist == null) {
					recipelist = new ArrayList<T>();
					recipelist.add(recipe);
					validInput.put(item, recipelist);
				} else {
					recipelist.add(recipe);
				}
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
		
		RecipeInput[] rinput = recipe.getSortedInput();
		
		int min = -1;
		for (int i = 0; i < recipe.getInputCount(); i++) {
			if (copy[i].isEmpty()) { continue; }
			int qty = copy[i].getQty(rinput[i]);
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
	public boolean isItemValid(ItemStack stack, RecipeInput[] other) {
		if (stack.isEmpty()) { return false; }
		RecipeInput item = new RecipeInput(stack);
		
		if (other == null) {
			return validInput.get(item) != null;
		}
		
		int empty = 0;
		// First just check to see if it stacks with the current contents
		for (int i = 0; i < other.length; i++) {
			if (other[i].isEmpty()) { empty++; continue; }			
			
			if(other[i].equals(item)) {
				// There's no good way to handle two of the same ingredient in a recipe, and there should be no reason to do so
				// TODO: Prevent two of the same ingredient in a recipe
				
				// Inventory managers need to separately manage whether the current item can stack with existing items.
				// If the current item is already in the input inventory, we just reject it
				return false;
			}
		}
		if (empty == 0) { return false; }
		empty--; // The current ingredient
		
		ArrayList<T> recipelist = validInput.get(item);
		if (recipelist == null) {
			// Ingredient isn't valid at all
			return false;
		}
		
		for (int i = 0; i < recipelist.size(); i++) {
			T recipe = recipelist.get(i);
			
			// Not found tracks the number of recipe ingredients that weren't found
			int notfound = 0;
			// Ingredients tracks the input ingredients to ensure that they are all part of the recipe
			boolean[] ingredient = new boolean[other.length];
			Arrays.fill(ingredient, false);
			
			for (int j = 0; j < recipe.getInputCount(); j++) {
				RecipeInput input = recipe.getInput(j);
				if (input.isEmpty()) { continue; }
				
				boolean found = false;
				
				for (int k = 0; k < other.length; k++) {
					if (other[k].equals(item)) { found = true; break;}
					if (other[k].isEmpty()) { ingredient[k] = true; continue; }
					
					if (input.equals(other[k])) { ingredient[k] = true; found = true; break; }
				}
				
				if (!found) { 
					notfound++; 
					if (notfound > empty) { break; }
				}
			}
			
			for (int j = 0; j < ingredient.length; j++) {
				if (ingredient[j] == false) { notfound = empty + 1; }
			}
			if (notfound <= empty) { return true; }
		}
		return false;		
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
