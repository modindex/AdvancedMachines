package jaminv.advancedmachines.lib.dialog.control;

import jaminv.advancedmachines.lib.dialog.Dialog;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class DialogText implements IDialogElement {
	protected final int x, y;
	protected int w, h;
	protected final String text;
	protected final int color;
	
	public static enum VAlign { TOP, CENTER, BOTTOM };
	public static enum HAlign { LEFT, CENTER, RIGHT };
	
	protected VAlign valign;
	protected HAlign halign;

	public DialogText(int x, int y, int w, int h, String text, int color, HAlign halign, VAlign valign) {
		this.x = x; this.y = y;
		this.w = w; this.h = h;
		this.text = text;
		this.color = color;
		this.halign = halign;
		this.valign = valign;		
	}
	
	public DialogText(int x, int y, int w, String text, int color, HAlign halign) {
		this(x, y, w, -1, text, color, halign, VAlign.TOP);
	}
	
	public DialogText(int x, int y, int w, String text, int color) {
		this(x, y, w, -1, text, color, HAlign.CENTER, VAlign.TOP);
	}
	
	public DialogText(int xpos, int ypos, String text, int color) {
		this(xpos, ypos, -1, -1, text, color, HAlign.LEFT, VAlign.TOP);
	}
	
	public int getX() { return x; }
	public int getY() { return y; }
	public int getW() { return w; }
	public int getH() { return h; }
	public String getText() { return text; }
	public int getColor() { return color; }
	
	protected int getX(FontRenderer font, String text, int drawX) {
		int strw = font.getStringWidth(text);

		switch (halign) {
		case CENTER:
			return drawX + w / 2 - strw / 2;
		case RIGHT:
			strw = font.getStringWidth(text);
			return drawX + w - strw; 
		case LEFT:
		default:
			if (this.w == -1) { this.w = strw + 2; }
			return drawX;
		}
	}
	
	protected int getY(FontRenderer font, String text, int drawY) {
		int strh = font.getWordWrappedHeight(text, w);
		
		switch (valign) {
		case CENTER:
			return drawY + h / 2 - strh / 2;
		case BOTTOM:
			return drawY + h - strh;
		case TOP:
		default:
			if (this.h == -1) { this.h = strh + 2; }
			return drawY;
		}
	}
	
	public void draw(Dialog gui, FontRenderer font, int drawX, int drawY) {
		String loc = I18n.format(this.getText());
		int xpos = this.getX(font, loc, drawX);
		int ypos = this.getY(font, loc, drawY);
		font.drawString(I18n.format(loc), xpos, ypos, color);
	}

	@Override
	public String getTooltip(int mouseX, int mouseY) {
		return null;
	}
}
