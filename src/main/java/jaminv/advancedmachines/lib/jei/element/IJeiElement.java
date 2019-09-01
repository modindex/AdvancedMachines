package jaminv.advancedmachines.lib.jei.element;

import jaminv.advancedmachines.lib.recipe.RecipeJei;
import jaminv.advancedmachines.lib.recipe.Recipe;
import jaminv.advancedmachines.lib.util.coord.Offset;
import mezz.jei.api.IGuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public interface IJeiElement {
	public int getX();
	public int getY();
	public int getW();
	public int getH();
	
	void init(IGuiHelper guiHelper, ResourceLocation resource);
	void draw(Minecraft minecraft, RecipeJei recipe, Offset baseOffset);

	String getTooltip(Recipe recipe);
}