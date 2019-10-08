package jaminv.advancedmachines.lib.recipe;

import java.util.List;

import jaminv.advancedmachines.lib.fluid.FluidTank;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Interface largely used to limit what methods external objects have access to.
 * For example, external objects should not have access to the various add() methods.
 * This interface also abstracts the various internal-use objects, like RecipeInput, RecipeOutput
 * and ItemComparable.
 */
public interface Recipe {
	public interface Input {
		public List<Ingredient> getItems();
		public List<FluidStack> getFluids();
	}
	
	public interface Output {
		public List<ItemStack> getItems();
		public List<FluidStack> getFluids();
	}
	
	public String getRecipeId();
	
	public Input getInput(boolean extractOnly);	
	public Output getOutput();
	
	/**
	 * This method is not deterministic (there is a random component applied).
	 * If you need multiple secondary outputs, you should call this method multiple times.
	 */
	public Output getSecondary(float productivity);
	
	public int getEnergy();
	public int getProcessTime();
	public float getXp();
	
	public int getRecipeQty(ItemStack[] items, FluidStack[] fluids, ItemStack[] inventory, FluidTank[] tanks);
}
