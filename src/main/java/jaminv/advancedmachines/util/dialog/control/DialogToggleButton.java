package jaminv.advancedmachines.util.dialog.control;

import jaminv.advancedmachines.util.dialog.struct.DialogTextureMap;
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
	
	@Override
	public void draw(GuiScreen screen, FontRenderer font, int drawX, int drawY) {
		this.draw(screen, drawX, drawY, state);
	}

	@Override
	public boolean mouseClicked(int relativeX, int relativeY, int mouseButton) {
		this.state = this.state.next();
		this.onStateChanged(this.state);
		return true;
	}
	
	protected void onStateChanged(T newstate) {	}

	@Override
	public String getTooltip(int mouseX, int mouseY) {
		return null;
	}
}
