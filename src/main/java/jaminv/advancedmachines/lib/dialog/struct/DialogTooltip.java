package jaminv.advancedmachines.lib.dialog.struct;

import jaminv.advancedmachines.lib.util.coord.CoordRect;
import net.minecraft.client.resources.I18n;

public class DialogTooltip extends CoordRect {
	protected final String text;
	
	public DialogTooltip(int x, int y, int w, int h, String text) {
		super(x, y, w, h);
		this.text = text;
	}
	
	public String getText() { return I18n.format(text); }
}
