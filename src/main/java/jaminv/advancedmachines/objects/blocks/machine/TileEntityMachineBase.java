package jaminv.advancedmachines.objects.blocks.machine;

import jaminv.advancedmachines.util.Config;
import jaminv.advancedmachines.util.managers.machine.RecipeInput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public abstract class TileEntityMachineBase extends TileEntity implements ITickable {
	

	private int tick;
	
	@Override
	public void update() {
		tick++;
		if (tick < Config.tickUpdate) { return; }
		
		tick = 0;
		if (canProcess()) {
			process();
		} else {
			haltProcess();
		}
	}
	
	public abstract boolean isProcessing();
	public abstract boolean canProcess();
	protected abstract void process();
	protected abstract void haltProcess();
	
	public boolean canInteractWith(EntityPlayer playerIn) {
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}

}
