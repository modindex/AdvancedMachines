package jaminv.advancedmachines.lib.dialog.control;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;

public interface IDialogControl extends IDialogElement {
	/**
	 * Mouse Click Event
	 * 
	 * @param relativeX X-coordinate relative to element left
	 * @param relativeY Y-coordinate relative to element top
	 * @param mouseButton
	 * @return Return true to trigger redraw, false otherwise
	 */
	public boolean mouseClicked(int relativeX, int relativeY, int mouseButton);
}