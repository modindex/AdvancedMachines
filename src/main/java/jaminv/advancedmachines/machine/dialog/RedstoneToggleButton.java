package jaminv.advancedmachines.machine.dialog;

import jaminv.advancedmachines.lib.dialog.Dialog;
import jaminv.advancedmachines.lib.dialog.control.DialogToggleButton;
import jaminv.advancedmachines.lib.machine.RedstoneControlled;
import jaminv.advancedmachines.lib.machine.RedstoneControlled.RedstoneState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class RedstoneToggleButton extends DialogToggleButton<RedstoneState> {

	
	protected final RedstoneControlled te;
		
	public RedstoneToggleButton(RedstoneControlled te, int x, int y, int w, int h) {
		// This is created before the NBT data for the tile entity is loaded, so the default is largely irrevelent here and is set later.
		super(x, y, w, h, RedstoneState.IGNORE);
		this.te = te;
	}
	
	public RedstoneToggleButton(RedstoneControlled te) {
		this(te, 153, 22, 14, 14);
		this.addDefaultTextures();
	}

	public void addDefaultTextures() {
		this.addTexture(RedstoneState.IGNORE, 200, 67);
		this.addTexture(RedstoneState.ACTIVE, 228, 67);
		this.addTexture(RedstoneState.INACTIVE, 214, 67);
	}
		
	@Override
	protected void onStateChanged(RedstoneState newstate) {
		te.setRedstoneState(newstate);
	}
		
	@Override
	public void draw(Dialog gui, FontRenderer font, int drawX, int drawY) {
		this.state = te.getRedstoneState();
		super.draw(gui, font, drawX, drawY);
	}
	
	@Override
	public String getTooltip(int mouseX, int mouseY) {
		return I18n.format(state.getName());
	}	
}
