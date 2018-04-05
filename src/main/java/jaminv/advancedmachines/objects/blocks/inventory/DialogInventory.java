package jaminv.advancedmachines.objects.blocks.inventory;

import java.util.ArrayList;
import java.util.List;

import jaminv.advancedmachines.util.dialog.DialogBase;
import jaminv.advancedmachines.util.dialog.struct.DialogPos;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public abstract class DialogInventory extends DialogBase {
	
	public DialogInventory(String background, int xpos, int ypos, int width, int height) {
		super(background, xpos, ypos, width, height);
	}
	
	final protected static int SLOT_X_SPACING = 18;
	final protected static int SLOT_Y_SPACING = 18;
	final protected static int BORDER_X_SPACING = 8;
	final protected static int INVENTORY_ROWS = 3;
	final protected static int INVENTORY_COLS = 9;	
	final protected static int HOTBAR_ROWS = 1;
	final protected static int HOTBAR_COLS = 9;
	
	public static class ContainerLayout {
		protected final int xpos, ypos, xspacing, yspacing, rows, cols;
		
		public static final ContainerLayout EMPTY = new ContainerLayout();
		
		private ContainerLayout() {
			xpos = 0; ypos = 0; xspacing = 0; yspacing = 0; rows = 1; cols = 1;
		}
		public ContainerLayout(int xpos, int ypos, int xspacing, int yspacing, int rows, int cols) {
			this.xpos = xpos; this.ypos = ypos;
			this.xspacing = xspacing; this.yspacing = yspacing;
			this.rows = rows; this.cols = cols;
		}
		public ContainerLayout(int xpos, int ypos, int rows, int cols) {
			this(xpos, ypos, SLOT_X_SPACING, SLOT_Y_SPACING, rows, cols);
		}
		public ContainerLayout(int xpos, int ypos) {
			this(xpos, ypos, SLOT_X_SPACING, SLOT_Y_SPACING, 1, 1);
		}
		public int getXPos() { return xpos; }
		public int getYPos() { return ypos; }
		public int getXSpacing() { return xspacing; }
		public int getYSpacing() { return yspacing; }
		public int getRows() { return rows; }
		public int getCols() { return cols; }
		
		public SlotItemHandler createSlot(IItemHandler itemHandler, int slotIndex, int x, int y) {
			return new SlotItemHandler(itemHandler, slotIndex, x, y);
		}
		
		public DialogPos getPosition(int index) {
			return new DialogPos(xpos + (index % cols) * xspacing, ypos + (index / cols) * yspacing);
		}
	}
	public class InventoryLayout extends ContainerLayout {
		public InventoryLayout(int xpos, int ypos) {
			super(xpos, ypos, SLOT_X_SPACING, SLOT_Y_SPACING, INVENTORY_ROWS, INVENTORY_COLS);
		}
	}
	public class HotbarLayout extends ContainerLayout {
		public HotbarLayout(int xpos, int ypos) {
			super (xpos, ypos, SLOT_X_SPACING, SLOT_Y_SPACING, HOTBAR_ROWS, HOTBAR_COLS);
		}
	}
	
	private List<ContainerLayout> layouts = new ArrayList<>();
	protected void addLayout(ContainerLayout layout) {
		layouts.add(layout);
	}
	protected void addLayout(int xpos, int ypos, int xspacing, int yspacing, int rows, int cols) {
		this.addLayout(new ContainerLayout(xpos, ypos, xspacing, yspacing, rows, cols));
	}
	protected void addLayout(int xpos, int ypos, int xspacing, int yspacing) {
		this.addLayout(new ContainerLayout(xpos, ypos, xspacing, yspacing));
	}
	protected void addLayout(int xpos, int ypos) {
		this.addLayout(new ContainerLayout(xpos, ypos));
	}
	public ContainerLayout getLayout(int index) {
		if (index >= this.layouts.size()) { return ContainerLayout.EMPTY; }
		return this.layouts.get(index);
	}
	public ContainerLayout[] getLayouts() {
		return layouts.toArray(new ContainerLayout[0]);
	}
	
	private ContainerLayout inventory, hotbar;
	protected void setInventoryLayout(ContainerLayout layout) { this.inventory = layout; }
	protected void setHotbarLayout(ContainerLayout layout) { this.hotbar = layout; }
	
	public ContainerLayout getInventoryLayout() { return this.inventory; } 
	public ContainerLayout getHotbarLayout() { return this.hotbar; }	
}
