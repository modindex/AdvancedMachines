package jaminv.advancedmachines.lib.container.layout;

import jaminv.advancedmachines.lib.util.coord.Offset;
import jaminv.advancedmachines.lib.util.coord.Pos;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * I don't know if this interface is strictly necessary, but
 * it seemed like a good practice. It may prevent me from
 * using inheritance unnecessarily at some point in the future.
 */
public interface IItemLayout {
	public int getCount();
	
	public Pos getPosition(int index);

	public SlotItemHandler createSlot(IItemHandler itemHandler, int slotIndex, int x, int y);
}
