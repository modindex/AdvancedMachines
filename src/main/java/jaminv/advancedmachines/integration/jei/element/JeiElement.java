package jaminv.advancedmachines.integration.jei.element;

import jaminv.advancedmachines.util.recipe.RecipeBase;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class JeiElement {

	int x, y, w, h, u, v;
	IDrawable drawable;
	
	public JeiElement(int x, int y, int w, int h, int u, int v) {
		this.x = x; this.y = y;
		this.w = w; this.h = h;
		this.u = u; this.v = v;
	}
	
	public int getX() { return x; }
	public int getY() { return y; }
	public int getW() { return w; }
	public int getH() { return h; }
	public int getU() { return u; }
	public int getV() { return v; }

	public void init(IGuiHelper guiHelper, ResourceLocation resource) {
		drawable = guiHelper.createDrawable(resource, u, v, w, h);		
	}
	
	public void draw(Minecraft minecraft, int offsetX, int offsetY) {
		drawable.draw(minecraft, x + offsetX, y + offsetY);
	}
	
	public String getTooltip(RecipeBase recipe) {
		return null;
	}
}
