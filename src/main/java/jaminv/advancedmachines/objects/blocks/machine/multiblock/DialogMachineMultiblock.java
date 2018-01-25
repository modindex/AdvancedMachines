package jaminv.advancedmachines.objects.blocks.machine.multiblock;

import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.TileEntityMachineBase;
import jaminv.advancedmachines.util.dialog.DialogBase.Tooltip;
import net.minecraft.client.resources.I18n;

public abstract class DialogMachineMultiblock extends DialogMachineBase {

	public DialogMachineMultiblock(String background, int xpos, int ypos, int width, int height) {
		super(background, xpos, ypos, width, height);
	}

	public class TooltipMultiblock extends Tooltip {
		
		protected final TileEntityMachineMultiblock te;

		public TooltipMultiblock(int xpos, int ypos, int width, int height, TileEntityMachineMultiblock te) {
			super(xpos, ypos, width, height, "");
			this.te = te;
		}
		
		@Override
		public String getText() {
			return te.getMultiblockString();
		}
	}
}
