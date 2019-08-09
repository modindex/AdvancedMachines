package jaminv.advancedmachines.lib.inventory;

import jaminv.advancedmachines.lib.recipe.ItemComparable;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Used for things like ore dictionaries, wild cards, etc., so that items 
 * can be extracted from an inventory without matching an exact request.
 * Instead, if isValid() returns true for the item, it will be extracted.
 *
 * `RecipeInput` implements this for exactly that purpose.
 * 
 * @author jamin
 */
public interface IItemGeneric {
	public boolean isValid(ItemStack stack);
	
	public class ItemStackWrapper implements IItemGeneric {
		protected final ItemStack stack;
		public ItemStackWrapper(ItemStack stack) {
			this.stack = stack;
		}
		
		public boolean isValid(ItemStack stack) {
			return ItemStack.areItemStacksEqual(this.stack, stack);
		}
	}
}
