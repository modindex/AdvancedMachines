package jaminv.advancedmachines.lib.jei;

import java.util.ArrayList;

import jaminv.advancedmachines.lib.container.ContainerMachine;
import jaminv.advancedmachines.lib.container.layout.IItemLayout;
import jaminv.advancedmachines.lib.container.layout.IJeiLayoutManager;
import jaminv.advancedmachines.lib.container.layout.ItemLayoutGrid;
import jaminv.advancedmachines.lib.dialog.Dialog;
import jaminv.advancedmachines.lib.jei.element.IJeiElement;
import jaminv.advancedmachines.lib.util.coord.CoordRect;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public abstract class JeiDialog extends Dialog implements IJeiDialog {
	public JeiDialog(Container container, String background, int xpos, int ypos, int width, int height) {
		super(container, background, xpos, ypos, width, height); 
	}
	
	@Override public ResourceLocation getJeiBackground() { return this.getBackground(); }
	
	protected ArrayList<IJeiElement> jeielement = new ArrayList<IJeiElement>();
	protected void addJeiElement(IJeiElement element) {
		jeielement.add(element);
	}
	@Override public ArrayList<IJeiElement> getJeiElements() { return jeielement; }
}
