package jaminv.advancedmachines.util.recipe;

import java.util.Arrays;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public abstract class RecipeBase {
	public abstract int getInputCount();
	public abstract int getOutputCount();
	
	private RecipeInput[] input;
	private RecipeOutput[] output;
	private NonNullList<RecipeOutput> secondary;
	private int energy;
	
	public RecipeBase(int energy) {
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
	
	public RecipeBase addInput(int index, RecipeInput input) {
		this.input[index] = input;
		Arrays.<RecipeInput>sort(input, new RecipeInput.InputCompare());
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
}
