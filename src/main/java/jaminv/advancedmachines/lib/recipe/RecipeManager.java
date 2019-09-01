package jaminv.advancedmachines.lib.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Recipe Manager for recipes with (theoretically) any number of ingredients.
 * 
 * Although this is slightly less efficient for single-ingredient recipes, 
 * it's still quite efficient and having a single recipe manager is easier to maintain.
 * 
 * This recipe manager does not support two ingredients of the same type.
 * It adds far too much complexity and isn't really necessary when you can extract
 * multiple items of the same type from a single slot.
 * 
 * @author Jamin VanderBerg
 * @param <T> Recipe type
 */
public class RecipeManager<T extends RecipeBase> implements IRecipeManager<T> {  
	
	private Map<ItemComparableList, T> lookup = new HashMap<ItemComparableList, T>();
	private Map<ItemComparable, ArrayList<T>> validInput = new HashMap<ItemComparable, ArrayList<T>>();
	private ArrayList<T> list = new ArrayList<T>();
	
	/**
	 * Returns NULL if no recipe available
	 * @return T Recipe if found, NULL otherwise.
	 */
	public T getRecipe(ItemComparableList input) {
		return lookup.get(input.sort());		
	}
	
	public T getRecipe(ItemStack[] input, FluidStack[] fluids) { 
		return getRecipe(new ItemComparableList(input, fluids));
	}
	
	public void addRecipe(T recipe) {
		// Check to make sure the recipe has at least 1 ingredient and that no ingredients have error states.
		if (recipe.getInputCount() < 1) { return; }
		boolean valid = false;
		for (int i = 0; i < recipe.getInputCount(); i++) {
			RecipeInput item = recipe.getInput(i);
			if (item.hasError()) { return; }
			if (!item.isEmpty()) { valid = true; }
		}
		if (!valid) { return; }
		
		// Make sure all the outputs are valid.
		for (int i = 0; i < recipe.getOutputCount(); i++) {
			if (recipe.getOutput(i).isEmpty()) { return; }
		}
		
		addLookup(recipe);
		
		for (int i = 0; i < recipe.getInputCount(); i++) {
			RecipeInput item = recipe.getInput(i);
			if (item.isEmpty()) { continue; }
			
			if (item.isFluid()) {
				addValidInput(new ItemComparable(item.toFluidStack()), recipe);			
			} else {
				for (ItemStack stack : item.getItems()) {
					addValidInput(new ItemComparable(stack), recipe);
				}
			}
		}
		
		list.add(recipe);
	}
	
	protected void addValidInput(ItemComparable item, T recipe) {
		ArrayList<T> recipelist = validInput.get(item);
		if (recipelist == null) {
			recipelist = new ArrayList<T>();
			recipelist.add(recipe);
			validInput.put(item, recipelist);
		} else {
			recipelist.add(recipe);
		}
	}
	
	/**
	 * Recursive function for iterating through all item combinations and adding them to the lookup.
	 * 
	 * Fortunately, this is only called when recipes are added.
	 */
	protected void addLookup(T recipe) {
		List<ItemComparableList> list = addLookup(recipe, 0);
		for(ItemComparableList iclist : list) {
			lookup.put(iclist.sort(), recipe);
		}
	}
	
	protected List<ItemComparableList> addLookup(T recipe, int index) {
		List<ItemComparableList> other = null;
		if (index + 1 < recipe.getInputCount()) {
			other = addLookup(recipe, index+1);
		}
		
		List<ItemComparable> items = new ArrayList<ItemComparable>();
		RecipeInput input = recipe.getInput(index);
		if (input.isFluid()) {
			items.add(new ItemComparable(input.toFluidStack()));
		} else {
			for (ItemStack stack : input.getItems()) {
				items.add(new ItemComparable(stack));
			}
		}

		List<ItemComparableList> ret = new ArrayList<ItemComparableList>();
		for (ItemComparable item : items) {
			if (other == null || other.isEmpty()) {
				ret.add(new ItemComparableList(item));
			} else {
				for (ItemComparableList iclist : other) {
					ret.add(iclist.copy().add(item).sort());
				}
			}
		}
		return ret;
	}
	
	public boolean isItemValid(ItemStack stack, @Nullable ItemStack[] other, @Nullable FluidStack[] fluids) {
		return isItemValid(new ItemComparable(stack), new ItemComparableList(other, fluids));
	}
	public boolean isFluidValid(FluidStack stack, @Nullable ItemStack[] other, @Nullable FluidStack[] fluids) {
		if (stack == null) { return false; }
		return isItemValid(new ItemComparable(stack), new ItemComparableList(other, fluids));
	}	

	public boolean isItemValid(ItemStack stack, @Nullable ItemStack[] other) {
		return isItemValid(new ItemComparable(stack), new ItemComparableList(other));
	}
	
	/**
	 * Checks to see if the ingredient is valid
	 * 
	 * Checks an ingredient and all other input ingredient to see if a recipe (any recipe)
	 * can be built from those items. (Even an incomplete recipe).
	 * 
	 * Used by inventory movement routines to check to see if an item should be able to be moved
	 * into the input grid.
	 * 
	 * Note that the inventory manager is expected to separately check if the item can stack
	 * with another existing item. If this method simply returned 'true' for such instances, then
	 * the inventory manager would simply put that item in the first available slot, not necessarily
	 * in the stackable slot. So this method returns 'false' if `item` exists in `iclist`.
	 * 	
	 * @param item Item to check. Should not already be a part of `iclist`.
	 * @param iclist Other items already in input slots.
	 * @return true if item should be moved. false otherwise.
	 */
	protected boolean isItemValid(ItemComparable item, ItemComparableList iclist) {
		if (item == null || item.isEmpty()) { return false; }

		if (iclist == null || iclist.size() == 0) {
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
					if (notfound > (recipe.getInputCount() - iclist.size())) { continue recipeloop; }
				}
			}
			
			for (int j = 0; j < ingredient.length; j++) {
				if (ingredient[j] == false) { continue recipeloop; }
			}
			
			return true;
		}
		return false;		
	}
	
	public List<T> getRecipeList() {
		return list;
	}	
}
