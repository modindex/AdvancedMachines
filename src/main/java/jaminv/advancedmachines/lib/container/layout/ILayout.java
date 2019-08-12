package jaminv.advancedmachines.lib.container.layout;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * I don't know if this interface is strictly necessary, but
 * it seemed like a good practice. It may prevent me from
 * using inheritance unnecessarily at some point in the future.
 */
public interface ILayout {
	public int getXPos();
	public int getYPos();
	public int getXSpacing();
	public int getYSpacing();
	public int getRows();
	public int getCols();
	
	public SlotItemHandler createSlot(IItemHandler itemHandler, int slotIndex, int x, int y);
}
