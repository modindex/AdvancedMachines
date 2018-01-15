package jaminv.advancedmachines.integration.jei;

import jaminv.advancedmachines.integration.jei.purifier.PurifierRecipeCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@JEIPlugin
@SideOnly(Side.CLIENT)
public class JEI implements IModPlugin {
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		PurifierRecipeCategory.register(registry);
	}

	@Override
	public void register(IModRegistry registry) {
		PurifierRecipeCategory.initialize(registry);
	}
	
}
