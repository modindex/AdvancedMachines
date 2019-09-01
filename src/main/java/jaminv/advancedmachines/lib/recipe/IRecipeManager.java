package jaminv.advancedmachines.lib.recipe;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Recipe Manager Interface Definition
 * 
 * `RecipeManager` is the de facto implementation of this interface, and should work for most purposes.
 * This still exists as a generic interface, however, in case there is something that it can't handle.
 * Auto-crafting machines, for example, can't be handled by `RecipeManager`.
 * 
 * External code should use `IRecipeManger` as the variable type rather than a specific implementation.
 * 
 * @author Jamin VanderBerg
 * @param <T>
 */
public interface IRecipeManager<T extends IRecipe> {
	public IRecipe getRecipe(@Nullable ItemStack[] input, @Nullable FluidStack[] fluids);

	public boolean isItemValid(ItemStack stack, @Nullable ItemStack[] other, @Nullable FluidStack[] fluids);
	public boolean isFluidValid(FluidStack stack, @Nullable ItemStack[] other, @Nullable FluidStack[] fluids);
}
