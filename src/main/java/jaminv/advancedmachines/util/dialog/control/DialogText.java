package jaminv.advancedmachines.util.dialog.control;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class DialogText implements IDialogElement {
	protected final int x, y, w;
	protected final String text;
	protected final int color;
	
	public DialogText(int x, int y, int w, String text, int color) {
		this.x = x; this.y = y;
		this.w = w;
		this.text = text;
		this.color = color;
	}
	
	public DialogText(int xpos, int ypos, String text, int color) {
		this(xpos, ypos, -1, text, color);
	}
	
	public int getX() { return x; }
	public int getY() { return y; }
	public int getW() { return w; }
	public int getH() { return 0; }
	public String getText() { return text; }
	public int getColor() { return color; }
	
	public void draw(GuiScreen gui, FontRenderer font, int drawX, int drawY) {
		int x = drawX;
		String loc = I18n.format(text);
		if (w != -1) {
			int strw = font.getStringWidth(loc);
			x += w / 2 - strw / 2;				
		}
		font.drawString(I18n.format(loc), x, drawY, color);
	}

	@Override
	public String getTooltip(int mouseX, int mouseY) {
		return null;
	}
}
