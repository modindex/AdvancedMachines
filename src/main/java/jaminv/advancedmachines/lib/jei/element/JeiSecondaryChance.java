package jaminv.advancedmachines.lib.jei.element;

import java.util.List;

import jaminv.advancedmachines.lib.container.layout.IItemLayout;
import jaminv.advancedmachines.lib.container.layout.IJeiLayoutManager;
import jaminv.advancedmachines.lib.recipe.IJeiRecipe;
import jaminv.advancedmachines.lib.recipe.IRecipe;
import jaminv.advancedmachines.lib.util.coord.CoordOffset;
import jaminv.advancedmachines.lib.util.coord.Offset;
import jaminv.advancedmachines.lib.util.coord.Pos;
import mezz.jei.api.IGuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public abstract class JeiSecondaryChance implements IJeiElement {
	protected final IJeiLayoutManager layout; 
	
	public static class Above extends JeiSecondaryChance {
		public Above(IJeiLayoutManager layout) {
			super(layout);
		}

		@Override
		public Offset getChanceOffset(int width) {
			return new CoordOffset(8 - width/2, -9);
		}
	}
	
	public static class Left extends JeiSecondaryChance {
		public Left(IJeiLayoutManager layout) {
			super(layout);
		}

		@Override
		public Offset getChanceOffset(int width) {
			return new CoordOffset(-(width + 2)-1, 5);
		}
	}
	
	public JeiSecondaryChance(IJeiLayoutManager layout) {
		this.layout = layout;
	}

	// This doesn't use a drawable, nor does it have tooltips, so the coordinates are irrelevant.
	@Override public int getX() { return 0; }
	@Override public int getY() { return 0; }
	@Override public int getW() { return 0; }
	@Override public int getH() { return 0; }	

	@Override
	public void init(IGuiHelper guiHelper, ResourceLocation resource) {}
	
	@Override	
	public void draw(Minecraft minecraft, IJeiRecipe recipe, Offset baseOffset) {
		
		int i = 0;
		for (IJeiRecipe.ISecondary sec : recipe.getJeiSecondary().getItems()) {
			String percent = I18n.format("dialog.common.percent", sec.getChance());
			int width = minecraft.fontRenderer.getStringWidth(percent);
		
			Pos pos = layout.getItemSecondaryLayout().getPosition(i).add(getChanceOffset(width).add(baseOffset));
			minecraft.fontRenderer.drawString(percent, pos.getX(), pos.getY(), 0x808080);
			i++;
		}
	}
	
	public abstract Offset getChanceOffset(int width);
	
	@Override public String getTooltip(IRecipe recipe) { return null; }
}
