package jaminv.advancedmachines.util.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.util.helper.ItemHelper;
import jaminv.advancedmachines.util.recipe.machine.purifier.PurifierManager;
import jaminv.advancedmachines.util.recipe.machine.purifier.PurifierManager.PurifierRecipe;
import net.minecraft.item.ItemStack;

public abstract class RecipeManagerThreeIngredient<T extends RecipeBase> implements IRecipeManager<T> {  
	
	private Map<ItemComparableList, T> lookup = new HashMap<ItemComparableList, T>();
	private Map<ItemComparable, ArrayList<T>> validInput = new HashMap<ItemComparable, ArrayList<T>>();
	private ArrayList<T> list = new ArrayList<T>();
	
	protected void addRecipe(T recipe) {
		//RecipeInput[] input = recipe.getSortedInput();
		
		boolean valid = false;
		for (int i = 0; i < recipe.getInputCount(); i++) {
			RecipeInput item = recipe.getInput(i);
			if (item.hasError()) { return; }
			if (!item.isEmpty()) { valid = true; }
		}
		if (!valid) { return; }
		
		for (int i = 0; i < recipe.getOutputCount(); i++) {
			if (recipe.getOutput(i).isEmpty()) { return; }
		}
		
		for (ItemStack stack1 : recipe.getInput(0).getItems()) {
			for (ItemStack stack2 : recipe.getInput(1).getItems()) {
				for (ItemStack stack3 : recipe.getInput(2).getItems()) {
					ItemComparableList iclist = new ItemComparableList();
					iclist.add(new ItemComparable(stack1));
					iclist.add(new ItemComparable(stack2));
					iclist.add(new ItemComparable(stack3));
					iclist.sort();
					
					lookup.put(iclist, recipe);
				}
			}
		}
		
		for (int i = 0; i < recipe.getInputCount(); i++) {
			RecipeInput item = recipe.getInput(i);
			if (item.isEmpty()) { continue; }
			
			for (ItemStack stack : item.getItems()) {
				ItemComparable ic = new ItemComparable(stack);
				ArrayList<T> recipelist = validInput.get(ic);
				if (recipelist == null) {
					recipelist = new ArrayList<T>();
					recipelist.add(recipe);
					validInput.put(ic, recipelist);
				} else {
					recipelist.add(recipe);
				}
			}
		}
		
		list.add(recipe);
	}
	
	
	/**
	 * Returns NULL if no recipe available
	 * @param stack
	 * @return T
	 */
	@Override
	public T getRecipe(ItemStack[] stack) {
		ItemComparableList iclist = new ItemComparableList(stack);
		
		return lookup.get(iclist);		
	}
	
	/**
	 * Returns a valid recipe only if the items *and* count match,
	 * otherwise returns null.
	 * 
	 * @param stack
	 * @return PurifierRecipe
	 */
	@Override
	public T getRecipeMatch(ItemStack[] stack) {
		T recipe = getRecipe(stack);
		if (recipe == null) { return null; }
		
		for (int i = 0; i < recipe.getInputCount(); i++) {
			RecipeInput input = recipe.getInput(i);
			boolean found = false;
			for (ItemStack item : stack) {
				if (input.isValid(item)) {
					found = true;
					if (!input.isValidWithCount(item)) { return null; }
				}
				if (!found) { return null; }
			}
		}
		return recipe;
	}
	
	@Override
	public int getRecipeQty(T recipe, ItemStack[] stack) {
		int min = -1;
		for (int i = 0; i < recipe.getInputCount(); i++) {
			RecipeInput input = recipe.getInput(i);
			boolean found = false;
			for (ItemStack item : stack) {
				if (input.isValid(item)) {
					int qty = input.getQty(item);
					if (min == -1 || qty < min) {
						min = qty;
					}
				}
				if (!found) { return 0; }
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
		ItemComparable item = new ItemComparable(stack);
		
		ItemComparableList iclist = new ItemComparableList(other);
		if (iclist.size() == 0) {
			return validInput.containsKey(item);
		}
		
		// Inventory is already full
		if (iclist.size() == 3) {
			return false;
		}
		
		for (int i = 0; i < iclist.size(); i++) {
			// Inventory managers need to separately manage whether the current item can stack with existing items.
			// Otherwise, it will put the item in the first empty slot, not stack it.
			// If the current item is already in the input inventory, we just reject it.
			if (item.equals(iclist.get(i))) { return false; }
		}
		
		iclist.add(item);		
		
		ArrayList<T> recipelist = validInput.get(item);
		if (recipelist == null) {
			// Ingredient isn't valid at all
			return false;
		}
		
		recipeloop:
		for (int i = 0; i < recipelist.size(); i++) {
			T recipe = recipelist.get(i);
			
			// Not found tracks the number of recipe ingredients that weren't found
			int notfound = 0;
			// Ingredients tracks the input ingredients to ensure that they are all part of the recipe
			boolean[] ingredient = new boolean[iclist.size()];
			Arrays.fill(ingredient, false);
			
			for (int j = 0; j < recipe.getInputCount(); j++) {
				RecipeInput input = recipe.getInput(j);
				if (input.isEmpty()) { continue; }
				
				boolean found = false;
				
				for (int k = 0; k < iclist.size(); k++) {
					if (iclist.get(k).isEmpty()) { ingredient[k] = true; continue; }
					
					if (input.isValid(iclist.get(k))) { ingredient[k] = true; found = true; break; }
				}
				
				if (!found) { 
					notfound++; 
					if (notfound > (3 - iclist.size())) { continue recipeloop; }
				}
			}
			
			for (int j = 0; j < ingredient.length; j++) {
				if (ingredient[j] == false) { continue recipeloop; }
			}
			
			return true;
		}
		return false;		
	}
	
	@Override
	public List<T> getRecipeList() {
		return list;
	}	
}
