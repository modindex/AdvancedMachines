package jaminv.advancedmachines.lib.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;

import jaminv.advancedmachines.lib.fluid.IFluidTankInternal;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

public abstract class RecipeBase implements IJeiRecipe {
	
	protected static class Input implements IInput {
		protected List<IItemGeneric> items = new ArrayList<IItemGeneric>();
		protected List<FluidStack> fluids = new ArrayList<FluidStack>();
		@Override public List<IItemGeneric> getItems() { return items; }
		@Override public List<FluidStack> getFluids() { return fluids; }
	}	
	
	protected static class Output implements IOutput {
		protected List<ItemStack> items = new ArrayList<ItemStack>();
		protected List<FluidStack> fluids = new ArrayList<FluidStack>();
		@Override public List<ItemStack> getItems() { return items; }
		@Override public List<FluidStack> getFluids() { return fluids; }
	}
	
	protected static class SecondaryOutput implements ISecondaryOutput {
		protected List<ISecondary> items = new ArrayList<ISecondary>();
		protected List<ISecondary> fluids = new ArrayList<ISecondary>();
		@Override public List<ISecondary> getItems() { return items; }
		@Override public List<ISecondary> getFluids() { return fluids; }
	}
	
	private String recipeid;
	
	private RecipeInput[] input;
	private RecipeOutput[] output;
	private NonNullList<RecipeOutput> secondary;
	private int energy;
	
	private int processTime;
	
	public RecipeBase(String id, int energy, int processTime) {
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
		this.processTime = processTime;
	}
	
	public abstract int getInputCount();
	public abstract int getOutputCount();
	public int getCatalystCount() { return 0; }
	
	protected RecipeInput getInput(int index) { return input[index]; }
	protected RecipeOutput getOutput(int index) { return output[index]; }
	
	protected Input inputcache;
	protected Output outputcache;
	protected SecondaryOutput secondarycache;
	
	@Override
	public Input getInput() {
		if (inputcache != null) { return inputcache; }
		inputcache = new Input();
		
		for (RecipeInput in : this.input) {
			if (!in.getExtract()) { continue; }
			if (in.isFluid()) {
				inputcache.fluids.add(in.toFluidStack());
			} else {
				inputcache.items.add(in);
			}
		}
		return inputcache;
	}
	
	@Override
	public Output getOutput() { 
		if (outputcache != null) { return outputcache; }
		return outputcache = getOutput(this.output, false); 
	}
	
	@Override
	public Output getSecondary() { return getOutput(this.secondary.toArray(new RecipeOutput[secondary.size()]), true); }
	
	protected Output getOutput(RecipeOutput[] outputs, boolean doRandom) {
		Output ret = new Output();
		Random rand = new Random();
		
		for (RecipeOutput out : outputs) {
			if (doRandom && rand.nextInt(100) > out.getChance()) { continue; }
			if (out.isFluid()) {
				ret.fluids.add(out.toFluidStack());
			} else {
				ret.items.add(out.toItemStack());
			}
		}
		return ret;
	}
	
	@Override
	public ISecondaryOutput getJeiSecondary() {
		if (secondarycache != null) { return secondarycache; }
		secondarycache = new SecondaryOutput();
		
		for (RecipeOutput sec : this.secondary) {
			if (sec.isFluid()) {
				secondarycache.fluids.add(sec);
			} else {
				secondarycache.items.add(sec);
			}			
		}
		return secondarycache;
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

	
	@Override
	public int getEnergy() {
		return energy;
	}
	
	public RecipeBase setProcessTime(int ticks) {
		processTime = ticks;
		return this;
	}
	
	@Override
	public int getProcessTime() { return processTime; }
	
	public int getInputQty(@Nullable ItemStack[] items, @Nullable FluidStack[] fluids) {
		int min = -1;
		for (int i = 0; i < this.getInputCount(); i++) {
			RecipeInput input = this.input[i];
			if (input.isEmpty()) { continue; }
			
			boolean found = false;
			
			if (input.isFluid()) {
				if (fluids == null) { return 0; }
				for (FluidStack fluid : fluids) {
					if (input.isValid(fluid)) {
						found = true;
						if (!input.getExtract()) { continue; }
						int qty = input.getQty(fluid);
						if (min == -1 || qty < min) { min = qty; }
					}
				}
			} else {
				if (items == null) { return 0; }
				for (ItemStack item : items) {
					if (input.isValid(item)) {
						found = true;
						if (!input.getExtract()) { continue; }
						int qty = input.getQty(item);
						if (min == -1 || qty < min) { min = qty; }
					}
				}
			}
			if (!found) { return 0; }
		}
		return min;
	}
	
	public int getOutputQty(ItemStack[] inventory, @Nullable IFluidTankInternal[] tanks) {
		int slot = 0, tank = 0;
		int min = -1;
		for (int i = 0; i < this.getOutputCount(); i++) {
			RecipeOutput output = this.output[i];
			int count = 0;
			
			if (output.isFluid()) {
				if (tanks == null) { return 0; }
				FluidStack stack = output.toFluidStack();
				do {
					if (tanks[tank].fillInternal(stack, false) == stack.amount) {
						count = tanks[tank].getCapacity() / stack.amount;
					}
					tank++;
				} while (count == 0 && tank < tanks.length);
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
	
	public int getRecipeQty(ItemStack[] items, FluidStack[] fluids, ItemStack[] inventory, IFluidTankInternal[] tanks) {
		return Math.min(getInputQty(items, fluids), getOutputQty(inventory, tanks));
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
		return secondary.size();
	}
	
	@Override
	public String toString() {
		ToStringHelper helper = MoreObjects.toStringHelper(this);
		helper.add("input", input);
		helper.add("output", output);
		return helper.toString();
	}
}
