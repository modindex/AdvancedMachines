package jaminv.advancedmachines.util.dialog.control;

import java.util.ArrayList;
import java.util.List;

import jaminv.advancedmachines.util.dialog.gui.IGuiObserver;
import jaminv.advancedmachines.util.dialog.struct.DialogArea;
import jaminv.advancedmachines.util.enums.EnumComponent;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class DialogTextBox extends DialogArea implements IDialogControlAdvanced {

	protected int id, max;
	protected GuiTextField field = null;
	protected String text = "";
	protected String pattern = ".";
	
	public DialogTextBox(int id, int x, int y, int w, int h, int max) {
		super(x, y, w, h);
		this.id = id;
		this.max = max;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	@Override
	public void init(GuiScreen screen, FontRenderer font, int drawX, int drawY) {
		field = new GuiTextField(id, font, drawX, drawY, this.getW(), this.getH());
		field.setText(text);
		field.setMaxStringLength(max);
		field.setEnableBackgroundDrawing(false);
	}

	@Override
	public void draw(GuiScreen screen, FontRenderer font, int drawX, int drawY) {
		field.drawTextBox();
	}

	@Override
	public boolean mouseClicked(int relativeX, int relativeY, int mouseButton) {
		field.setFocused(true);
		return true;
	}
	
	@Override
	public boolean keyTyped(char c, int i) {
		boolean ret = false;
		if (field.isFocused()) {
			//String s = String.valueOf(c);
			//if (s != "" && !s.matches(pattern)) {
			//	return false;
			//}
			
			ret = field.textboxKeyTyped(c, i);
			text = field.getText();
			
			for (IElementStateObserver<String> obv : observers) {
				obv.onStateChanged(this, text);
			}
		}
		return ret;
	}
	
	private List<IElementStateObserver<String>> observers = new ArrayList<>();	
	public void addObserver(IElementStateObserver<String> obv) {
		this.observers.add(obv);
	}

	public void removeObserver(IElementStateObserver<String> obv) {
		this.observers.remove(obv);
	}
}
