package jaminv.advancedmachines.lib.container.layout;

import java.util.ArrayList;
import java.util.List;

import jaminv.advancedmachines.lib.container.layout.Layout.HotbarLayout;
import jaminv.advancedmachines.lib.container.layout.Layout.InventoryLayout;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class LayoutManager implements ILayoutManager {
	private List<ILayout> layouts = new ArrayList<>();
	
	public LayoutManager addLayout(ILayout layout) {
		layouts.add(layout);
		return this;
	}
	public ILayoutManager addLayout(int xpos, int ypos, int xspacing, int yspacing, int rows, int cols) {
		this.addLayout(new Layout(xpos, ypos, xspacing, yspacing, rows, cols));
		return this;
	}
	public LayoutManager addLayout(int xpos, int ypos, int xspacing, int yspacing) {
		this.addLayout(new Layout(xpos, ypos, xspacing, yspacing));
		return this;
	}
	public LayoutManager addLayout(int xpos, int ypos) {
		this.addLayout(new Layout(xpos, ypos));
		return this;
	}
	public ILayout getLayout(int index) {
		if (index >= this.layouts.size()) { return Layout.EMPTY; }
		return this.layouts.get(index);
	}
	public ILayout[] getLayouts() {
		return layouts.toArray(new Layout[0]);
	}
	
	private ILayout inventory, hotbar;
	public ILayoutManager setInventoryLayout(Layout layout) { this.inventory = layout; return this; }
	public ILayoutManager setHotbarLayout(Layout layout) { this.hotbar = layout; return this; }
	
	public LayoutManager setInventoryLayout(int xpos, int ypos) { this.inventory = new InventoryLayout(xpos, ypos); return this; }
	public LayoutManager setHotbarLayout(int xpos, int ypos) { this.hotbar = new HotbarLayout(xpos, ypos); return this; }
	
	public ILayout getInventoryLayout() { return this.inventory; } 
	public ILayout getHotbarLayout() { return this.hotbar; }	
	
	public static SlotItemHandler createSlot(IItemHandler itemHandler, int slotIndex, int x, int y) {
		return new SlotItemHandler(itemHandler, slotIndex, x, y);
	}
	
	@Override
	public void addInventorySlots(ILayoutUser container, IItemHandler inventory) {
		int slotIndex = 0;
		
		for (ILayout layout : getLayouts()) {
			for (int r = 0; r < layout.getRows(); r++) {
				for (int c = 0; c < layout.getCols(); c++) {
					int x = layout.getXPos() + c * layout.getXSpacing();
					int y = layout.getYPos() + r * layout.getYSpacing();
					container.addSlot(layout.createSlot(inventory, slotIndex, x, y));
					slotIndex++;
				}
			}
		}
	}
	
	@Override
	public void addPlayerSlots(ILayoutUser container, IInventory playerInventory) {
		int slotIndex = 9;
		
		if (inventory != null) {
			for (int r = 0; r < inventory.getRows(); r++) {
				for (int c = 0; c < inventory.getCols(); c++) {
					int x = inventory.getXPos() + c * inventory.getXSpacing();
					int y = inventory.getYPos() + r * inventory.getYSpacing();
					container.addSlot(new Slot(playerInventory, slotIndex, x, y));
					slotIndex++;
				}
			}
		}

		if (hotbar != null) {
			for (int r = 0; r < hotbar.getRows(); r++) {
				for (int c = 0; c < hotbar.getCols(); c++) {
					int x = hotbar.getXPos() + c * hotbar.getXSpacing();
					int y = hotbar.getYPos() + r * hotbar.getYSpacing();
					container.addSlot(new Slot(playerInventory, c, x, y));
				}
			}
		}
	}	
}
