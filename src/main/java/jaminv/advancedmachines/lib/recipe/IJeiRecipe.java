package jaminv.advancedmachines.lib.recipe;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface IJeiRecipe extends IRecipe {
	public static interface ISecondary {
		public ItemStack toItemStack();
		public FluidStack toFluidStack();
		public int getChance();		
	}
	
	public static interface ISecondaryOutput {
		public List<ISecondary> getItems();
		public List<ISecondary> getFluids();
	}
	
	public ISecondaryOutput getJeiSecondary();
}
