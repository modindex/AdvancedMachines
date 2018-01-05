package jaminv.advancedmachines.objects.blocks.machine.purifier;

import jaminv.advancedmachines.util.Reference;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class GuiMachinePurifier extends GuiContainer {
	public static final int WIDTH = 176;
	public static final int HEIGHT = 193;
	
	private static final ResourceLocation background = new ResourceLocation(Reference.MODID, "textures/gui/purifier.png");
	
	public GuiMachinePurifier(TileEntityMachinePurifier tileEntity, ContainerMachinePurifier container) {
		super(container);
		xSize = WIDTH;
		ySize = HEIGHT;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(background);
		drawTexturedModalRect(guiLeft, guiTop, 24, 0, xSize, ySize);
	}
}
