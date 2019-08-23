package jaminv.advancedmachines.lib.dialog;

import jaminv.advancedmachines.lib.util.coord.CoordRect;
import jaminv.advancedmachines.lib.util.coord.Rect;
import net.minecraft.util.ResourceLocation;

public interface IDialog {
	ResourceLocation getBackground();
	int getW();
	int getH();
	Rect getDialogArea();
}