package jaminv.advancedmachines.integration.jei;

import jaminv.advancedmachines.util.Reference;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.util.Translator;

public abstract class RecipeCategoryBase<T extends IRecipeWrapper> implements IRecipeCategory<T> {

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
}
