package jaminv.advancedmachines.util.dialog.struct;

import net.minecraft.client.resources.I18n;

public class DialogTooltip extends DialogArea {
	protected final String text;
	
	public DialogTooltip(int x, int y, int w, int h, String text) {
		super(x, y, w, h);
		this.text = text;
	}
	
	public String getText() { return I18n.format(text); }
}
