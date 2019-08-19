package jaminv.advancedmachines.objects.blocks.machine.dialog;

import jaminv.advancedmachines.util.dialog.DialogBase;
import jaminv.advancedmachines.util.dialog.control.DialogToggleButton;
import jaminv.advancedmachines.util.enums.IOState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;

public class DialogIOToggleButton extends DialogToggleButton<IOState> {
	
	public static interface ISwitchableIO {
		public void setInputState(boolean state);
		public void setPriority(int priority);
		
		public boolean getInputState();
		public int getPriority();
		
		public boolean canInput();
		public boolean canOutput();
		
		public boolean hasController();
	}	
	
	protected final ISwitchableIO te;
	public DialogIOToggleButton(int x, int y, int w, int h, ISwitchableIO te) {
		// This is created before the NBT data for the tile entity is loaded, so the default is largely irrevelent here and is set later.
		super(x, y, w, h, IOState.INPUT);
		this.te = te;
		this.addTexture(IOState.INPUT, 200, 0);
		this.addTexture(IOState.OUTPUT, 200, 9);
		this.setDisabledTexture(209, 0);
	}
	
	@Override
	protected boolean canChangeState(IOState newstate) {
		if (newstate == IOState.INPUT) {
			return te.canInput();
		} else {
			return te.canOutput();
		}
	}

	@Override
	protected void onStateChanged(IOState newstate) {
		te.setInputState(newstate.getState());
	}
	
	@Override
	public void draw(DialogBase gui, FontRenderer font, int drawX, int drawY) {
		if (!te.canInput() && !te.canOutput()) {
			super.drawDisabledTexture(gui, drawX, drawY);
			return;
		}
		this.state = te.getInputState() ? IOState.INPUT : IOState.OUTPUT;
		super.draw(gui, font, drawX, drawY);		
	}

	@Override
	public String getTooltip(int mouseX, int mouseY) {
		if (!te.canInput() && !te.canOutput()) { 
			if (!te.hasController()) { return I18n.format("dialog.io.no_machine"); }
			else { return I18n.format("dialog.io.no_io"); } 
		}
		if (!te.canInput()) { return I18n.format("dialog.io.no_input"); }
		if (!te.canOutput()) { return I18n.format("dialog.io.no_output"); }
		return I18n.format(te.getInputState() ? IOState.INPUT.getName() : IOState.OUTPUT.getName());
	}
}
