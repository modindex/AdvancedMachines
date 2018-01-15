package jaminv.advancedmachines.integration.jei.purifier;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import jaminv.advancedmachines.init.BlockInit;
import jaminv.advancedmachines.integration.jei.RecipeCategoryBase;
import jaminv.advancedmachines.integration.jei.RecipeUids;
import jaminv.advancedmachines.integration.jei.RecipeWrapperBase;
import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase.ContainerLayout;
import jaminv.advancedmachines.objects.blocks.machine.purifier.DialogMachinePurifier;
import jaminv.advancedmachines.objects.blocks.machine.purifier.TileEntityMachinePurifier;
import jaminv.advancedmachines.util.dialog.DialogBase;
import jaminv.advancedmachines.util.recipe.machine.PurifierManager;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class PurifierRecipeCategory extends RecipeCategoryBase<PurifierRecipeCategory.PurifierRecipe> {

	protected static DialogMachinePurifier dialog = new DialogMachinePurifier();
	
	public PurifierRecipeCategory(IGuiHelper guiHelper) {
		DialogBase.Texture texture = dialog.getJeiTexture();
		this.background = guiHelper.createDrawable(dialog.getJeiBackground(),
			texture.getXPos(), texture.getYPos(), texture.getWidth(), texture.getHeight());
		this.localizedName = I18n.format("tile.purifier.name"); 
	}

	public static class PurifierRecipe extends RecipeWrapperBase<PurifierManager.PurifierRecipe> {
		public PurifierRecipe(PurifierManager.PurifierRecipe recipe, DialogMachinePurifier dialog) {
			super(recipe, dialog);
		}
	}
	
	@Nonnull
	@Override
	public String getUid() {
		return RecipeUids.PURIFIER;
	}
	
	public static void register(IRecipeCategoryRegistration registry) {
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		
		registry.addRecipeCategories(new PurifierRecipeCategory(guiHelper));
	}
	
	public static void initialize(IModRegistry registry) {
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		
		registry.addRecipes(getRecipes(guiHelper), RecipeUids.PURIFIER);
		
		DialogBase.Target jeiTarget = dialog.getJeiTarget();
		registry.addRecipeClickArea(TileEntityMachinePurifier.GuiMachinePurifier.class,
			jeiTarget.getXPos(), jeiTarget.getYPos(),
			jeiTarget.getWidth(), jeiTarget.getHeight(),
			RecipeUids.PURIFIER
		);
		
		registry.addRecipeCatalyst(new ItemStack(BlockInit.MACHINE_PURIFIER), RecipeUids.PURIFIER);
	}
	
	public static List<PurifierRecipe> getRecipes(IGuiHelper guiHelper) {
		List<PurifierRecipe> recipes = new ArrayList<>();
		
		for (PurifierManager.PurifierRecipe recipe : PurifierManager.getRecipeList()) {
			recipes.add(new PurifierRecipe(recipe, dialog));
		}
		
		return recipes;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, PurifierRecipe recipe, IIngredients ingredients) {

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
	protected void displayRecipeSection(IGuiItemStackGroup guiItemStacks, PurifierRecipe recipe, ContainerLayout layout, List<List<ItemStack>> items, Section section) {
		int x = layout.getXPos();
		int y = layout.getYPos();
		int count = 0;
		
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
			guiItemStacks.init(i + offset, input, x, y);
			guiItemStacks.set(i + offset, items.get(i + itemOffset));
			
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
