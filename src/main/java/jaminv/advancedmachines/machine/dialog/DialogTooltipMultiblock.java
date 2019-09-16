package jaminv.advancedmachines.machine.dialog;

import jaminv.advancedmachines.lib.dialog.struct.DialogTooltip;
import jaminv.advancedmachines.machine.TileMachine;

public class DialogTooltipMultiblock extends DialogTooltip {
	
	protected final TileMachine te;

	public DialogTooltipMultiblock(int xpos, int ypos, int width, int height, TileMachine te) {
		super(xpos, ypos, width, height, "");
		this.te = te;
	}
	
	@Override
	public String getText() {
		return te.getMultiblockString();
	}
}