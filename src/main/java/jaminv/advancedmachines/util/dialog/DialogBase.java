package jaminv.advancedmachines.util.dialog;

import jaminv.advancedmachines.util.Reference;
import jaminv.advancedmachines.util.dialog.gui.IGuiObservable;
import jaminv.advancedmachines.util.dialog.gui.IGuiObserver;
import jaminv.advancedmachines.util.dialog.struct.DialogArea;
import jaminv.advancedmachines.util.dialog.struct.DialogText;
import jaminv.advancedmachines.util.dialog.struct.DialogTooltip;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import scala.actors.threadpool.Arrays;

public class DialogBase implements IGuiObserver {
	private final ResourceLocation background;
	private final DialogArea dialog;
	private NonNullList<DialogText> text = NonNullList.<DialogText>create();
	private NonNullList<DialogTooltip> tooltip = NonNullList.<DialogTooltip>create();
	
	public DialogBase(IGuiObservable gui, String background, int xpos, int ypos, int width, int height) {
		this.background = new ResourceLocation(Reference.MODID, background);
		this.dialog = new DialogArea(xpos, ypos, width, height);
		gui.addObserver(this);
	}
	
	protected GuiScreen gui;
	protected FontRenderer font;
	protected int guiLeft, guiTop;
	
	@Override
	public void init(GuiScreen gui, FontRenderer font, int guiLeft, int guiTop) {
		this.gui = gui; this.font = font;
		this.guiLeft = guiLeft; this.guiTop = guiTop;
	}
	
	protected void addText(int xpos, int ypos, int width, String text, int color) {
		this.text.add(new DialogText(xpos, ypos, width, text, color));
	}
	
	protected void addText(int xpos, int ypos, String text, int color) {
		this.text.add(new DialogText(xpos, ypos, text, color));
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
	
	public void drawBackground(float partialTops, int mouseX, int mouseY) {
		gui.drawTexturedModalRect(guiLeft, guiTop, dialog.getX(), dialog.getY(), dialog.getW(), dialog.getH());
		this.drawText();
	}
	
	public void drawForeground(int mouseX, int mouseY) {
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
	}	
	
	public void drawText() {
		for (DialogText t : text) {
			t.draw(font, guiLeft, guiTop);
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	}
}
