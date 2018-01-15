package jaminv.advancedmachines.util.dialog;

import jaminv.advancedmachines.objects.blocks.machine.TileEntityMachineBase;
import jaminv.advancedmachines.util.Reference;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class DialogBase {
	
	public static class Position {
		protected final int xpos, ypos;
		public Position(int xpos, int ypos) {
			this.xpos = xpos; this.ypos = ypos;
		}
		public int getXPos() { return xpos; }
		public int getYPos() { return ypos; }
	}
	
	public static class Texture {
		protected final int xpos, ypos, width, height, u, v;
		
		public Texture(int xpos, int ypos, int width, int height, int u, int v) {
			this.xpos = xpos; this.ypos = ypos;
			this.width = width; this.height = height;
			this.u = u; this.v = v;
		}
		
		public Texture(int xpos, int ypos, int width, int height) {
			this(xpos, ypos, width, height, -1, -1);
		}
		
		public int getXPos() { return xpos; }
		public int getYPos() { return ypos; }
		public int getWidth() { return width; }
		public int getHeight() { return height; }
		public int getU() { return u; }
		public int getV() { return v; }
	}

	public class Text {
		protected final int xpos, ypos, width;
		protected final String text;
		protected final int color;
		
		public Text(int xpos, int ypos, int width, String text, int color) {
			this.xpos = xpos; this.ypos = ypos;
			this.width = width;
			this.text = text;
			this.color = color;
		}
		
		public Text(int xpos, int ypos, String text, int color) {
			this(xpos, ypos, -1, text, color);
		}
		
		public int getXPos() { return xpos; }
		public int getYPos() { return ypos; }
		public int getWidth() { return width; }
		public String getText() { return text; }
		public int getColor() { return color; }
		
		public void draw(FontRenderer render, int guiLeft, int guiTop) {
			int x = xpos;
			String loc = I18n.format(text);
			if (width != -1) {
				int strw = render.getStringWidth(loc);
				x += width / 2 - strw / 2;				
			}
			render.drawString(I18n.format(loc), x + guiLeft, ypos + guiTop, color);
		}
	}
	
	public static class Target {
		protected final int xpos, ypos, width, height;
		
		public Target(int xpos, int ypos, int width, int height) {
			this.xpos = xpos; this.ypos = ypos;
			this.width = width; this.height = height;
		}
		
		public int getXPos() { return xpos; }
		public int getYPos() { return ypos; }
		public int getWidth() { return width; }
		public int getHeight() { return height; }
	}
	
	public class Tooltip extends Target {
		protected final String text;
		
		public Tooltip(int xpos, int ypos, int width, int height, String text) {
			super(xpos, ypos, width, height);
			this.text = text;
		}
		
		public String getText() { return I18n.format(text); }
	}
	
	private final ResourceLocation background;
	private final Texture dialog;
	private NonNullList<Text> text = NonNullList.<Text>create();
	private NonNullList<Tooltip> tooltip = NonNullList.<Tooltip>create();
	
	public DialogBase(String background, int xpos, int ypos, int width, int height) {
		this.background = new ResourceLocation(Reference.MODID, background);
		this.dialog = new Texture(xpos, ypos, width, height);
	}
	
	protected void addText(int xpos, int ypos, int width, String text, int color) {
		this.text.add(new Text(xpos, ypos, width, text, color));
	}
	
	protected void addText(int xpos, int ypos, String text, int color) {
		this.text.add(new Text(xpos, ypos, text, color));
	}
	
	protected void addTooltip(int xpos, int ypos, int width, int height, String text) {
		this.tooltip.add(new Tooltip(xpos, ypos, width, height, text));
	}
	
	protected void addTooltip(Tooltip tip) {
		this.tooltip.add(tip);
	}
	
	public ResourceLocation getBackground() {
		return this.background;
	}

	public int getWidth() {
		return dialog.width;
	}
	
	public int getHeight() {
		return dialog.height;
	}
	
	public Texture getDialogTexture() {
		return this.dialog;
	}
	
	public void drawBackground(GuiScreen gui, int guiLeft, int guiTop) {
		gui.drawTexturedModalRect(guiLeft, guiTop, dialog.getXPos(), dialog.getYPos(), dialog.getWidth(), dialog.getHeight());
	}
	
	public void drawForeground(GuiScreen gui, int mouseX, int mouseY, int guiLeft, int guiTop) {
		for (Tooltip tip : tooltip) {
			if (mouseX >= tip.xpos + guiLeft && mouseX <= tip.xpos + tip.width + guiLeft
				&& mouseY >= tip.ypos + guiTop && mouseY <= tip.ypos + tip.height + guiTop
			) {
				gui.drawHoveringText(tip.getText(), mouseX - guiLeft, mouseY - guiTop);
			}
		}
	}	
	
	public void drawText(FontRenderer render, int guiLeft, int guiTop) {
		for (Text t : text) {
			t.draw(render, guiLeft, guiTop);
		}
	}
}
