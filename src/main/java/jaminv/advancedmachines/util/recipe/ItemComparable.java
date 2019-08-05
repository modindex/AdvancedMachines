package jaminv.advancedmachines.util.recipe;

import java.util.Arrays;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * This was created as a hashable ItemStack wrapper that doesn't care about item counts.
 * So it can be used to search for a recipe based on a set of ingredients.
 * Once found, the recipe manager still has to use the actual ItemStack inputs to determine
 * if the quantities match the recipe.
 *
 * This has since been expanded to including handling fluids and items interchangeably.
 * Since this class is only used for comparison, handing them interchangeably causes no issues.
 * It's up to the recipe to determine whether items or fluids are expected.
 */
public class ItemComparable {
	
	private Item item = Items.AIR;
	private int meta = -1;
	private NBTTagCompound nbt = null;
	private Fluid fluid = null;
	
	private ItemComparable() {}
	public static final ItemComparable EMPTY = new ItemComparable();	
	
	public ItemComparable(ItemStack stack) {
		item = stack.getItem();
		meta = Items.DIAMOND.getDamage(stack);
		nbt = stack.getTagCompound();		
	}
	
	public ItemComparable(FluidStack stack) {
		fluid = stack.getFluid();
		nbt = stack.tag;
	}
	
	public boolean isEmpty() {
		return item == Items.AIR && fluid == null;
	}
	
	public boolean isItem() { return item != Items.AIR; }
	public boolean isFluid() { return fluid != null; }

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ItemComparable)) { return false; }
		ItemComparable comp = (ItemComparable)obj;
		
		return item.equals(comp.item)	 
			&& ((fluid == null && comp.fluid == null) || fluid.equals(comp.fluid)) 
			&& (meta == OreDictionary.WILDCARD_VALUE || comp.meta == OreDictionary.WILDCARD_VALUE || meta == comp.meta)
			&& ((nbt == null && comp.nbt == null) || (nbt != null && comp.nbt != null && nbt.equals(comp.nbt))); 
	}

	@Override
	public int hashCode() {
		if (fluid != null) { return FluidRegistry.getFluidName(fluid).hashCode(); }
		
		// Although it might cause hash collision, we don't hash meta or NBT data. Hash collision should be fairly minimal.
		// We don't hash meta because wildcard meta data wouldn't work.
		// We don't hash NBT because then items with simple NBT data (like renaming in an anvil) would be rejected by the recipe.
		return item.getRegistryName().hashCode();
	}
}
