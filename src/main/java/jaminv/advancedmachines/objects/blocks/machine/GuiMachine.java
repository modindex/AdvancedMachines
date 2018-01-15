package jaminv.advancedmachines.objects.blocks.machine;

import jaminv.advancedmachines.util.Reference;
import jaminv.advancedmachines.util.dialog.DialogBase;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class GuiMachine extends GuiContainer {
	
	TileEntityMachineBase te;
	ContainerMachine container;
	DialogMachineBase dialog;
	
	public GuiMachine(TileEntityMachineBase tileEntity, ContainerMachine container, DialogMachineBase dialog) {
		super(container);
		xSize = dialog.getWidth();
		ySize = dialog.getHeight();
		
		te = tileEntity;
		this.dialog = dialog;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(dialog.getBackground());
		
		dialog.drawBackground(this, guiLeft, guiTop, te);
		dialog.drawText(fontRenderer, guiLeft, guiTop);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		dialog.drawForeground(this, mouseX, mouseY, guiLeft, guiTop);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

}
