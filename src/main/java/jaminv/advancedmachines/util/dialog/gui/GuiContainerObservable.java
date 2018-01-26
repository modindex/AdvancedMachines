package jaminv.advancedmachines.util.dialog.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jaminv.advancedmachines.objects.items.ItemStackHandlerObservable.IObserver;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public class GuiContainerObservable extends GuiContainer implements IGuiObservable {

	public GuiContainerObservable(Container inventorySlotsIn) {
		super(inventorySlotsIn);
	}

	private List<IGuiObserver> observers = new ArrayList<>();	
	@Override
	public void addObserver(IGuiObserver obv) {
		this.observers.add(obv);
		obv.init(this, fontRenderer, guiLeft, guiTop);
	}

	@Override
	public void removeObserver(IGuiObserver obv) {
		this.observers.remove(obv);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		for (IGuiObserver obv : this.observers) {
			obv.drawBackground(partialTicks, mouseX, mouseY);
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		
		for (IGuiObserver obv : this.observers) {
			obv.drawForeground(mouseX, mouseY);
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		for (IGuiObserver obv : this.observers) {
			obv.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}

}
