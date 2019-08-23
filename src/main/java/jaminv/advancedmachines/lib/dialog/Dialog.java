package jaminv.advancedmachines.lib.dialog;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import jaminv.advancedmachines.lib.dialog.control.DialogText;
import jaminv.advancedmachines.lib.dialog.control.IDialogControl;
import jaminv.advancedmachines.lib.dialog.control.IDialogControlAdvanced;
import jaminv.advancedmachines.lib.dialog.control.IDialogElement;
import jaminv.advancedmachines.lib.dialog.struct.DialogTooltip;
import jaminv.advancedmachines.lib.util.coord.CoordRect;
import jaminv.advancedmachines.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.Container;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.config.GuiUtils;
import scala.actors.threadpool.Arrays;

public class Dialog extends GuiContainer implements IDialog {
	private final ResourceLocation background;
	private final CoordRect dialog;
	private NonNullList<IDialogElement> elements = NonNullList.<IDialogElement>create();
	private NonNullList<DialogTooltip> tooltip = NonNullList.<DialogTooltip>create();
	private NonNullList<DialogText> text = NonNullList.<DialogText>create();
	
	public Dialog(Container container, String background, int xpos, int ypos, int width, int height) {
		super(container);
		this.background = new ResourceLocation(Reference.MODID, background);
		this.dialog = new CoordRect(xpos, ypos, width, height);
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
	
	@Override
	public ResourceLocation getBackground() {
		return this.background;
	}

	@Override
	public int getW() {
		return dialog.getW();
	}
	
	@Override
	public int getH() {
		return dialog.getH();
	}
	
	@Override
	public CoordRect getDialogArea() {
		return this.dialog;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		for (IDialogElement element : elements) {
			if (element instanceof IDialogControlAdvanced) {
				((IDialogControlAdvanced)element).init(this, this.fontRenderer, guiLeft + element.getX(), guiTop + element.getY());
			}
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(this.background);
		drawTexturedModalRect(guiLeft, guiTop, dialog.getX(), dialog.getY(), dialog.getW(), dialog.getH());
		
		for (IDialogElement element : elements) {
			element.draw(this, this.fontRenderer, guiLeft + element.getX(), guiTop + element.getY());
		}
		
		for (DialogText t : text) {
			t.draw(this, this.fontRenderer, guiLeft + t.getX(), guiTop + t.getY());
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		
		for (DialogTooltip tip : tooltip) {
			if (mouseX >= tip.getX() + guiLeft && mouseX <= tip.getX() + tip.getW() + guiLeft
				&& mouseY >= tip.getY() + guiTop && mouseY <= tip.getY() + tip.getH() + guiTop
			) {
				ScaledResolution scaled = new ScaledResolution(this.mc);
				GuiUtils.drawHoveringText(Arrays.asList(tip.getText().split("\\\\n")), mouseX - guiLeft, mouseY - guiTop, scaled.getScaledWidth() - guiLeft, scaled.getScaledHeight() - guiTop, -1, this.fontRenderer);
				//gui.drawHoveringText(tip.getText(), mouseX - guiLeft, mouseY - guiTop);
			}
		}
		
		for (IDialogElement element : elements) {
			if (mouseX >= element.getX() + guiLeft && mouseX <= element.getX() + element.getW() + guiLeft
					&& mouseY >= element.getY() + guiTop && mouseY <= element.getY() + element.getH() + guiTop
				) {
				String str = element.getTooltip(mouseX, mouseY);
				if (str != null && str != "") {
					ScaledResolution scaled = new ScaledResolution(this.mc);
					GuiUtils.drawHoveringText(Arrays.asList(str.split("\\\\n")), mouseX - guiLeft, mouseY - guiTop, scaled.getScaledWidth() - guiLeft, scaled.getScaledHeight() - guiTop, -1, this.fontRenderer);
				}
			}
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		for (IDialogElement element : elements) {
			if (element instanceof IDialogControl) {
				IDialogControl control = (IDialogControl)element;
				if (mouseX >= control.getX() + guiLeft && mouseX <= control.getX() + control.getW() + guiLeft
					&& mouseY >= control.getY() + guiTop && mouseY <= control.getY() + control.getH() + guiTop) {
					
					control.mouseClicked(mouseX - control.getX() - guiLeft, mouseY - control.getY() - guiTop, mouseButton);
				}
			}
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		
		for (IDialogElement element : elements) {
			if (element instanceof IDialogControlAdvanced) {
				boolean used = ((IDialogControlAdvanced)element).keyTyped(typedChar, keyCode);
				if (used) { return; }
			}
		}
		
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
	
	public void drawFluid(int x, int y, FluidStack fluid, int w, int h) {
		if (fluid == null) { return; }
		
		GL11.glPushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		Minecraft mc = Minecraft.getMinecraft();
		
		mc.renderEngine.bindTexture(new ResourceLocation("textures/atlas/blocks.png"));
		
		TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluid.getFluid().getStill(fluid).toString());
		drawTiledTexture(x, y, sprite, w, h);
		
		mc.getTextureManager().bindTexture(this.background);
		GL11.glPopMatrix();		
	}
	
	public void drawTiledTexture(int x, int y, TextureAtlasSprite sprite, int w, int h) {
		for (int i = 0; i < w; i += 16) {
			for (int j = 0; j < h; j += 16) {
				drawScaledTexture(x + i, y + j, sprite, Math.min(w-i, 16), Math.min(h-j, 16));
			}
		}
	}
	
	public void drawScaledTexture(int x, int y, TextureAtlasSprite sprite, int w, int h) {
		if (sprite == null) { return; }
		
		double minU = sprite.getMinU(), maxU = sprite.getMaxU(), minV = sprite.getMinV(), maxV = sprite.getMaxV();
		
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(x, y+h, this.zLevel).tex(minU, minV + (maxV - minV) * h / 16F).endVertex();
		buffer.pos(x+w, y+h, this.zLevel).tex(minU + (maxU - minU) * w / 16F, minV + (maxV - minV) * h / 16F).endVertex();
		buffer.pos(x+w, y, this.zLevel).tex(minU + (maxU - minU) * w / 16F, minV).endVertex();
		buffer.pos(x, y, this.zLevel).tex(minU, minV).endVertex();
		Tessellator.getInstance().draw();
	}

}
