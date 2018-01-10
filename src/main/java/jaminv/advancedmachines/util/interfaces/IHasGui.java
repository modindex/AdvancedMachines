package jaminv.advancedmachines.util.interfaces;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public interface IHasGui {
	public Class<? extends Container> getContainerClass();
	public Class<? extends GuiContainer> getGuiClass();
}
