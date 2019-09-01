package jaminv.advancedmachines.lib.recipe;

import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Used for things like ore dictionaries, wild cards, etc., so that items 
 * can be extracted from an inventory without matching an exact request.
 * Instead, if isValid() returns true for the item, it will be extracted.
 *
 * `RecipeInput` implements this for exactly that purpose.
 * 
 * @author Jamin VanderBerg
 */
public interface Ingredient {
	public boolean isValid(ItemStack stack);
	public boolean isValid(FluidStack stack);
	public int getCount();
	
	/** 
	 * Get list of all possible items.
	 * 
	 * This is principally for JEI integration. I may wish to move this to IJeiRecipe at
	 * some point in the future
	 * @return List<ItemStack> containing all possible items that meet the criteria. Items may have WILDCARD metadata.
	 */
	public List<ItemStack> getItems();
}
