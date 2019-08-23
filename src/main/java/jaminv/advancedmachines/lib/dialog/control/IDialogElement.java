package jaminv.advancedmachines.lib.dialog.control;

import jaminv.advancedmachines.lib.dialog.Dialog;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;

public interface IDialogElement {
	public int getX();
	public int getY();
	public int getW();
	public int getH();
	public void draw(Dialog gui, FontRenderer font, int drawX, int drawY);
	
	public String getTooltip(int mouseX, int mouseY);		
}
