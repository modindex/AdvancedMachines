package jaminv.advancedmachines.util.dialog;

import org.lwjgl.opengl.GL11;

import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogEnergyBar;
import jaminv.advancedmachines.util.Reference;
import jaminv.advancedmachines.util.dialog.control.DialogText;
import jaminv.advancedmachines.util.dialog.control.IDialogControl;
import jaminv.advancedmachines.util.dialog.control.IDialogControlAdvanced;
import jaminv.advancedmachines.util.dialog.control.IDialogElement;
import jaminv.advancedmachines.util.dialog.gui.IGuiObservable;
import jaminv.advancedmachines.util.dialog.gui.IGuiObserver;
import jaminv.advancedmachines.util.dialog.struct.DialogArea;
import jaminv.advancedmachines.util.dialog.struct.DialogTooltip;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import scala.actors.threadpool.Arrays;

public class DialogBase implements IGuiObserver {
	private final ResourceLocation background;
	private final DialogArea dialog;
	private NonNullList<IDialogElement> elements = NonNullList.<IDialogElement>create();
	private NonNullList<DialogTooltip> tooltip = NonNullList.<DialogTooltip>create();
	private NonNullList<DialogText> text = NonNullList.<DialogText>create();
	
	public DialogBase(String background, int xpos, int ypos, int width, int height) {
		this.background = new ResourceLocation(Reference.MODID, background);
		this.dialog = new DialogArea(xpos, ypos, width, height);
	}
	
	/**
	 * Add a Dialog Element
	 * 
	 * Dialog is not setup to handle overlapping elements
	 * @param element
	 */
	protected void addElement(IDialogElement element) {
		if (element instanceof DialogText) {
			this.text.add((DialogText)element);
		} else {
			this.elements.add(element);
		}
	}
	
	protected void addText(int xpos, int ypos, int width, String text, int color) {
		this.text.add(new DialogText(xpos, ypos, width, text, color));
	}
	
	protected void addText(int xpos, int ypos, String text, int color) {
		this.text.add(new DialogText(xpos, ypos, text, color));
	}
	
	protected void addText(DialogText text) {
		this.text.add(text);
	}
	
	protected void addTooltip(int xpos, int ypos, int width, int height, String text) {
		this.tooltip.add(new DialogTooltip(xpos, ypos, width, height, text));
	}
	
	protected void addTooltip(DialogTooltip tip) {
		this.tooltip.add(tip);
	}
	
	public ResourceLocation getBackground() {
		return this.background;
	}

	public int getW() {
		return dialog.getW();
	}
	
	public int getH() {
		return dialog.getH();
	}
	
	public DialogArea getDialogArea() {
		return this.dialog;
	}
	
	@Override
	public void init(GuiScreen gui, FontRenderer font, int guiLeft, int guiTop) {
		for (IDialogElement element : elements) {
			if (element instanceof IDialogControlAdvanced) {
				((IDialogControlAdvanced)element).init(gui, font, guiLeft + element.getX(), guiTop + element.getY());
			}
		}
	}
	
	public void drawBackground(GuiScreen gui, FontRenderer font, int guiLeft, int guiTop, int mouseX, int mouseY) {
		gui.mc.getTextureManager().bindTexture(this.background);
		gui.drawTexturedModalRect(guiLeft, guiTop, dialog.getX(), dialog.getY(), dialog.getW(), dialog.getH());
		
		for (IDialogElement element : elements) {
			element.draw(gui, font, guiLeft + element.getX(), guiTop + element.getY());
		}
		
		for (DialogText t : text) {
			t.draw(gui, font, guiLeft + t.getX(), guiTop + t.getY());
		}
	}
	
	public void drawForeground(GuiScreen gui, FontRenderer font, int guiLeft, int guiTop, int mouseX, int mouseY) {
		Minecraft minecraft = gui.mc;

		for (DialogTooltip tip : tooltip) {
			if (mouseX >= tip.getX() + guiLeft && mouseX <= tip.getX() + tip.getW() + guiLeft
				&& mouseY >= tip.getY() + guiTop && mouseY <= tip.getY() + tip.getH() + guiTop
			) {
				ScaledResolution scaled = new ScaledResolution(minecraft);
				GuiUtils.drawHoveringText(Arrays.asList(tip.getText().split("\\\\n")), mouseX - guiLeft, mouseY - guiTop, scaled.getScaledWidth() - guiLeft, scaled.getScaledHeight() - guiTop, -1, font);
				//gui.drawHoveringText(tip.getText(), mouseX - guiLeft, mouseY - guiTop);
			}
		}
		
		for (IDialogElement element : elements) {
			if (mouseX >= element.getX() + guiLeft && mouseX <= element.getX() + element.getW() + guiLeft
					&& mouseY >= element.getY() + guiTop && mouseY <= element.getY() + element.getH() + guiTop
				) {
				String str = element.getTooltip(mouseX, mouseY);
				if (str != null && str != "") {
					ScaledResolution scaled = new ScaledResolution(minecraft);
					GuiUtils.drawHoveringText(Arrays.asList(str.split("\\\\n")), mouseX - guiLeft, mouseY - guiTop, scaled.getScaledWidth() - guiLeft, scaled.getScaledHeight() - guiTop, -1, font);
				}
			}
		}
	}
	
	@Override
	public void mouseClicked(int guiLeft, int guiTop, int mouseX, int mouseY, int mouseButton) {
		for (IDialogElement element : elements) {
			if (element instanceof IDialogControl) {
				IDialogControl control = (IDialogControl)element;
				if (mouseX >= control.getX() + guiLeft && mouseX <= control.getX() + control.getW() + guiLeft
					&& mouseY >= control.getY() + guiTop && mouseY <= control.getY() + control.getH() + guiTop) {
					
					boolean redraw = control.mouseClicked(mouseX - control.getX() - guiLeft, mouseY - control.getY() - guiTop, mouseButton);
					//if (redraw) {
					//	control.draw(gui, font, control.getX() + guiLeft, control.getY() + guiTop);
					//}
				}
			}
		}
	}
	
	@Override
	public boolean keyTyped(char c, int i) {
		for (IDialogElement element : elements) {
			if (element instanceof IDialogControlAdvanced) {
				boolean used = ((IDialogControlAdvanced)element).keyTyped(c, i);
				if (used) { return used; }
			}
		}
		
		return false;
	}
	
	public static void drawTiledTexture(int x, int y, TextureAtlasSprite sprite, int w, int h) {
		for (int i = 0; i < w; i += 16) {
			for (int j = 0; j < h; j += 16) {
				drawScaledTexture(x + i, y + i, sprite, Math.min(w-i, 16), Math.min(h-i, 16));
			}
		}
	}
	
	public static void drawScaledTexture(int x, int y, TextureAtlasSprite sprite, int w, int h) {
		if (sprite == null) { return; }
		
		double minU = sprite.getMinU(), maxU = sprite.getMaxU(), minV = sprite.getMinV(), maxV = sprite.getMaxV();
		
		BufferBuilder buffer = Tesselator.getInstance().getBuffer();
		buffer.begin(GL11.GL_QUADS DefaultVertexFormats.POSITION_TEX);
		buffer.pos(x,  y + h,  )
	}
}
