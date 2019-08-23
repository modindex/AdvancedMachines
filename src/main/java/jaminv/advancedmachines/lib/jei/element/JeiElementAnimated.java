package jaminv.advancedmachines.lib.jei.element;

import jaminv.advancedmachines.lib.recipe.RecipeBase;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.gui.GuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class JeiElementAnimated extends JeiElement {

	int ticks;
	IDrawableAnimated.StartDirection direction;
	boolean inverted;
	
	public JeiElementAnimated(int x, int y, int w, int h, int u, int v, int ticks, IDrawableAnimated.StartDirection direction, boolean inverted) {
		super(x, y, w, h, u, v);
		this.ticks = ticks;
		this.direction = direction;
		this.inverted = inverted;
	}
	
	@Override
	public void init(IGuiHelper guiHelper, ResourceLocation resource) {
		drawable = guiHelper.createAnimatedDrawable(guiHelper.createDrawable(resource, u, v, w, h), ticks, direction, inverted);
	} 
}
