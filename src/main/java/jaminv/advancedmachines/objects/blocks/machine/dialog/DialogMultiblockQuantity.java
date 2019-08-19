package jaminv.advancedmachines.objects.blocks.machine.dialog;

import jaminv.advancedmachines.lib.machine.IMachineProcess;
import jaminv.advancedmachines.objects.blocks.machine.TileEntityMachineMultiblock;
import jaminv.advancedmachines.util.dialog.control.DialogText;

public class DialogMultiblockQuantity extends DialogText {

	IMachineProcess process;
	
	public DialogMultiblockQuantity(IMachineProcess process, int x, int y, int w, int h, int color, HAlign halign, VAlign valign) {
		super(x, y, w, h, "", color, halign, valign);
		this.process = process;
	}
	
	public DialogMultiblockQuantity(IMachineProcess process, int x, int y, int w, int h, int color) {
		super(x, y, w, h, "", color, HAlign.RIGHT, VAlign.BOTTOM);
		this.process = process;
	}
	
	@Override
	public String getText() {
		int qty = process.getQtyProcessing();
		if (qty <= 1) { return ""; }
		return "x" + String.valueOf(qty);
	}

}
