package jaminv.advancedmachines.integration.jei;

import jaminv.advancedmachines.util.Reference;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.util.Translator;

public abstract class RecipeCategoryBase<T extends IRecipeWrapper> implements IRecipeCategory<T> {

	private final IDrawable background;
	private final String localizedName;
	
	public RecipeCategoryBase(IDrawable background, String unlocalizedName) {
		this.background = background;
		this.localizedName = Translator.translateToLocal(unlocalizedName);
	}
	
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
}
