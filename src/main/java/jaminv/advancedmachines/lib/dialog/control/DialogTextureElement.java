package jaminv.advancedmachines.lib.dialog.control;

import jaminv.advancedmachines.lib.dialog.Dialog;
import jaminv.advancedmachines.lib.dialog.struct.DialogTexture;
import jaminv.advancedmachines.lib.dialog.struct.DialogTextureMap.DialogTextureMapDefault;
import net.minecraft.client.gui.FontRenderer;

public class DialogTextureElement extends DialogTextureMapDefault implements IDialogElement {

	public DialogTextureElement(int x, int y, int w, int h, int u, int v) {
		super(x, y, w, h, u, v);
	}

	@Override
	public void draw(Dialog gui, FontRenderer font, int drawX, int drawY) {
		DialogTexture texture = this.getTexture(TextureDefault.DEFAULT);		
		gui.drawTexturedModalRect(drawX + this.getX(), drawY + this.getY(),
			texture.getU(), texture.getV(),
			this.getW(), this.getH());
	}
	
	@Override
	public String getTooltip(int mouseX, int mouseY) {
		return null;
	}
}
