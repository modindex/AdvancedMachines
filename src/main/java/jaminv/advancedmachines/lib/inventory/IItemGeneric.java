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
 * @author Jamin VanderBerg
 */
public interface IItemGeneric {
	public boolean isValid(ItemStack stack);
	public boolean isValid(FluidStack stack);
	public int getCount();
	
	/** Used for error reporting */
	public String toString();
	
	public class ItemStackWrapper implements IItemGeneric {
		protected final ItemStack stack;
		public ItemStackWrapper(ItemStack stack) {
			this.stack = stack;
		}

		@Override
		public boolean isValid(ItemStack stack) {
			return ItemStack.areItemStacksEqual(this.stack, stack);
		}
		
		@Override
		public boolean isValid(FluidStack stack) { return false; }
		
		@Override
		public int getCount() { return stack.getCount(); }

		@Override
		public String toString() {
			return stack.toString();
		}
	}
	
	/** Used to change the count of an IItemGeneric */
	public class ItemGenericWrapper implements IItemGeneric {
		protected final IItemGeneric item;
		protected final int count;
		public ItemGenericWrapper(IItemGeneric item, int count) {
			this.item = item;
			this.count = count;
		}

		@Override
		public boolean isValid(ItemStack stack) { return item.isValid(stack); }
		
		@Override
		public boolean isValid(FluidStack fluid) { return item.isValid(fluid); }
		
		@Override
		public int getCount() { return count; }

		@Override
		public String toString() {
			return item.toString();
		}
	}	
}
