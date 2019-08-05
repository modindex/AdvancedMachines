package jaminv.advancedmachines.util.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidTank;

public abstract class RecipeManagerSimple<T extends RecipeBase> implements IRecipeManager<T> {  
	
	private List<T> recipes = new ArrayList<T>();
	private Map<ItemComparable, T> lookup = new HashMap<ItemComparable, T>();
	
	protected void addRecipe(T recipe) {
		if (recipe.getInput(0).isEmpty()) { return; }
		for (int i = 0; i < recipe.getOutputCount(); i++) {
			if (recipe.getOutput(i).isEmpty()) { return; }
		}
		
		int count = 0;
		List<ItemStack> input = recipe.getInput(0).getItems();
		for (ItemStack item : input) {
			ItemComparable comp = new ItemComparable(item);
			if (!lookup.containsKey(comp)) {
				lookup.put(comp, recipe);
				count++;
			}
		}
		
		if (count >= 1) { recipes.add(recipe); }
	}
	
	/**
	 * Returns NULL if no recipe available
	 * @param stack
	 * @return PurifierRecipe
	 */
	public T getRecipe(ItemStack[] stack) {
		if (stack == null || stack.length == 0 || stack[0].isEmpty()) { return null; }
		return lookup.get(new ItemComparable(stack[0]));
	}
	
	/**
	 * Returns a valid recipe only if the items *and* count match,
	 * otherwise returns null.
	 * 
	 * @param stack
	 * @return PurifierRecipe
	 */
	@Override
	public T getRecipeMatch(ItemStack[] input) {
		if (input[0].isEmpty()) { return null; }
		
		T recipe = getRecipe(input);
		if (recipe == null) { return null; }
		
		if (!recipe.getInput(0).isValid(input[0])) { return null; } 
		return recipe;
	}
	
	@Override
	public int getRecipeQty(T recipe, ItemStack[] input) {
		if (input[0].isEmpty()) { return 0; }
		
		return recipe.getInput(0).getQty(input[0]);
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
	public boolean isItemValid(ItemStack stack, ItemStack[] other) {
		if (stack == null || stack.isEmpty()) { return false; }
		return lookup.containsKey(new ItemComparable(stack));
	}
	
	@Override
	public List<T> getRecipeList() {
		return recipes;
	}
}
