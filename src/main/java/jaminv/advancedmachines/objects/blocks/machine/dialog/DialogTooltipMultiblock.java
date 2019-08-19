package jaminv.advancedmachines.objects.blocks.machine.dialog;

import jaminv.advancedmachines.objects.blocks.machine.TileEntityMachineMultiblock;
import jaminv.advancedmachines.util.dialog.struct.DialogTooltip;

public class DialogTooltipMultiblock extends DialogTooltip {
	
	protected final TileEntityMachineMultiblock te;

	public DialogTooltipMultiblock(int xpos, int ypos, int width, int height, TileEntityMachineMultiblock te) {
		super(xpos, ypos, width, height, "");
		this.te = te;
	}
	
	@Override
	public String getText() {
		return te.getMultiblockString();
	}
}