package jaminv.advancedmachines.util.recipe;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import jaminv.advancedmachines.util.ModConfig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public abstract class RecipeBase {
	public abstract int getInputCount();
	public abstract int getOutputCount();
	
	private String recipeid;
	
	private RecipeInput[] input;
	private RecipeOutput[] output;
	private NonNullList<RecipeOutput> secondary;
	private int energy;
	
	private int processTime = ModConfig.general.processTimeBasic;
	
	public RecipeBase(String id, int energy) {
		this.recipeid = id;
		this.input = new RecipeInput[getInputCount()];
		this.output = new RecipeOutput[getOutputCount()];
		this.secondary = NonNullList.<RecipeOutput>create();
		
		for (int i = 0; i < getInputCount(); i++) {
			this.input[i] = RecipeInput.EMPTY;
		}
		for (int i = 0; i < getOutputCount(); i++) {
			this.output[i] = RecipeOutput.EMPTY;
		}
		
		this.energy = energy;
	}
	
	public String getRecipeId() { return recipeid; }
	
	public RecipeBase addInput(int index, RecipeInput input) {
		this.input[index] = input;
		return this;
	}
	
	public RecipeBase setInput(RecipeInput input) {
		return this.addInput(0, input);
	}
	
	public RecipeBase addOutput(int index, RecipeOutput output) {
		this.output[index] = output;
		return this;
	}
	
	public RecipeBase setOutput(RecipeOutput output) {
		return this.addOutput(0, output);
	}
	
	public RecipeBase addSecondary(RecipeOutput output) {
		if (output == null) { return this; }
		this.secondary.add(output);
		return this;
	}
	
	public RecipeInput getInput(int index) {
		return input[index];
	}
	
	public RecipeOutput getOutput(int index) {
		return output[index];
	}
	
	public NonNullList<RecipeOutput> getSecondary() {
		return secondary;
	}
	
	public int getEnergy() {
		return energy;
	}
	
	public RecipeBase setProcessTime(int ticks) {
		processTime = ticks;
		return this;
	}
	
	public int getProcessTime() { return processTime; }
	
	public int getInputQty(@Nullable ItemStack[] items, @Nullable FluidStack[] fluids) {
		int min = -1;
		for (int i = 0; i < this.getInputCount(); i++) {
			RecipeInput input = this.getInput(i);
			if (input.isEmpty()) { continue; }
			
			boolean found = false;
			
			if (input.isFluid()) {
				if (fluids == null) { return 0; }
				for (FluidStack fluid : fluids) {
					if (input.isValid(fluid)) {
						found = true;
						int qty = input.getQty(fluid);
						if (min == -1 || qty < min) { min = qty; }
					}
				}
			} else {
				if (items == null) { return 0; }
				for (ItemStack item : items) {
					if (input.isValid(item)) {
						found = true;
						int qty = input.getQty(item);
						if (min == -1 || qty < min) { min = qty; }
					}
				}
			}
			if (!found) { return 0; }
		}
		return min;
	}
	
	public int getInputQty(ItemStack[] items) { return getInputQty(items, (FluidStack[])null); }
	public int getInputQty(ItemStack[] items, FluidStack fluid) { return getInputQty(items, new FluidStack[]{ fluid }); }
	
	public int getOutputQty(ItemStack[] inventory, List<FluidTank> tanks) {
		int slot = 0, tank = 0;
		int min = -1;
		for (int i = 0; i < this.getOutputCount(); i++) {
			RecipeOutput output = this.getOutput(i);
			int count = 0;
			
			if (output.isFluid()) {
				if (tanks == null || tanks.isEmpty()) { return 0; }
				FluidStack stack = output.toFluidStack();
				do {
					if (tanks.get(tank).fillInternal(stack, false) == stack.amount) {
						count = tanks.get(tank).getCapacity() / stack.amount;
					}
					tank++;
				} while (count == 0 && tank < tanks.size());
			} else {
				if (inventory == null) { return 0; }
				ItemStack stack = output.toItemStack();
				do {
					if (inventory[slot] == null || inventory[slot].isEmpty()) {
						count = stack.getMaxStackSize() / stack.getCount(); 
					} else if (inventory[slot].getItem().equals(stack.getItem())) {
						int left = inventory[slot].getMaxStackSize() - inventory[slot].getCount();
						count = left / stack.getCount();
					}
					slot++;
				} while(count == 0 && slot < inventory.length);
			}
			
			if (count == 0) { return 0; }
			if (min == -1 || min > count) { min = count; }
		}
		return min;
	}
	
	public int getOutputQty(ItemStack[] inventory) { return getOutputQty(inventory, Collections.emptyList()); }
	public int getOutputQty(ItemStack[] inventory, FluidTank tank) { return getOutputQty(inventory, Collections.singletonList(tank)); }
	
	public int getRecipeQty(ItemStack[] items, FluidStack[] fluids, ItemStack[] inventory, List<FluidTank> tanks) {
		return Math.min(getInputQty(items, fluids), getOutputQty(inventory, tanks));
	}
	public int getRecipeQty(ItemStack[] items, ItemStack[] inventory) {
		return Math.min(getInputQty(items), getOutputQty(inventory));
	}
	
	/* Helpful utility methods */
	public RecipeBase addInput(int index, String oredictName, int count) { return this.addInput(index, new RecipeInput(oredictName, count)); }
	public RecipeBase addInput(int index, String oredictName) { return this.addInput(index, new RecipeInput(oredictName)); }
	public RecipeBase addInput(int index, ItemStack stack) { return this.addInput(index, new RecipeInput(stack)); }
	public RecipeBase addInput(int index, Item item, int count, int meta) { return this.addInput(index, new RecipeInput(item, count, meta)); }
	public RecipeBase addInput(int index, Item item) { return this.addInput(index, new RecipeInput(item)); }
	public RecipeBase addBlankInput(int index) { return this.addInput(index, new RecipeInput()); }
	
	public RecipeBase setInput(String oredictName, int count) { return this.setInput(new RecipeInput(oredictName, count)); }
	public RecipeBase setInput(String oredictName) { return this.setInput(new RecipeInput(oredictName)); }
	public RecipeBase setInput(ItemStack stack) { return this.setInput(new RecipeInput(stack)); }
	public RecipeBase setInput(Item item, int count, int meta) { return this.setInput(new RecipeInput(item, count, meta)); }
	public RecipeBase setInput(Item item) { return this.setInput(new RecipeInput(item)); }

	public RecipeBase addOutput(int index, String oredictName, int count) { return this.addOutput(index, new RecipeOutput(oredictName, count)); }
	public RecipeBase addOutput(int index, String oredictName) { return this.addOutput(index, new RecipeOutput(oredictName)); }
	public RecipeBase addOutput(int index, ItemStack stack) { return this.addOutput(index, new RecipeOutput(stack)); }
	public RecipeBase addOutput(int index, Item item, int count, int meta) { return this.addOutput(index, new RecipeOutput(item, count, meta)); }
	public RecipeBase addOutput(int index, Item item) { return this.addOutput(index, new RecipeOutput(item)); }
	
	public RecipeBase setOutput(String oredictName, int count) { return this.setOutput(new RecipeOutput(oredictName, count)); }
	public RecipeBase setOutput(String oredictName) { return this.setOutput(new RecipeOutput(oredictName)); }
	public RecipeBase setOutput(ItemStack stack) { return this.setOutput(new RecipeOutput(stack)); }
	public RecipeBase setOutput(Item item, int count, int meta) { return this.setOutput(new RecipeOutput(item, count, meta)); }
	public RecipeBase setOutput(Item item) { return this.setOutput(new RecipeOutput(item)); }
	public int getSecondaryCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public String toString() {
		String ret = getRecipeId() + " (";
		boolean first = true;
		for (RecipeInput i : input) {
			if (!first) { ret += ", "; }
			first = false;
			
			ret += i.toString();
		}
		ret += " -> ";
		first = true;
		for (RecipeOutput o : output) {
			if (!first) { ret += ", "; }
			first = false;
			
			ret += o.toString();
		}
		
		ret += ")";
		return ret;
	}
}
