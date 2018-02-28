package jaminv.advancedmachines.util.dialog.control;

import jaminv.advancedmachines.objects.blocks.machine.TileEntityMachineBase;
import jaminv.advancedmachines.util.dialog.struct.DialogTexture;
import jaminv.advancedmachines.util.dialog.struct.DialogTextureMap.DialogTextureMapDefault;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;

public abstract class DialogProgressBar extends DialogTextureMapDefault implements IDialogElement {
	public static enum ProgressAxis { HORIZONTAL, VERTICAL };
	
	protected final ProgressAxis axis;
	public DialogProgressBar(int x, int y, int w, int h, ProgressAxis axis) {
		super(x, y, w, h);
		this.axis = axis;
	}
	
	public DialogProgressBar(int x, int y, int w, int h, int u, int v, ProgressAxis axis) {
		super(x, y, w, h, u, v);
		this.axis = axis;
	}
	
	protected abstract float getPercent();

	@Override
	public void draw(GuiScreen gui, FontRenderer font, int drawX, int drawY) {
		DialogTexture texture = this.getTexture(TextureDefault.DEFAULT);		
		int y = drawY;
		int w = this.getW(), h = this.getH();
		int v = texture.getV();
		
		switch (this.axis) {
		case HORIZONTAL:
			w *= this.getPercent();
			break;
		case VERTICAL:
			h *= this.getPercent();
			y += this.getH() - h;
			v += this.getH() - h;
			break;
		}
		
		gui.drawTexturedModalRect(drawX, y, texture.getU(), v, w, h);
	}
}
