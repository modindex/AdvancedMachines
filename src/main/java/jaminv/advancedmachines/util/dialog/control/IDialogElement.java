package jaminv.advancedmachines.util.dialog.control;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;

public interface IDialogElement {
	public int getX();
	public int getY();
	public void draw(GuiScreen screen, FontRenderer font, int drawX, int drawY);
}
