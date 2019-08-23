package jaminv.advancedmachines.lib.jei.element;

import jaminv.advancedmachines.lib.recipe.IJeiRecipe;
import jaminv.advancedmachines.lib.recipe.IRecipe;
import jaminv.advancedmachines.lib.util.coord.Offset;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class JeiElement implements IJeiElement {

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

	@Override
	public void init(IGuiHelper guiHelper, ResourceLocation resource) {
		drawable = guiHelper.createDrawable(resource, u, v, w, h);		
	}
	
	@Override
	public void draw(Minecraft minecraft, IJeiRecipe recipe, Offset offset) {
		drawable.draw(minecraft, x + offset.getX(), y + offset.getY());
	}
	
	@Override
	public String getTooltip(IRecipe recipe) {
		return null;
	}
}
