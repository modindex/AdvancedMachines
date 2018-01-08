package jaminv.advancedmachines.util.managers.machine;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.init.ItemInit;
import jaminv.advancedmachines.util.Config;
import jaminv.advancedmachines.util.handlers.EnumHandler;
import net.minecraft.item.ItemStack;

public class PurifierManager {
	
	private static Map<RecipeInput, PurifierRecipe> recipes = new HashMap<RecipeInput, PurifierRecipe>();
	
	public static class PurifierRecipe {
		final RecipeInput input;
		final RecipeOutput output;
		final RecipeOutput[] secondary;
		final int energy;
		
		public PurifierRecipe(RecipeInput input, RecipeOutput output, RecipeOutput[] secondary, int energy) {
			this.input = input;
			this.output = output;
			this.secondary = secondary;
			this.energy = energy;
		}
		
		public RecipeInput getInput() {
			return input;
		}
		
		public RecipeOutput getOutput() {
			return output;
		}
		
		public RecipeOutput[] getSecondary() {
			return secondary;
		}
		
		public int getEnergy() {
			return energy;
		}
		
		@Override
		public String toString() {
			String ret = "PurifierRecipe(";
			return ret + input + ", " + output + ")";
		}		
	}

	public static void init() {
		RecipeOutput[] secondaryCopper = { 
			new RecipeOutput("ingotIron").withChance(20),
			new RecipeOutput("ingotGold").withChance(10),
			new RecipeOutput("dustNickel").withChance(5),
			new RecipeOutput("ingotSilver").withChance(10) 
		};
		RecipeInput in = new RecipeInput("ingotCopper", 2);
		RecipeOutput out = new RecipeOutput(ItemInit.INGOT_PURE, 1, EnumHandler.EnumMaterialPure.COPPER.getMeta(), Config.doIncludePure, "ingotCopperPure");
		addRecipe(in, out,
			secondaryCopper,
			30000
		);
	}
	
	public static void addRecipe(RecipeInput input, RecipeOutput output, RecipeOutput[] secondary, int energy) {
		if (input.isEmpty() || output.isEmpty()) { return; }
		PurifierRecipe recipe = new PurifierRecipe(input, output, secondary, energy);
		
		Main.logger.log(Level.INFO, recipe.toString());
		
		recipes.put(input, recipe);
	}
	
	/**
	 * Returns NULL if no recipe available
	 * @param stack
	 * @return PurifierRecipe
	 */
	public static PurifierRecipe getRecipe(ItemStack stack) {
		if (stack.isEmpty()) { return null; }
		RecipeInput item = new RecipeInput(stack);
		return recipes.get(item);
	}
	
	public static PurifierRecipe getRecipe(RecipeInput input) {
		if (input == null) { return null; }
		return recipes.get(input);
	}
	
	/**
	 * Returns a valid recipe only if the items *and* count match,
	 * otherwise returns null.
	 * 
	 * @param stack
	 * @return PurifierRecipe
	 */
	public static PurifierRecipe getRecipeMatch(RecipeInput input) {
		if (input.isEmpty()) { return null; }
		PurifierRecipe recipe = recipes.get(input);
		if (recipe == null) { return null; }
		
		if (!input.doesMatch(recipe.getInput())) { return null; }
		return recipe;
	}
}
