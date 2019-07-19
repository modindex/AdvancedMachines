package jaminv.advancedmachines.integration.jei.category;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import jaminv.advancedmachines.init.BlockInit;
import jaminv.advancedmachines.integration.jei.RecipeCategoryBase;
import jaminv.advancedmachines.integration.jei.RecipeUids;
import jaminv.advancedmachines.integration.jei.RecipeWrapperBase;
import jaminv.advancedmachines.objects.blocks.machine.instance.alloy.DialogMachineAlloy;
import jaminv.advancedmachines.objects.blocks.machine.instance.alloy.TileEntityMachineAlloy;
import jaminv.advancedmachines.objects.blocks.machine.instance.purifier.DialogMachinePurifier;
import jaminv.advancedmachines.objects.blocks.machine.instance.purifier.TileEntityMachinePurifier;
import jaminv.advancedmachines.util.dialog.struct.DialogArea;
import jaminv.advancedmachines.util.material.MaterialExpansion;
import jaminv.advancedmachines.util.recipe.machine.AlloyManager;
import jaminv.advancedmachines.util.recipe.machine.purifier.PurifierManager;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class AlloyRecipeCategory extends RecipeCategoryBase<AlloyRecipeCategory.AlloyRecipe> {
	
	protected static DialogMachineAlloy sDialog = new DialogMachineAlloy();

	public AlloyRecipeCategory(IGuiHelper guiHelper) {
		super(sDialog); 
		DialogArea texture = dialog.getJeiTexture();
		this.background = guiHelper.createDrawable(dialog.getJeiBackground(),
			texture.getX(), texture.getY(), texture.getW(), texture.getH());
		this.localizedName = I18n.format("dialog.alloy.title"); 
	}

	public static class AlloyRecipe extends RecipeWrapperBase<AlloyManager.AlloyRecipe> {
		public AlloyRecipe(IGuiHelper guiHelper, AlloyManager.AlloyRecipe recipe, DialogMachineAlloy dialog) {
			super(guiHelper, recipe, dialog); 
		}
	}
	
	@Nonnull
	@Override
	public String getUid() {
		return RecipeUids.ALLOY;
	}
	
	public static void register(IRecipeCategoryRegistration registry) {
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		
		registry.addRecipeCategories(new AlloyRecipeCategory(guiHelper));
	}
	
	public static void initialize(IModRegistry registry) {
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		
		registry.addRecipes(getRecipes(guiHelper), RecipeUids.ALLOY);
		
		DialogArea jeiTarget = sDialog.getJeiTarget();
		registry.addRecipeClickArea(TileEntityMachineAlloy.GuiMachineAlloy.class,
			jeiTarget.getX(), jeiTarget.getY(),
			jeiTarget.getW(), jeiTarget.getH(),
			RecipeUids.ALLOY
		);
		
		registry.addRecipeCatalyst(new ItemStack(BlockInit.MACHINE_ALLOY, 1, MaterialExpansion.BASIC.getMeta()), RecipeUids.ALLOY);
		registry.addRecipeCatalyst(new ItemStack(BlockInit.MACHINE_ALLOY, 1, MaterialExpansion.COMPRESSED.getMeta()), RecipeUids.ALLOY);
		registry.addRecipeCatalyst(new ItemStack(BlockInit.MACHINE_ALLOY, 1, MaterialExpansion.QUAD.getMeta()), RecipeUids.ALLOY);
		registry.addRecipeCatalyst(new ItemStack(BlockInit.MACHINE_ALLOY, 1, MaterialExpansion.IMPOSSIBLE.getMeta()), RecipeUids.ALLOY);
	}
	
	public static List<AlloyRecipe> getRecipes(IGuiHelper guiHelper) {
		List<AlloyRecipe> recipes = new ArrayList<>();
		
		for (AlloyManager.AlloyRecipe recipe : AlloyManager.getRecipeList()) {
			recipes.add(new AlloyRecipe(guiHelper, recipe, sDialog));
		}
		
		return recipes;
	}


}
