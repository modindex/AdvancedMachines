package jaminv.advancedmachines.util.interfaces;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;

public interface IHasGui {
	public Container createContainer(IInventory inventory);
	public GuiContainer createGui(IInventory inventory);
}
