package jaminv.advancedmachines.objects.blocks.machine.dialog;

import jaminv.advancedmachines.util.dialog.DialogBase;
import jaminv.advancedmachines.util.dialog.control.DialogToggleButton;
import jaminv.advancedmachines.util.enums.IOState;
import jaminv.advancedmachines.util.interfaces.ISwitchableIO;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;

public class DialogIOToggleButton extends DialogToggleButton<IOState> {
	
	protected final ISwitchableIO te;
	public DialogIOToggleButton(int x, int y, int w, int h, ISwitchableIO te) {
		// This is created before the NBT data for the tile entity is loaded, so the default is largely irrevelent here and is set later.
		super(x, y, w, h, IOState.INPUT);
		this.te = te;
		this.addTexture(IOState.INPUT, 200, 0);
		this.addTexture(IOState.OUTPUT, 200, 9);
	}
	
	@Override
	protected void onStateChanged(IOState newstate) {
		te.setInputState(newstate.getState());
	}
	
	@Override
	public void draw(DialogBase gui, FontRenderer font, int drawX, int drawY) {
		this.state = te.getInputState() ? IOState.INPUT : IOState.OUTPUT;
		super.draw(gui, font, drawX, drawY);		
	}

	@Override
	public String getTooltip(int mouseX, int mouseY) {
		return I18n.format(te.getInputState() ? IOState.INPUT.getName() : IOState.OUTPUT.getName());
	}
}
