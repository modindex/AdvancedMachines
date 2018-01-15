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
	
	protected static DialogMachinePurifier sDialog = new DialogMachinePurifier();

	public PurifierRecipeCategory(IGuiHelper guiHelper) {
		super(sDialog); 
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
		
		DialogBase.Target jeiTarget = sDialog.getJeiTarget();
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
			recipes.add(new PurifierRecipe(recipe, sDialog));
		}
		
		return recipes;
	}


}
