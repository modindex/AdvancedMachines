package jaminv.advancedmachines.util.dialog.gui;

import jaminv.advancedmachines.util.dialog.struct.DialogTooltip;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.client.config.GuiUtils;
import scala.actors.threadpool.Arrays;

public interface IGuiObserver {
	public void drawBackground(GuiScreen gui, FontRenderer font, int guiLeft, int guiTop, int mouseX, int mouseY);
	public void drawForeground(GuiScreen gui, FontRenderer font, int guiLeft, int guiTop, int mouseX, int mouseY);
	public void mouseClicked(int guiLeft, int guiTop, int mouseX, int mouseY, int mouseButton);
}
