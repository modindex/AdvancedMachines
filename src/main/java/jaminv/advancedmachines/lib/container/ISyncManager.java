package jaminv.advancedmachines.lib.container;

import java.util.List;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ISyncManager {
	void detectAndSendChanges(Container container, List<IContainerListener> listeners);
	void updateProgressBar(int id, int data);
}