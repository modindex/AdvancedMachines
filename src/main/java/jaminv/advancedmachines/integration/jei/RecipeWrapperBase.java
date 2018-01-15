package jaminv.advancedmachines.integration.jei;

import java.util.ArrayList;
import java.util.List;

import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase;
import jaminv.advancedmachines.util.dialog.DialogBase;
import jaminv.advancedmachines.util.recipe.RecipeBase;
import jaminv.advancedmachines.util.recipe.RecipeInput;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class RecipeWrapperBase<T extends RecipeBase> implements IRecipeWrapper {

	protected final T recipe;
	protected final DialogMachineBase dialog;
	
	protected List<List<ItemStack>> inputs;
	protected List<ItemStack> outputs;	
	
	public RecipeWrapperBase(T recipe, DialogMachineBase dialog) {
		this.recipe = recipe;
		this.dialog = dialog;
		
		this.inputs = new ArrayList<List<ItemStack>>();
		this.outputs = new ArrayList<ItemStack>();
		
		for (int i = 0; i < recipe.getInputCount(); i++) {
			this.inputs.add(recipe.getInput(i).getItems());
		}
		
		for (int i = 0; i < recipe.getOutputCount(); i++) {
			this.outputs.add(recipe.getOutput(i).toItemStack());
		}
		
		for (int i = 0; i < recipe.getSecondary().size(); i++) {
			this.outputs.add(recipe.getSecondary().get(i).toItemStack());
		}
	}
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(ItemStack.class, this.inputs);
		ingredients.setOutputs(ItemStack.class, this.outputs);
	}
	
	public int getInputCount() { return recipe.getInputCount(); }
	public int getOutputCount() { return recipe.getOutputCount(); }
	public int getSecondaryCount() { return recipe.getSecondary().size(); }
	
	public int getSecondaryChance(int index) {
		return recipe.getSecondary().get(index).getChance();
	}
	
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		DialogMachineBase.ContainerLayout layout = dialog.getJeiSecondaryLayout();
		int x = layout.getXPos();
		int y = layout.getYPos();
		int count = 0;
		
		for (int i = 0; i < recipe.getSecondary().size(); i++) {
			String percent = I18n.format("dialog.common.percent", getSecondaryChance(i));
			int width = minecraft.fontRenderer.getStringWidth(percent);
			
			minecraft.fontRenderer.drawString(percent, x + 9 - width/2, y-8, 0x808080);
			
			x += layout.getXSpacing();
			count++;
			if (count >= layout.getCols()) {
				x = layout.getXPos();
				y += layout.getYSpacing();
				count = 0;
			}
		}
	}
}
