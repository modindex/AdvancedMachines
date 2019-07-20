package jaminv.advancedmachines .integration.jei.category;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import jaminv.advancedmachines.init.BlockInit;
import jaminv.advancedmachines.integration.jei.RecipeCategoryBase;
import jaminv.advancedmachines.integration.jei.RecipeUids;
import jaminv.advancedmachines.integration.jei.RecipeWrapperBase;
import jaminv.advancedmachines.objects.blocks.machine.instance.purifier.DialogMachinePurifier;
import jaminv.advancedmachines.objects.blocks.machine.instance.purifier.TileEntityMachinePurifier;
import jaminv.advancedmachines.util.dialog.struct.DialogArea;
import jaminv.advancedmachines.util.material.MaterialExpansion;
import jaminv.advancedmachines.util.recipe.machine.purifier.PurifierManager;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class PurifierRecipeCategory extends RecipeCategoryBase<PurifierRecipeCategory.PurifierRecipe> {
	
	protected static DialogMachinePurifier sDialog = new DialogMachinePurifier();

	public PurifierRecipeCategory(IGuiHelper guiHelper) {
		super(sDialog); 
		DialogArea texture = dialog.getJeiTexture();
		this.background = guiHelper.createDrawable(dialog.getJeiBackground(),
			texture.getX(), texture.getY(), texture.getW(), texture.getH());
		this.localizedName = I18n.format("dialog.purifier.title"); 
	}

	public static class PurifierRecipe extends RecipeWrapperBase<PurifierManager.PurifierRecipe> {
		public PurifierRecipe(IGuiHelper guiHelper, PurifierManager.PurifierRecipe recipe, DialogMachinePurifier dialog) {
			super(guiHelper, recipe, dialog);
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
		
		DialogArea jeiTarget = sDialog.getJeiTarget();
		registry.addRecipeClickArea(TileEntityMachinePurifier.GuiMachinePurifier.class,
			jeiTarget.getX(), jeiTarget.getY(),
			jeiTarget.getW(), jeiTarget.getH(),
			RecipeUids.PURIFIER
		);
		
		registry.addRecipeCatalyst(new ItemStack(BlockInit.MACHINE_PURIFIER, 1, MaterialExpansion.BASIC.getMeta()), RecipeUids.PURIFIER);
		registry.addRecipeCatalyst(new ItemStack(BlockInit.MACHINE_PURIFIER, 1, MaterialExpansion.COMPRESSED.getMeta()), RecipeUids.PURIFIER);
		registry.addRecipeCatalyst(new ItemStack(BlockInit.MACHINE_PURIFIER, 1, MaterialExpansion.QUAD.getMeta()), RecipeUids.PURIFIER);
		registry.addRecipeCatalyst(new ItemStack(BlockInit.MACHINE_PURIFIER, 1, MaterialExpansion.IMPROBABLE.getMeta()), RecipeUids.PURIFIER);
	}
	
	public static List<PurifierRecipe> getRecipes(IGuiHelper guiHelper) {
		List<PurifierRecipe> recipes = new ArrayList<>();
		
		for (PurifierManager.PurifierRecipe recipe : PurifierManager.getRecipeList()) {
			recipes.add(new PurifierRecipe(guiHelper, recipe, sDialog));
		}
		
		return recipes;
	}


}
