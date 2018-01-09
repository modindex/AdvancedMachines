package jaminv.advancedmachines.integration.jei;

import jaminv.advancedmachines.util.recipe.RecipeBase;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;

public class RecipeWrapperBase<T extends RecipeBase> implements IRecipeWrapper {

	private final T recipe;
	
	public RecipeWrapperBase(T recipe) {
		this.recipe = recipe;
	}
	
	@Override
	public void getIngredients(IIngredients ingredients) {

	}

}
