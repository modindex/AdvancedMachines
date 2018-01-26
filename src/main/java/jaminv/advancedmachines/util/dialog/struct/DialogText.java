package jaminv.advancedmachines.util.dialog.struct;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;

public class DialogText {
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
	public String getText() { return text; }
	public int getColor() { return color; }
	
	public void draw(FontRenderer render, int guiLeft, int guiTop) {
		int xpos = this.x;
		String loc = I18n.format(text);
		if (w != -1) {
			int strw = render.getStringWidth(loc);
			xpos += w / 2 - strw / 2;				
		}
		render.drawString(I18n.format(loc), xpos + guiLeft, y + guiTop, color);
	}
}
