package jaminv.advancedmachines.lib.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;

import jaminv.advancedmachines.lib.fluid.IFluidTankInternal;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

public abstract class RecipeImpl implements RecipeJei {
	
	protected static class InputImpl implements Input {
		protected List<Ingredient> items = new ArrayList<Ingredient>();
		protected List<FluidStack> fluids = new ArrayList<FluidStack>();
		@Override public List<Ingredient> getItems() { return items; }
		@Override public List<FluidStack> getFluids() { return fluids; }
	}	
	
	protected static class OutputImpl implements Output {
		protected List<ItemStack> items = new ArrayList<ItemStack>();
		protected List<FluidStack> fluids = new ArrayList<FluidStack>();
		@Override public List<ItemStack> getItems() { return items; }
		@Override public List<FluidStack> getFluids() { return fluids; }
	}
	
	protected static class SecondaryOutputImpl implements SecondaryOutput {
		protected List<Secondary> items = new ArrayList<Secondary>();
		protected List<Secondary> fluids = new ArrayList<Secondary>();
		@Override public List<Secondary> getItems() { return items; }
		@Override public List<Secondary> getFluids() { return fluids; }
	}
	
	private String recipeid;
	
	private RecipeInput[] input;
	private RecipeOutput[] output;
	private NonNullList<RecipeOutput> secondary;
	private int energy;
	
	private int processTime;
	
	public RecipeImpl(String id, int energy, int processTime) {
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
	
	protected Map<Boolean, InputImpl> inputcache = new HashMap<>();
	protected OutputImpl outputcache;
	protected SecondaryOutputImpl secondarycache;
	
	@Override
	public Input getInput(boolean extractOnly) {
		InputImpl input;
		if ((input = inputcache.get(extractOnly)) != null) { return input; }
		input = new InputImpl();
		
		for (RecipeInput in : this.input) {
			if (!in.getExtract() && extractOnly) { continue; }
			if (in.isFluid()) {
				input.fluids.add(in.toFluidStack());
			} else {
				input.items.add(in);
			}
		}
		inputcache.put(extractOnly, input);
		return input;
	}
	
	@Override
	public Output getOutput() { 
		if (outputcache != null) { return outputcache; }
		return outputcache = getOutput(this.output, false); 
	}
	
	@Override
	public OutputImpl getSecondary() { return getOutput(this.secondary.toArray(new RecipeOutput[secondary.size()]), true); }
	
	protected OutputImpl getOutput(RecipeOutput[] outputs, boolean doRandom) {
		OutputImpl ret = new OutputImpl();
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
	public SecondaryOutput getJeiSecondary() {
		if (secondarycache != null) { return secondarycache; }
		secondarycache = new SecondaryOutputImpl();
		
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
	
	public RecipeImpl addInput(int index, RecipeInput input) {
		this.input[index] = input;
		return this;
	}
	
	public RecipeImpl setInput(RecipeInput input) {
		return this.addInput(0, input);
	}
	
	public RecipeImpl addOutput(int index, RecipeOutput output) {
		this.output[index] = output;
		return this;
	}
	
	public RecipeImpl setOutput(RecipeOutput output) {
		return this.addOutput(0, output);
	}
	
	public RecipeImpl addSecondary(RecipeOutput output) {
		if (output == null) { return this; }
		this.secondary.add(output);
		return this;
	}

	
	@Override
	public int getEnergy() {
		return energy;
	}
	
	public RecipeImpl setProcessTime(int ticks) {
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
	public RecipeImpl addInput(int index, String oredictName, int count) { return this.addInput(index, new RecipeInput(oredictName, count)); }
	public RecipeImpl addInput(int index, String oredictName) { return this.addInput(index, new RecipeInput(oredictName)); }
	public RecipeImpl addInput(int index, ItemStack stack) { return this.addInput(index, new RecipeInput(stack)); }
	public RecipeImpl addInput(int index, Item item, int count, int meta) { return this.addInput(index, new RecipeInput(item, count, meta)); }
	public RecipeImpl addInput(int index, Item item) { return this.addInput(index, new RecipeInput(item)); }
	public RecipeImpl addBlankInput(int index) { return this.addInput(index, new RecipeInput()); }
	
	public RecipeImpl setInput(String oredictName, int count) { return this.setInput(new RecipeInput(oredictName, count)); }
	public RecipeImpl setInput(String oredictName) { return this.setInput(new RecipeInput(oredictName)); }
	public RecipeImpl setInput(ItemStack stack) { return this.setInput(new RecipeInput(stack)); }
	public RecipeImpl setInput(Item item, int count, int meta) { return this.setInput(new RecipeInput(item, count, meta)); }
	public RecipeImpl setInput(Item item) { return this.setInput(new RecipeInput(item)); }

	public RecipeImpl addOutput(int index, String oredictName, int count) { return this.addOutput(index, new RecipeOutput(oredictName, count)); }
	public RecipeImpl addOutput(int index, String oredictName) { return this.addOutput(index, new RecipeOutput(oredictName)); }
	public RecipeImpl addOutput(int index, ItemStack stack) { return this.addOutput(index, new RecipeOutput(stack)); }
	public RecipeImpl addOutput(int index, Item item, int count, int meta) { return this.addOutput(index, new RecipeOutput(item, count, meta)); }
	public RecipeImpl addOutput(int index, Item item) { return this.addOutput(index, new RecipeOutput(item)); }
	
	public RecipeImpl setOutput(String oredictName, int count) { return this.setOutput(new RecipeOutput(oredictName, count)); }
	public RecipeImpl setOutput(String oredictName) { return this.setOutput(new RecipeOutput(oredictName)); }
	public RecipeImpl setOutput(ItemStack stack) { return this.setOutput(new RecipeOutput(stack)); }
	public RecipeImpl setOutput(Item item, int count, int meta) { return this.setOutput(new RecipeOutput(item, count, meta)); }
	public RecipeImpl setOutput(Item item) { return this.setOutput(new RecipeOutput(item)); }
	
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
