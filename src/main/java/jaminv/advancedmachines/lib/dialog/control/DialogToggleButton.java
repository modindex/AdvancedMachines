package jaminv.advancedmachines.lib.dialog.control;

import jaminv.advancedmachines.lib.dialog.Dialog;
import jaminv.advancedmachines.lib.dialog.control.DialogToggleButton.IEnumIterable;
import jaminv.advancedmachines.lib.dialog.control.enums.IOState;
import jaminv.advancedmachines.lib.dialog.struct.DialogTexture;
import jaminv.advancedmachines.lib.dialog.struct.DialogTextureMap;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;

public class DialogToggleButton<T extends DialogToggleButton.IEnumIterable<T>> extends DialogTextureMap<T> implements IDialogControl {
	public static interface IEnumIterable<T> {
		public T next();
	}
	
	protected T state;
	
	public DialogToggleButton(int x, int y, int w, int h, T defaultState) {
		super(x, y, w, h);
		state = defaultState;
	}
	
	protected void setState(T state) { this.state = state; }
	public T getState() { return this.state; }
	
	protected boolean disabled = false;
	protected DialogTexture disabledTexture = null;

	public DialogToggleButton setDisabledTexture(int u, int v) {
		return setDisabledTexture(new DialogTexture(u, v));
	}
	
	public void disable() { this.state = null; }
	
	public DialogToggleButton setDisabledTexture(DialogTexture texture) {
		this.disabledTexture = texture;
		return this;
	}

	@Override
	public void draw(Dialog gui, FontRenderer font, int drawX, int drawY) {
		if (state == null) {
			if (disabledTexture == null) { return; }
			this.drawDisabledTexture(gui, drawX, drawY);
		} else {
			this.draw(gui, drawX, drawY, state);
		}
	}
	
	public void drawDisabledTexture(Dialog gui, int drawX, int drawY) {
		gui.drawTexturedModalRect(drawX, drawY, disabledTexture.getU(), disabledTexture.getV(), w, h);		
	}

	@Override
	public boolean mouseClicked(int relativeX, int relativeY, int mouseButton) {
		if (disabled) { return false; }
		if (canChangeState(this.state.next())) {
			this.state = this.state.next();
		}
		this.onStateChanged(this.state);
		return true;
	}
	
	protected boolean canChangeState(T newstate) { return true; }
	protected void onStateChanged(T newstate) {	}

	@Override
	public String getTooltip(int mouseX, int mouseY) {
		return null;
	}
}
