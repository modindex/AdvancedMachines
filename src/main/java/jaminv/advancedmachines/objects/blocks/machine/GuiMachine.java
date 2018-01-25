package jaminv.advancedmachines.objects.blocks.machine;

import jaminv.advancedmachines.objects.blocks.inventory.GuiInventory;
import jaminv.advancedmachines.util.Reference;
import jaminv.advancedmachines.util.dialog.DialogBase;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class GuiMachine extends GuiInventory {
	
	protected final TileEntityMachineBase te;
	protected final DialogMachineBase machineDialog;
	
	public GuiMachine(ContainerMachine container, DialogMachineBase dialog, TileEntityMachineBase tileEntity) {
		super(container, dialog);
		te = tileEntity;
		machineDialog = dialog;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(dialog.getBackground());
		
		machineDialog.drawBackground(this, guiLeft, guiTop, te);
		machineDialog.drawText(fontRenderer, guiLeft, guiTop);
	}
}
