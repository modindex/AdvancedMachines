package jaminv.advancedmachines.integration.jei.category;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import jaminv.advancedmachines.init.BlockInit;
import jaminv.advancedmachines.integration.jei.RecipeCategoryBase;
import jaminv.advancedmachines.integration.jei.RecipeUids;
import jaminv.advancedmachines.integration.jei.RecipeWrapperBase;
import jaminv.advancedmachines.objects.blocks.machine.instance.grinder.TileEntityMachineGrinder;
import jaminv.advancedmachines.objects.blocks.machine.instance.purifier.DialogMachinePurifier;
import jaminv.advancedmachines.objects.blocks.machine.instance.purifier.TileEntityMachinePurifier;
import jaminv.advancedmachines.util.dialog.struct.DialogArea;
import jaminv.advancedmachines.util.material.MaterialExpansion;
import jaminv.advancedmachines.util.recipe.machine.GrinderManager;
import jaminv.advancedmachines.util.recipe.machine.PurifierManager;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class GrinderRecipeCategory extends RecipeCategoryBase<GrinderRecipeCategory.GrinderRecipe> {
	
	protected static DialogMachinePurifier sDialog = new DialogMachinePurifier();

	public GrinderRecipeCategory(IGuiHelper guiHelper) {
		super(sDialog); 
		DialogArea texture = dialog.getJeiTexture();
		this.background = guiHelper.createDrawable(dialog.getJeiBackground(),
			texture.getX(), texture.getY(), texture.getW(), texture.getH());
		this.localizedName = I18n.format("dialog.grinder.title"); 
	}

	public static class GrinderRecipe extends RecipeWrapperBase<GrinderManager.GrinderRecipe> {
		public GrinderRecipe(GrinderManager.GrinderRecipe recipe, DialogMachinePurifier dialog) {
			super(recipe, dialog);
		}
	}
	
	@Nonnull
	@Override
	public String getUid() {
		return RecipeUids.GRINDER;
	}
	
	public static void register(IRecipeCategoryRegistration registry) {
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		
		registry.addRecipeCategories(new GrinderRecipeCategory(guiHelper));
	}
	
	public static void initialize(IModRegistry registry) {
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		
		registry.addRecipes(getRecipes(guiHelper), RecipeUids.GRINDER);
		
		DialogArea jeiTarget = sDialog.getJeiTarget();
		registry.addRecipeClickArea(TileEntityMachineGrinder.GuiMachineGrinder.class,
			jeiTarget.getX(), jeiTarget.getY(),
			jeiTarget.getW(), jeiTarget.getH(),
			RecipeUids.GRINDER
		);

		registry.addRecipeCatalyst(new ItemStack(BlockInit.MACHINE_GRINDER, 1, MaterialExpansion.BASIC.getMeta()), RecipeUids.GRINDER);
		registry.addRecipeCatalyst(new ItemStack(BlockInit.MACHINE_GRINDER, 1, MaterialExpansion.COMPRESSED.getMeta()), RecipeUids.GRINDER);
		registry.addRecipeCatalyst(new ItemStack(BlockInit.MACHINE_GRINDER, 1, MaterialExpansion.QUAD.getMeta()), RecipeUids.GRINDER);
		registry.addRecipeCatalyst(new ItemStack(BlockInit.MACHINE_GRINDER, 1, MaterialExpansion.IMPOSSIBLE.getMeta()), RecipeUids.GRINDER);
	}
	
	public static List<GrinderRecipe> getRecipes(IGuiHelper guiHelper) {
		List<GrinderRecipe> recipes = new ArrayList<>();
		
		for (GrinderManager.GrinderRecipe recipe : GrinderManager.getRecipeList()) {
			recipes.add(new GrinderRecipe(recipe, sDialog));
		}
		
		return recipes;
	}


}
