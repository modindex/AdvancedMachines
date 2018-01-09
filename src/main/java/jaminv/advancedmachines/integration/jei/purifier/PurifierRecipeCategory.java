package jaminv.advancedmachines.integration.jei.purifier;

import javax.annotation.Nonnull;

import jaminv.advancedmachines.integration.jei.RecipeCategoryBase;
import jaminv.advancedmachines.integration.jei.RecipeUids;
import jaminv.advancedmachines.integration.jei.RecipeWrapperBase;
import jaminv.advancedmachines.util.recipe.RecipeBase;
import jaminv.advancedmachines.util.recipe.machine.PurifierManager;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;

public class PurifierRecipeCategory extends RecipeCategoryBase<PurifierRecipeCategory.PurifierRecipe> {

	public PurifierRecipeCategory(IDrawable background, String unlocalizedName) {
		super(background, unlocalizedName);
		// TODO Auto-generated constructor stub
	}

	public static class PurifierRecipe extends RecipeWrapperBase<PurifierManager.PurifierRecipe> {
		public PurifierRecipe(PurifierManager.PurifierRecipe recipe) {
			super(recipe);
		}
	}
	
	@Nonnull
	@Override
	public String getUid() {
		return RecipeUids.PURIFIER;
	}
}
