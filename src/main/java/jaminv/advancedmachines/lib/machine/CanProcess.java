package jaminv.advancedmachines.lib.machine;

import net.minecraft.util.math.BlockPos;

public interface CanProcess {
	public void setProcessingState(boolean state);
	public boolean isProcessing();
}
