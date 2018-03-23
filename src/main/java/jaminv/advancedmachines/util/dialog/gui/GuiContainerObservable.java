package jaminv.advancedmachines.util.dialog.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jaminv.advancedmachines.objects.items.ItemStackHandlerObservable.IObserver;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public class GuiContainerObservable extends GuiContainer implements IGuiObservable {

	public GuiContainerObservable(Container inventorySlotsIn, int width, int height) {
		super(inventorySlotsIn);
		xSize = width;
		ySize = height;
	}

	private List<IGuiObserver> observers = new ArrayList<>();	
	@Override
	public void addObserver(IGuiObserver obv) {
		this.observers.add(obv);
	}

	@Override
	public void removeObserver(IGuiObserver obv) {
		this.observers.remove(obv);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		for (IGuiObserver obv : this.observers) {
			obv.init(this, this.fontRenderer, guiLeft, guiTop);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		for (IGuiObserver obv : this.observers) {
			obv.drawBackground(this, this.fontRenderer, guiLeft, guiTop, mouseX, mouseY);
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		
		for (IGuiObserver obv : this.observers) {
			obv.drawForeground(this, this.fontRenderer, guiLeft, guiTop, mouseX, mouseY);
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		for (IGuiObserver obv : this.observers) {
			obv.mouseClicked(guiLeft, guiTop, mouseX, mouseY, mouseButton);
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		for (IGuiObserver obv : this.observers) {
			boolean used = obv.keyTyped(typedChar, keyCode);
			if (used) { return; }
		}
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
}
