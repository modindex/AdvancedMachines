package jaminv.advancedmachines.lib.jei;

import java.util.ArrayList;

import jaminv.advancedmachines.lib.container.layout.IItemLayout;
import jaminv.advancedmachines.lib.container.layout.IJeiLayoutManager;
import jaminv.advancedmachines.lib.container.layout.ItemLayoutGrid;
import jaminv.advancedmachines.lib.dialog.IDialog;
import jaminv.advancedmachines.lib.jei.element.IJeiElement;
import jaminv.advancedmachines.lib.util.coord.CoordOffset;
import jaminv.advancedmachines.lib.util.coord.Offset;
import jaminv.advancedmachines.lib.util.coord.Rect;
import net.minecraft.util.ResourceLocation;

public interface IJeiDialog extends IDialog {

	ResourceLocation getJeiBackground();

	Rect getJeiTexture();
	Rect getJeiTarget();

	ArrayList<IJeiElement> getJeiElements();

	public default Offset getJeiOffset() { return getJeiTexture().getOffset(getDialogArea()); }

	public IJeiLayoutManager getLayout();
}