package jaminv.advancedmachines.lib.container.layout;

import jaminv.advancedmachines.util.dialog.struct.DialogPos;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class Layout implements ILayout {	
	final protected static int SLOT_X_SPACING = 18;
	final protected static int SLOT_Y_SPACING = 18;
	final protected static int BORDER_X_SPACING = 8;
	final protected static int INVENTORY_ROWS = 3;
	final protected static int INVENTORY_COLS = 9;	
	final protected static int HOTBAR_ROWS = 1;
	final protected static int HOTBAR_COLS = 9;
	
	public static class InventoryLayout extends Layout {
		public InventoryLayout(int xpos, int ypos) {
			super(xpos, ypos, SLOT_X_SPACING, SLOT_Y_SPACING, INVENTORY_ROWS, INVENTORY_COLS);
		}
	}
	public static class HotbarLayout extends Layout {
		public HotbarLayout(int xpos, int ypos) {
			super (xpos, ypos, SLOT_X_SPACING, SLOT_Y_SPACING, HOTBAR_ROWS, HOTBAR_COLS);
		}
	}
	protected final int xpos, ypos, xspacing, yspacing, rows, cols;
	
	public static final Layout EMPTY = new Layout();
	
	private Layout() {
		xpos = 0; ypos = 0; xspacing = 0; yspacing = 0; rows = 1; cols = 1;
	}
	public Layout(int xpos, int ypos, int xspacing, int yspacing, int rows, int cols) {
		this.xpos = xpos; this.ypos = ypos;
		this.xspacing = xspacing; this.yspacing = yspacing;
		this.rows = rows; this.cols = cols;
	}
	public Layout(int xpos, int ypos, int rows, int cols) {
		this(xpos, ypos, SLOT_X_SPACING, SLOT_Y_SPACING, rows, cols);
	}
	public Layout(int xpos, int ypos) {
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
