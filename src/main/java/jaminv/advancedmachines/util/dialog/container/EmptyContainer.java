package jaminv.advancedmachines.util.dialog.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EmptyContainer extends Container {
	
	IContainerUpdate te;
	public EmptyContainer(IContainerUpdate te) {
		this.te = te;
	}
	
	protected int[] cachedFields;
	
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		
		boolean sendAll = false;
		if (cachedFields == null) {
			cachedFields = new int[te.getFieldCount()];
			sendAll = true;
		}

        for (int i = 0; i < this.listeners.size(); ++i)
        {
            IContainerListener icontainerlistener = this.listeners.get(i);
            
            for (int f = 0; f < te.getFieldCount(); f++) {
            	if (sendAll || cachedFields[f] != this.te.getField(f)) {
            		icontainerlistener.sendWindowProperty(this, f, this.te.getField(f));
            	}
            }
        }
        
        for (int f = 0; f < this.te.getFieldCount(); f++) {
        	cachedFields[f] = this.te.getField(f);
        }
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int data) {
		te.setField(id, data);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return te.canInteractWith(playerIn);
	}
}
