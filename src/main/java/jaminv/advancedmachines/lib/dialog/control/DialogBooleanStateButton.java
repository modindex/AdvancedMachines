package jaminv.advancedmachines.lib.dialog.control;

import jaminv.advancedmachines.lib.dialog.control.enums.BooleanIterable;
import jaminv.advancedmachines.lib.dialog.struct.DialogTexture;
import jaminv.advancedmachines.lib.dialog.struct.DialogTextureMap;

public class DialogBooleanStateButton extends DialogToggleButton<BooleanIterable> {

	public DialogBooleanStateButton(int x, int y, int w, int h, boolean defaultState) {
		super(x, y, w, h, new BooleanIterable(defaultState));
	}

	public DialogTextureMap addTexture(boolean state, int u, int v) {
		return super.addTexture(new BooleanIterable(state), u, v);
	}

	public DialogTextureMap addTexture(boolean state, DialogTexture texture) {
		return super.addTexture(new BooleanIterable(state), texture);
	}
}
