package jaminv.advancedmachines.machine.dialog;

import jaminv.advancedmachines.lib.dialog.struct.DialogTooltip;
import jaminv.advancedmachines.machine.TileMachineMultiblock;

public class DialogTooltipMultiblock extends DialogTooltip {
	
	protected final TileMachineMultiblock te;

	public DialogTooltipMultiblock(int xpos, int ypos, int width, int height, TileMachineMultiblock te) {
		super(xpos, ypos, width, height, "");
		this.te = te;
	}
	
	@Override
	public String getText() {
		return te.getMultiblockString();
	}
}