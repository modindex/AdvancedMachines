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
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RecipeImpl implements RecipeJei, RecipeInternal {
	
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
	
	private List<RecipeInput> input = new ArrayList<>();
	private List<RecipeOutput> output = new ArrayList<>();
	private List<RecipeInput> catalyst = new ArrayList<>();
	private List<RecipeOutput> secondary = new ArrayList<>();
	private int energy;
	private int processTime;
	private float xp = 0.0f;
	
	public RecipeImpl(String id, int energy, int processTime) {
		this.recipeid = id;
		this.energy = energy;
		this.processTime = processTime;
	}
	
	@Override
	public int getInputCount() { return input.size(); }
	@Override
	public int getOutputCount() { return output.size(); }
	@Override
	public int getCatalystCount() { return catalyst.size(); }
	@Override
	public int getSecondaryCount() { return secondary.size(); }
	
	public RecipeInput getInput(int index) { return input.get(index); }
	public RecipeOutput getOutput(int index) { return output.get(index); }
	public RecipeInput getCatalyst(int index) { return catalyst.get(index); }
	
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
		if (!extractOnly) {
			for (RecipeInput cat : this.catalyst) {
				if (cat.isFluid()) {
					input.fluids.add(cat.toFluidStack());
				} else {
					input.items.add(cat);
				}
			}
		}
		inputcache.put(extractOnly, input);
		ItemShulkerBox item;
		BlockShulkerBox block;

		return input;
	}
	
	@Override
	public Output getOutput() { 
		if (outputcache != null) { return outputcache; }
		return outputcache = getOutput(this.output, false); 
	}
	
	@Override
	public OutputImpl getSecondary() { return getOutput(this.secondary, true); }
	
	protected OutputImpl getOutput(List<RecipeOutput> outputs, boolean doRandom) {
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
	
	public RecipeImpl addInput(RecipeInput input) {
		this.input.add(input);
		return this;
	}
	
	public RecipeImpl addOutput(RecipeOutput output) {
		this.output.add(output);
		return this;
	}
	
	public RecipeInternal addSecondary(RecipeOutput output) {
		if (output == null) { return this; }
		this.secondary.add(output);
		return this;
	}
	
	public RecipeInternal addCatalyst(RecipeInput input) {
		this.catalyst.add(input);
		return this;
	}
	
	@Override
	public int getEnergy() {
		return energy;
	}
	
	public RecipeInternal setProcessTime(int ticks) {
		processTime = ticks;
		return this;
	}
	
	@Override
	public int getProcessTime() { return processTime; }
	
	@Override
	public float getXp() { return xp; }
	public void setXp(float xp) { this.xp = xp; }
	
	public int getInputQty(@Nullable ItemStack[] items, @Nullable FluidStack[] fluids) {
		int min = -1;
		for (RecipeInput in : this.input) {
			if (in.isEmpty()) { continue; }
			
			boolean found = false;
			
			if (in.isFluid()) {
				if (fluids == null) { return 0; }
				for (FluidStack fluid : fluids) {
					if (in.isValid(fluid)) {
						found = true;
						if (!in.getExtract()) { continue; }
						int qty = in.getQty(fluid);
						if (min == -1 || qty < min) { min = qty; }
					}
				}
			} else {
				if (items == null) { return 0; }
				for (ItemStack item : items) {
					if (in.isValid(item)) {
						found = true;
						if (!in.getExtract()) { continue; }
						int qty = in.getQty(item);
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
		for (RecipeOutput out : this.output) {
			int count = 0;
			
			if (out.isFluid()) {
				if (tanks == null) { return 0; }
				FluidStack stack = out.toFluidStack();
				do {
					if (tanks[tank].fillInternal(stack, false) == stack.amount) {
						count = tanks[tank].getCapacity() / stack.amount;
					}
					tank++;
				} while (count == 0 && tank < tanks.length);
			} else {
				if (inventory == null) { return 0; }
				ItemStack stack = out.toItemStack();
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
	
	@Override
	public String toString() {
		ToStringHelper helper = MoreObjects.toStringHelper(this);
		helper.add("input", input);
		helper.add("output", output);
		return helper.toString();
	}
}
