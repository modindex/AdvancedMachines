package jaminv.advancedmachines.lib.recipe;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface RecipeJei extends Recipe {
	public static interface Secondary {
		public ItemStack toItemStack();
		public FluidStack toFluidStack();
		public int getChance();		
	}
	
	public static interface SecondaryOutput {
		public List<Secondary> getItems();
		public List<Secondary> getFluids();
	}
	
	public SecondaryOutput getJeiSecondary();
}
