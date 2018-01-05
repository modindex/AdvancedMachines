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
			new RecipeOutput("dustIron").withChance(20),
			new RecipeOutput("dustGold").withChance(10),
			new RecipeOutput("dustNickel").withChance(5),
			new RecipeOutput("dustSilver").withChance(10) 
		};
		addRecipe(new RecipeInput("ingotCopper", 2), 
			new RecipeOutput(ItemInit.INGOT_PURE, 1, EnumHandler.EnumMaterialPure.COPPER.getMeta(), Config.doIncludePure, "ingotCopperPure"),
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
	
	public static PurifierRecipe getRecipe(ItemStack stack) {
		if (stack.isEmpty()) { return null; }
		RecipeInput item = new RecipeInput(stack);
		return recipes.get(item);
	}
	

}
