package jaminv.advancedmachines.objects.blocks.machine.purifier;

import jaminv.advancedmachines.util.Reference;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class GuiMachinePurifier extends GuiContainer {
	public static final int WIDTH = 176;
	public static final int HEIGHT = 193;
	
	private static final int PROCESS_XPOS = 80;
	private static final int PROCESS_YPOS = 37;
	private static final int PROCESS_WIDTH = 16;
	private static final int PROCESS_HEIGHT = 16;
	private static final int PROCESS_ICON_U = 200;
	private static final int PROCESS_ICON_V = 50;	
	
	TileEntityMachinePurifier te;
	
	private static final ResourceLocation background = new ResourceLocation(Reference.MODID, "textures/gui/purifier.png");
	
	public GuiMachinePurifier(TileEntityMachinePurifier tileEntity, ContainerMachinePurifier container) {
		super(container);
		xSize = WIDTH;
		ySize = HEIGHT;
		
		te = tileEntity;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(background);
		drawTexturedModalRect(guiLeft, guiTop, 24, 0, xSize, ySize);
		
		float process = te.getProcessPercent();
		int width = (int)(process * PROCESS_WIDTH);
		this.drawTexturedModalRect(
			this.guiLeft + PROCESS_XPOS,
			this.guiTop + PROCESS_YPOS,
			PROCESS_ICON_U,
			PROCESS_ICON_V,
			width,
			PROCESS_HEIGHT
		);
	}
}
