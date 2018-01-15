package jaminv.advancedmachines.integration.jei;

import java.util.List;

import jaminv.advancedmachines.integration.jei.purifier.PurifierRecipeCategory.PurifierRecipe;
import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase.ContainerLayout;
import jaminv.advancedmachines.util.Reference;
import jaminv.advancedmachines.util.dialog.DialogBase;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public abstract class RecipeCategoryBase<T extends RecipeWrapperBase> implements IRecipeCategory<T> {
	
	protected final DialogMachineBase dialog;
	
	public RecipeCategoryBase(DialogMachineBase dialog) {
		this.dialog = dialog;
	}

	protected IDrawableStatic background;
	protected IDrawableStatic icon;
	protected String localizedName;
	
	@Override
	public String getTitle() {
		return localizedName;
	}
	
	@Override
	public String getModName() {
		return Reference.NAME;
	}	
	
	@Override
	public IDrawable getBackground() {
		return background;
	}	
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, T recipe, IIngredients ingredients) {

		List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
		List<List<ItemStack>> outputs = ingredients.getOutputs(ItemStack.class);
		
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		this.displayRecipeSection(guiItemStacks, recipe, dialog.getJeiInputLayout(), inputs, Section.INPUT);
		this.displayRecipeSection(guiItemStacks, recipe, dialog.getJeiOutputLayout(), outputs, Section.OUTPUT);
		this.displayRecipeSection(guiItemStacks, recipe, dialog.getJeiSecondaryLayout(), outputs, Section.SECONDARY);
		
		int offset = recipe.getInputCount() + recipe.getOutputCount();
		guiItemStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
			if (slotIndex >= offset) {
				tooltip.add(I18n.format("dialog.common.chance", recipe.getSecondaryChance(slotIndex - offset)));
			}
		});
	}
	
	private enum Section { INPUT, OUTPUT, SECONDARY };	
	protected void displayRecipeSection(IGuiItemStackGroup guiItemStacks, T recipe, ContainerLayout layout, List<List<ItemStack>> items, Section section) {
		int offset = 0, itemOffset = 0, itemCount = 0;
		boolean input = false; 
		
		switch (section) {
			case INPUT: 
				offset = 0; itemOffset = 0; input = true; itemCount = recipe.getInputCount(); break;
			case OUTPUT: 
				offset = recipe.getInputCount(); itemOffset = 0; input = false; 
				itemCount = recipe.getOutputCount(); break;
			case SECONDARY: 
				offset = recipe.getInputCount() + recipe.getOutputCount(); 
				itemOffset = recipe.getOutputCount();
				itemCount = recipe.getSecondaryCount();
				input = false;
		}
		
		for (int i = 0; i < itemCount; i++) {
			DialogBase.Position pos = layout.getPosition(i); 
			guiItemStacks.init(i + offset, input, pos.getXPos(), pos.getYPos());
			guiItemStacks.set(i + offset, items.get(i + itemOffset));
		}
	}	
}
