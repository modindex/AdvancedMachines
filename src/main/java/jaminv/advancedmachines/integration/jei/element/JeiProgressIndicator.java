package jaminv.advancedmachines.integration.jei.element;

import jaminv.advancedmachines.util.ModConfig;
import mezz.jei.api.gui.IDrawableAnimated;

public class JeiProgressIndicator extends JeiElementAnimated {

	public JeiProgressIndicator(int x, int y, int w, int h, int u, int v) {
		super(x, y, w, h, u, v, ModConfig.general.processTimeBasic, IDrawableAnimated.StartDirection.LEFT, false);
	}
	
	String recipe_id;
}
