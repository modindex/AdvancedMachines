package jaminv.advancedmachines.lib.container.layout;

import java.util.List;

import jaminv.advancedmachines.lib.util.coord.Pos;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraftforge.items.IItemHandler;

public class LayoutHelper {

	public static int addSlots(ILayoutUser container, IItemHandler inventory, IItemLayout layout, int slotIndex) {
		if (layout == null) { return slotIndex; }
		for (int i = 0; i < layout.getCount(); i++) {
			Pos pos = layout.getPosition(i);
			container.addSlot(layout.createSlot(inventory, slotIndex + i, pos.getX(), pos.getY()));
		}		
		return slotIndex + layout.getCount();
	}
	
	public static int addPlayerSlots(ILayoutUser container, IItemLayout inventoryLayout, IItemLayout hotbarLayout, IInventory playerInventory) {
		int slotIndex = hotbarLayout.getCount();
		
		if (inventoryLayout != null) {
			for (int i = 0; i < inventoryLayout.getCount(); i++) {
				Pos pos = inventoryLayout.getPosition(i);
				container.addSlot(new Slot(playerInventory, i + slotIndex, pos.getX(), pos.getY()));
			}
		}

		if (hotbarLayout != null) {
			for (int i = 0; i < hotbarLayout.getCount(); i++) {
				Pos pos = hotbarLayout.getPosition(i);
				container.addSlot(new Slot(playerInventory, i, pos.getX(), pos.getY()));
			}
		}		
		return hotbarLayout.getCount() + inventoryLayout.getCount();
	}	
}
