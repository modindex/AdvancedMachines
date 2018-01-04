package jaminv.advancedmachines.objects.blocks.machine.purifier;

import jaminv.advancedmachines.util.Reference;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class GuiMachinePurifier extends GuiContainer {
	public static final int WIDTH = 180;
	public static final int HEIGHT = 152;
	
	private static final ResourceLocation background = new ResourceLocation(Reference.MODID, "textures/gui/mobfarm_basic");
	
	public GuiMachinePurifier(TileEntityMachinePurifier tileEntity, ContainerMachinePurifier container) {
		super(container);
		xSize = WIDTH;
		ySize = HEIGHT;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(background);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
}
