package jaminv.advancedmachines.lib.dialog.control;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;

public interface IDialogControlAdvanced extends IDialogControl {
	public void init(GuiScreen screen, FontRenderer font, int drawX, int drawY);
	public boolean keyTyped(char c, int i);
}
