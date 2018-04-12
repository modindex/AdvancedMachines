package jaminv.advancedmachines.objects.blocks.machine.dialog;

import jaminv.advancedmachines.objects.blocks.machine.multiblock.TileEntityMachineMultiblock;
import jaminv.advancedmachines.util.dialog.control.DialogText;

public class DialogMultiblockQuantity extends DialogText {

	TileEntityMachineMultiblock te;
	
	public DialogMultiblockQuantity(TileEntityMachineMultiblock te, int x, int y, int w, int h, int color, HAlign halign, VAlign valign) {
		super(x, y, w, h, "", color, halign, valign);
		this.te = te;
	}
	
	public DialogMultiblockQuantity(TileEntityMachineMultiblock te, int x, int y, int w, int h, int color) {
		super(x, y, w, h, "", color, HAlign.RIGHT, VAlign.BOTTOM);
		this.te = te;
	}
	
	@Override
	public String getText() {
		int qty = te.getQtyProcessing();
		if (qty <= 1) { return ""; }
		return "x" + String.valueOf(qty);
	}

}
