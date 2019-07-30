package jaminv.advancedmachines.util.dialog.control;

import jaminv.advancedmachines.util.dialog.DialogBase;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;

public interface IDialogElement {
	public int getX();
	public int getY();
	public int getW();
	public int getH();
	public void draw(DialogBase gui, FontRenderer font, int drawX, int drawY);
	
	public String getTooltip(int mouseX, int mouseY);		
}
