package jaminv.advancedmachines.lib.machine;

import net.minecraft.util.math.BlockPos;

public interface ICanProcess {
	public void setProcessingState(boolean state);
	public boolean isProcessing();
}
