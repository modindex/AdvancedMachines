package jaminv.advancedmachines.objects.blocks.inventory;

import java.util.ArrayList;
import java.util.List;

import jaminv.advancedmachines.objects.blocks.inventory.Layout.HotbarLayout;
import jaminv.advancedmachines.objects.blocks.inventory.Layout.InventoryLayout;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerLayout {
	private List<Layout> layouts = new ArrayList<>();
	
	public ContainerLayout addLayout(Layout layout) {
		layouts.add(layout);
		return this;
	}
	public ContainerLayout addLayout(int xpos, int ypos, int xspacing, int yspacing, int rows, int cols) {
		this.addLayout(new Layout(xpos, ypos, xspacing, yspacing, rows, cols));
		return this;
	}
	public ContainerLayout addLayout(int xpos, int ypos, int xspacing, int yspacing) {
		this.addLayout(new Layout(xpos, ypos, xspacing, yspacing));
		return this;
	}
	public ContainerLayout addLayout(int xpos, int ypos) {
		this.addLayout(new Layout(xpos, ypos));
		return this;
	}
	public Layout getLayout(int index) {
		if (index >= this.layouts.size()) { return Layout.EMPTY; }
		return this.layouts.get(index);
	}
	public Layout[] getLayouts() {
		return layouts.toArray(new Layout[0]);
	}
	
	private Layout inventory, hotbar;
	public ContainerLayout setInventoryLayout(Layout layout) { this.inventory = layout; return this; }
	public ContainerLayout setHotbarLayout(Layout layout) { this.hotbar = layout; return this; }
	
	public ContainerLayout setInventoryLayout(int xpos, int ypos) { this.inventory = new InventoryLayout(xpos, ypos); return this; }
	public ContainerLayout setHotbarLayout(int xpos, int ypos) { this.hotbar = new HotbarLayout(xpos, ypos); return this; }
	
	public Layout getInventoryLayout() { return this.inventory; } 
	public Layout getHotbarLayout() { return this.hotbar; }	
	
	public static SlotItemHandler createSlot(IItemHandler itemHandler, int slotIndex, int x, int y) {
		return new SlotItemHandler(itemHandler, slotIndex, x, y);
	}
}
