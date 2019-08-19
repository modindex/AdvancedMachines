package jaminv.advancedmachines.integration.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jaminv.advancedmachines.integration.jei.element.JeiElement;
import jaminv.advancedmachines.lib.container.layout.Layout;
import jaminv.advancedmachines.lib.recipe.IItemGeneric;
import jaminv.advancedmachines.lib.recipe.IJeiRecipe;
import jaminv.advancedmachines.lib.recipe.IRecipe;
import jaminv.advancedmachines.lib.recipe.IRecipe.IInput;
import jaminv.advancedmachines.lib.recipe.IRecipe.IOutput;
import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import scala.actors.threadpool.Arrays;

public class RecipeWrapperBase<T extends IJeiRecipe> implements IRecipeWrapper {

	protected final T recipe;
	protected final DialogMachineBase dialog;
	
	protected List<List<ItemStack>> inputs;
	protected List<ItemStack> outputs;
	protected IJeiRecipe.ISecondaryOutput secondary;
	
	public String getRecipeId() { return recipe.getRecipeId(); }
	
	public RecipeWrapperBase(IGuiHelper guiHelper, T recipe, DialogMachineBase dialog) {
		this.recipe = recipe;
		this.dialog = dialog;
		
		this.inputs = new ArrayList<List<ItemStack>>();
		this.outputs = new ArrayList<ItemStack>();
		
		IInput input = recipe.getInput();
		for (IItemGeneric item : input.getItems()) {
			this.inputs.add(item.getItems());
		}
		// TODO: JEI Fluids
		
		IOutput output = recipe.getOutput();
		for (ItemStack stack : output.getItems()) {
			this.outputs.add(stack);
		}
		
		secondary = recipe.getJeiSecondary();
		for (IJeiRecipe.ISecondary sec : secondary.getItems()) {
			this.outputs.add(sec.toItemStack());
		}
		
		for (JeiElement element : dialog.getJeiElements()) {
			element.init(guiHelper, dialog.getJeiBackground());
		}
	}
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(ItemStack.class, this.inputs);
		ingredients.setOutputs(ItemStack.class, this.outputs);
	}
	
	public int getSecondaryChance(int index) {
		return secondary.getItems().get(index).getChance();
	}
	
	public int getInputCount() { return inputs.size(); }
	public int getOutputCount() { return recipe.getOutput().getItems().size(); }
	public int getSecondaryCount() { return secondary.getItems().size(); }
	
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		for (JeiElement element : dialog.getJeiElements()) {
			element.draw(minecraft, -dialog.getJeiAdjustX(), -dialog.getJeiAdjustY());
		}
		
		Layout layout = dialog.getJeiSecondaryLayout();
		int x = layout.getXPos();
		int y = layout.getYPos();
		int count = 0;
		
		for (IJeiRecipe.ISecondary sec : secondary.getItems()) {
			String percent = I18n.format("dialog.common.percent", sec.getChance());
			int width = minecraft.fontRenderer.getStringWidth(percent);
		
			int tx, ty;
			
			switch (dialog.getDrawSecondaryChancePos()) {
			case ABOVE:
				tx = x + 9 - width/2;
				ty = y - 8;
				break;
			case LEFT:
			default:
				tx = x - (width + 2);
				ty = y + 6;
			}
			
			minecraft.fontRenderer.drawString(percent, tx, ty, 0x808080);
			
			x += layout.getXSpacing();
			count++;
			if (count >= layout.getCols()) {
				x = layout.getXPos();
				y += layout.getYSpacing();
				count = 0;
			}
		}
	}
	
	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		for (JeiElement element : dialog.getJeiElements()) {
			int x = element.getX() - dialog.getJeiAdjustX();
			int y = element.getY() - dialog.getJeiAdjustY();		
			
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
