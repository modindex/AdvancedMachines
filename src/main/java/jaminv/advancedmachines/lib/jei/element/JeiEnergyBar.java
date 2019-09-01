package jaminv.advancedmachines.lib.jei.element;

import jaminv.advancedmachines.lib.recipe.Recipe;
import jaminv.advancedmachines.lib.recipe.RecipeImpl;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawableAnimated;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class JeiEnergyBar extends JeiElementAnimated {

	public JeiEnergyBar(int x, int y, int w, int h, int u, int v) {
		super(x, y, w, h, u, v, 60 * 20, IDrawableAnimated.StartDirection.TOP, true);
	}
	
	@Override
	public String getTooltip(Recipe recipe) {
		return I18n.format("dialog.jei.energy", recipe.getEnergy());
	}	
}
