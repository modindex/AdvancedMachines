package jaminv.advancedmachines.lib.dialog.fluid;

import jaminv.advancedmachines.lib.dialog.Dialog;
import jaminv.advancedmachines.lib.dialog.control.DialogToggleButton;
import jaminv.advancedmachines.lib.dialog.control.enums.IOState;
import jaminv.advancedmachines.lib.dialog.struct.DialogTexture;
import jaminv.advancedmachines.lib.dialog.struct.DialogTextureMap;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;

public class DialogBucketToggle extends DialogToggleButton<IOState> {
	
	public static interface Toggle {
		public void setBucketState(IOState state);
		public IOState getBucketState();
	}
	
	public static interface Provider {
		public Toggle getBucketToggle();
	}
	
	protected final Toggle te;
	public DialogBucketToggle(int x, int y, int w, int h, Toggle te) {
		super(x, y, w, h, IOState.INPUT);
		this.te = te;
	}

	@Override
	public DialogBucketToggle addTexture(IOState identifier, int u, int v) {
		super.addTexture(identifier, u, v);	return this;
	}
	
	@Override
	public DialogTextureMap addTexture(IOState identifier, DialogTexture texture) {
		super.addTexture(identifier, texture); return this;
	}

	@Override
	protected void onStateChanged(IOState newstate) {
		te.setBucketState(newstate);
	}
	
	@Override
	public void draw(Dialog gui, FontRenderer font, int drawX, int drawY) {
		this.state = te.getBucketState();
		super.draw(gui, font, drawX, drawY);		
	}

	@Override
	public String getTooltip(int mouseX, int mouseY) {
		return I18n.format(te.getBucketState() == IOState.INPUT ? "dialog.bucket.input" : "dialog.bucket.output");
	}
}
