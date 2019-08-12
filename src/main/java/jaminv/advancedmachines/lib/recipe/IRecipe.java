package jaminv.advancedmachines.lib.recipe;

import java.util.List;

import jaminv.advancedmachines.lib.fluid.IFluidTankInternal;
import jaminv.advancedmachines.lib.inventory.IItemGeneric;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Interface largely used to limit what methods external objects have access to.
 * For example, external objects should not have access to the various add() methods.
 * This interface also abstract the various internal-use objects, like RecipeInput, RecipeOutput
 * and ItemComparable.
 */
public interface IRecipe {
	public interface IInput {
		public List<IItemGeneric> getItems();
		public List<FluidStack> getFluids();
	}
	
	public interface IOutput {
		public List<ItemStack> getItems();
		public List<FluidStack> getFluids();
	}	
	
	public IInput getInput();	
	public IOutput getOutput();
	
	/**
	 * This method is not deterministic (there is a random component applied).
	 * If you need multiple secondary outputs, you should call this method multiple times.
	 */
	public IOutput getSecondary();
	
	public int getEnergy();
	public int getProcessTime();
	
	public int getRecipeQty(ItemStack[] items, FluidStack[] fluids, ItemStack[] inventory, IFluidTankInternal[] tanks);
}
