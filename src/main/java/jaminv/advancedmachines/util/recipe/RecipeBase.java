package jaminv.advancedmachines.util.recipe;

import java.util.Arrays;

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
	
	public int getOutputQty(ItemStack[] stacks) {
		int stack = 0;
		int min = -1;
		for (int i = 0; i < this.getOutputCount(); i++) {
			ItemStack output = this.getOutput(i).toItemStack();
			int count = 0;
			do {
				if (stacks[stack] == null || stacks[stack].isEmpty()) {
					count = output.getMaxStackSize() / output.getCount(); 
				} else if (stacks[stack].getItem().equals(output.getItem())) {
					int left = stacks[stack].getMaxStackSize() - stacks[stack].getCount();
					count = left / output.getCount();
				}
				stack++;
			} while(count == 0 && stack < stacks.length);
			
			if (count == 0) { return 0; }

			if (min == -1 || min > count) { min = count; }
		}
		return min;
	}
	
	/**
	 * In its current state, recipes only handle a single fluid output.
	 * That will likely always remain the case.
	 * If getOutputQty() != 1, this method returns 0.
	 * @param tank FluidTank to fill
	 * @return int
	 */
	public int getOutputQty(FluidTank tank) {
		if (this.getOutputCount() != 1) { return 0; }
		
		FluidStack output = this.getOutput(0).toFluidStack();
		
		// Simulate a single fill to see if it will take
		// This is an easy way to make sure all the fluid type checking occurs.
		if (tank.fill(output, false) != output.amount) { return 0; }
		
		int capacity = tank.getCapacity() - tank.getFluidAmount();
		return capacity / output.amount;
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
