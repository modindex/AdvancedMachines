package jaminv.advancedmachines.objects.blocks.machine.dialog;

import jaminv.advancedmachines.lib.machine.IRedstoneControlled;
import jaminv.advancedmachines.lib.machine.IRedstoneControlled.RedstoneState;
import jaminv.advancedmachines.util.dialog.DialogBase;
import jaminv.advancedmachines.util.dialog.control.DialogToggleButton;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class RedstoneToggleButton extends DialogToggleButton<RedstoneState> {

	
	protected final IRedstoneControlled te;
		
	public RedstoneToggleButton(IRedstoneControlled te, int x, int y, int w, int h) {
		// This is created before the NBT data for the tile entity is loaded, so the default is largely irrevelent here and is set later.
		super(x, y, w, h, RedstoneState.IGNORE);
		this.te = te;
	}
	
	public RedstoneToggleButton(IRedstoneControlled te) {
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
	public void draw(DialogBase gui, FontRenderer font, int drawX, int drawY) {
		this.state = te.getRedstoneState();
		super.draw(gui, font, drawX, drawY);
	}
	
	@Override
	public String getTooltip(int mouseX, int mouseY) {
		return I18n.format(state.getName());
	}	
}
