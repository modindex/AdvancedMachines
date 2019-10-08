package jaminv.advancedmachines.lib.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jaminv.advancedmachines.lib.jei.element.IJeiElement;
import jaminv.advancedmachines.lib.recipe.Ingredient;
import jaminv.advancedmachines.lib.recipe.Recipe.Input;
import jaminv.advancedmachines.lib.recipe.Recipe.Output;
import jaminv.advancedmachines.lib.recipe.RecipeJei;
import jaminv.advancedmachines.lib.util.coord.Offset;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import scala.actors.threadpool.Arrays;

public class JeiRecipeWrapper implements IRecipeWrapper {

	protected final RecipeJei recipe;
	protected final IJeiDialog dialog;
	
	protected List<List<ItemStack>> inputItems;
	protected List<FluidStack> inputFluids;
	protected List<ItemStack> outputItems;
	protected List<FluidStack> outputFluids;
	protected RecipeJei.SecondaryOutput secondary;
	
	public String getRecipeId() { return recipe.getRecipeId(); }
	
	public JeiRecipeWrapper(IGuiHelper guiHelper, RecipeJei recipe, IJeiDialog dialog) {
		this.recipe = recipe;
		this.dialog = dialog;
		
		this.inputItems = new ArrayList<List<ItemStack>>();
		this.inputFluids = new ArrayList<FluidStack>();
		this.outputItems = new ArrayList<ItemStack>();
		this.outputFluids = new ArrayList<FluidStack>();
		
		Input input = recipe.getInput(false);
		for (Ingredient item : input.getItems()) {
			this.inputItems.add(item.getItems());
		}
		inputFluids = input.getFluids();
		
		Output output = recipe.getOutput();
		outputItems.addAll(output.getItems());
		outputFluids.addAll(output.getFluids());
		
		secondary = recipe.getJeiSecondary();
		for (RecipeJei.Secondary sec : secondary.getItems()) {
			this.outputItems.add(sec.toItemStack());
		}
		for (RecipeJei.Secondary fluid : secondary.getFluids()) {
			this.outputFluids.add(fluid.toFluidStack());
		}
		
		for (IJeiElement element : dialog.getJeiElements()) {
			element.init(guiHelper, dialog.getJeiBackground());
		}
	}
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(ItemStack.class, this.inputItems);
		ingredients.setInputs(FluidStack.class, this.inputFluids);
		ingredients.setOutputs(ItemStack.class, this.outputItems);
		ingredients.setOutputs(FluidStack.class, this.outputFluids);
	}
	
	public int getSecondaryChance(int index) {
		if (secondary == null || index >= secondary.getItems().size()) { return 0; }
		return secondary.getItems().get(index).getChance();
	}
	
	public int getInputCount() { return inputItems.size(); }
	public int getOutputCount() { return recipe.getOutput().getItems().size(); }
	public int getSecondaryCount() { return secondary.getItems().size(); }
	
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		for (IJeiElement element : dialog.getJeiElements()) {
			element.draw(minecraft, recipe, dialog.getJeiOffset());
		}
	}
	
	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		Offset off = dialog.getJeiOffset();
		for (IJeiElement element : dialog.getJeiElements()) {
			int x = element.getX() + off.getX();
			int y = element.getY() + off.getY();
			
			if (mouseX >= x && mouseX <= x + element.getW()
				&& mouseY >= y && mouseY <= y + element.getH()
			) {
				String tip = element.getTooltip(recipe);
				if (tip != null) {
					return Arrays.asList(tip.split("\\\\n"));
				}				
			}		
		}
		return Collections.emptyList();
	}	
}
