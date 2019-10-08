package jaminv.advancedmachines.machine.instance.alloy;

import jaminv.advancedmachines.lib.container.ContainerMachine;
import jaminv.advancedmachines.lib.container.layout.IJeiLayoutManager;
import jaminv.advancedmachines.lib.dialog.Color;
import jaminv.advancedmachines.lib.jei.JeiDialog;
import jaminv.advancedmachines.lib.jei.element.JeiEnergyBar;
import jaminv.advancedmachines.lib.jei.element.JeiProgressIndicator;
import jaminv.advancedmachines.lib.machine.RedstoneControlled;
import jaminv.advancedmachines.lib.util.coord.CoordRect;
import jaminv.advancedmachines.machine.dialog.DialogEnergyBar;
import jaminv.advancedmachines.machine.dialog.DialogMultiblockQuantity;
import jaminv.advancedmachines.machine.dialog.DialogProcessBar;
import jaminv.advancedmachines.machine.dialog.DialogTooltipMultiblock;
import jaminv.advancedmachines.machine.dialog.RedstoneToggleButton;
import net.minecraft.inventory.Container;

public class DialogMachineAlloy extends JeiDialog {
		
	@Override
	public IJeiLayoutManager getLayout() { return TileMachineAlloy.layout; }

	public DialogMachineAlloy(Container container) {
		super(container, "textures/gui/alloy.png", 24, 0, 176, 166);
		
		this.addText(8, 8, 160, "dialog.alloy.title", 0x404040);
		this.addText(8, 73, "dialog.common.inventory", 0x404040);
		
		addJeiElement(new JeiEnergyBar(9, 20, 14, 50, 200, 0));
		addJeiElement(new JeiProgressIndicator(92, 37, 24, 17, 200, 50));		
	}
	
	public DialogMachineAlloy(ContainerMachine container, TileMachineAlloy te) {
		this(container);

		this.addElement(new DialogProcessBar(te.getController(), 92, 37, 24, 17, 200, 50));
		this.addElement(new DialogEnergyBar(te.getEnergy(), 9, 20, 14, 50, 200, 0));
		this.addElement(new RedstoneToggleButton((RedstoneControlled)te));
		
		this.addTooltip(new DialogTooltipMultiblock(158, 7, 11, 11, te));		
		
		this.addText(new DialogMultiblockQuantity(te.getController(), 92, 33, 26, 26, Color.DIALOG_TEXT));
		this.addText(new DialogMultiblockQuantity(te.getController(), 91, 32, 26, 26, Color.WHITE));		
	}
	
	@Override
	public CoordRect getJeiTexture() {
		return new CoordRect(31, 18, 146, 54);
	}
	
	@Override
	public CoordRect getJeiTarget() {
		return new CoordRect(153, 38, 14, 14);
	}
}
