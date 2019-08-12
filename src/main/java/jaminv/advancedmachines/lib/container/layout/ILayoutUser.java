package jaminv.advancedmachines.lib.container.layout;

import net.minecraft.inventory.Slot;

public interface ILayoutUser {
	/**
	 * Grant public access to addSlotToContainer()
	 * 
	 * For some reason, Container.addSlotToContainer() is protected, but the inventorySlot member variables are public.
	 * Regardless, I've decided that it's better to use this interface to allow Container objects to grant public
	 * access to addSlot (at its own discretion), while possibly also extending this behavior.
	 * 
	 * Also, by this extension, a LayoutUser doesn't /have/ to be a Container.
	 * 
	 * I think this is far more reliable in the long-term than rewriting addSlotToContainer() in another class
	 * by accessing public member variables that really should be protected.
	 * 
	 * Most implementing classes should just `return addSlotToContainer(slotIn);`
	 * @param slotIn
	 */
	public Slot addSlot(Slot slotIn);
}
