package jaminv.advancedmachines.util.dialog.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jaminv.advancedmachines.objects.items.ItemStackHandlerObservable.IObserver;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public class GuiScreenObservable extends GuiScreen implements IGuiObservable {

	int xSize, ySize, guiLeft, guiTop;
	
	public GuiScreenObservable(int width, int height) {
		super();
		this.xSize = width;
		this.ySize = height;
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
    public void initGui()
    {
        super.initGui();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
    }	

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		for (IGuiObserver obv : this.observers) {
			obv.mouseClicked(guiLeft, guiTop, mouseX, mouseY, mouseButton);
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		for (IGuiObserver obv : this.observers) {
			obv.drawBackground(this, this.fontRenderer, guiLeft, guiTop, mouseX, mouseY);
		}		
		for (IGuiObserver obv : this.observers) {
			obv.drawForeground(this, this.fontRenderer, guiLeft, guiTop, mouseX, mouseY);
		}
	}
}
