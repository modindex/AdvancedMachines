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
import net.minecraftforge.fluids.FluidTank;

/**
 * Recipe Manager for recipes with (theoretically) any number of ingredients.
 * Use RecipeManagerSimple for single-ingredient recipes. It will be less resource-intensive.
 * 
 * This allows for up to one fluid ingredient.
 * 
 * @author Jamin VanderBerg
 * @param <T>
 */
public abstract class RecipeManagerThreeIngredient<T extends RecipeBase> implements IRecipeManager<T> {  
	
	private Map<ItemComparableList, T> lookup = new HashMap<ItemComparableList, T>();
	private Map<ItemComparable, ArrayList<T>> validInput = new HashMap<ItemComparable, ArrayList<T>>();
	private ArrayList<T> list = new ArrayList<T>();
	
	protected void addRecipe(T recipe) {
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
					ret.add(iclist.copy().add(item));
				}
			}
		}
		return ret;
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
			if (input.isEmpty()) { continue; }
			boolean found = false;
			for (ItemStack item : stack) {
				if (input.isValid(item)) {
					found = true;
					if (!input.isValidWithCount(item)) { return null; }
				}
			}
			if (!found) { return null; }
		}
		return recipe;
	}
	
	@Override
	public int getRecipeQty(T recipe, ItemStack[] stack) {
		int min = -1;
		for (int i = 0; i < recipe.getInputCount(); i++) {
			RecipeInput input = recipe.getInput(i);
			if (input.isEmpty()) { continue; }
			boolean found = false;
			for (ItemStack item : stack) {
				if (input.isValid(item)) {
					found = true;
					int qty = input.getQty(item);
					if (min == -1 || qty < min) {
						min = qty;
					}
				}
			}
			if (!found) { return 0; }
		}
		return min;
	}
	
	@Override
	public int getOutputQty(T recipe, ItemStack[] output) {
		return recipe.getOutputQty(output);
	}
	
	@Override
	public int getOutputQty(T recipe, FluidTank output) {
		return recipe.getOutputQty(output);
	}


	@Override
	public boolean isItemValid(ItemComparable item, ItemComparableList iclist) {
		if (item == null || item.isEmpty()) { return false; }

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
