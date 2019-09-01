package jaminv.advancedmachines.lib.container.layout;

import java.util.ArrayList;
import java.util.List;

import jaminv.advancedmachines.lib.container.layout.ItemLayoutGrid.HotbarLayout;
import jaminv.advancedmachines.lib.container.layout.ItemLayoutGrid.InventoryLayout;
import jaminv.advancedmachines.lib.container.layout.impl.InputLayout;
import jaminv.advancedmachines.lib.container.layout.impl.OutputLayout;
import jaminv.advancedmachines.lib.recipe.RecipeManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class LayoutManager implements ILayoutManager {
	private List<IItemLayout> layouts = new ArrayList<IItemLayout>();
	int inventorySlots = 0, playerSlots = 0;
	
	@Override public int getInventorySlots() { return inventorySlots; }
	@Override public int getPlayerSlots() { return playerSlots; }
	
	public LayoutManager addLayout(IItemLayout layout) {
		layouts.add(layout); return this;
	}
	
	public LayoutManager addLayout(int xPos, int yPos) {
		return this.addLayout(new ItemLayoutGrid(xPos, yPos));
	}
	
	public LayoutManager addLayout(int xPos, int yPos, int rows, int cols) {
		return this.addLayout(new ItemLayoutGrid(xPos, yPos, rows, cols));
	}
	
	private IItemLayout inventoryLayout, hotbarLayout;
	public LayoutManager setInventoryLayout(ItemLayoutGrid layout) { this.inventoryLayout = layout; return this; }
	public LayoutManager setHotbarLayout(ItemLayoutGrid layout) { this.hotbarLayout = layout; return this; }
	public LayoutManager setInventoryLayout(int xpos, int ypos) { this.inventoryLayout = new InventoryLayout(xpos, ypos); return this; }
	public LayoutManager setHotbarLayout(int xpos, int ypos) { this.hotbarLayout = new HotbarLayout(xpos, ypos); return this; }
	
	@Override
	public void addInventorySlots(ILayoutUser container, IItemHandler inventory) {
		for (IItemLayout layout : layouts) {
			this.inventorySlots = LayoutHelper.addSlots(container, inventory, layout, 0);
		}
	}
	
	@Override
	public void addPlayerSlots(ILayoutUser container, IInventory playerInventory) {
		playerSlots = LayoutHelper.addPlayerSlots(container, inventoryLayout, hotbarLayout, playerInventory);
	}	
}
