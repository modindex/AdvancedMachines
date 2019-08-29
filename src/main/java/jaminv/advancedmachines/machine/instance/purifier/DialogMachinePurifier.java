package jaminv.advancedmachines.machine.instance.purifier;

import jaminv.advancedmachines.lib.container.ContainerMachine;
import jaminv.advancedmachines.lib.container.layout.JeiLayoutManager;
import jaminv.advancedmachines.lib.jei.JeiDialog;
import jaminv.advancedmachines.lib.jei.element.JeiEnergyBar;
import jaminv.advancedmachines.lib.jei.element.JeiProgressIndicator;
import jaminv.advancedmachines.lib.jei.element.JeiSecondaryChance;
import jaminv.advancedmachines.lib.machine.IRedstoneControlled;
import jaminv.advancedmachines.lib.util.coord.CoordRect;
import jaminv.advancedmachines.machine.dialog.DialogEnergyBar;
import jaminv.advancedmachines.machine.dialog.DialogMultiblockQuantity;
import jaminv.advancedmachines.machine.dialog.DialogProcessBar;
import jaminv.advancedmachines.machine.dialog.DialogTooltipMultiblock;
import jaminv.advancedmachines.machine.dialog.RedstoneToggleButton;
import jaminv.advancedmachines.util.Color;
import net.minecraft.inventory.Container;

public class DialogMachinePurifier extends JeiDialog {
	
	@Override
	public JeiLayoutManager getLayout() { return TileMachinePurifier.layout; }
	
	public DialogMachinePurifier(Container container) {
		super(container, "textures/gui/purifier.png", 24, 0, 176, 195);
		
		this.addText(8, 8, 160, "dialog.purifier.title", Color.DIALOG_TEXT);
		this.addText(8, 73, "dialog.common.inventory", Color.DIALOG_TEXT);

		addJeiElement(new JeiEnergyBar(9, 20, 14, 50, 200, 0));
		addJeiElement(new JeiProgressIndicator(74, 23, 24, 17, 200, 50));
		addJeiElement(new JeiSecondaryChance.Above(getLayout()));
	}
	
	public DialogMachinePurifier(ContainerMachine container, TileMachinePurifier te) {
		this(container);
		
		this.addElement(new DialogProcessBar(te.getController(), 74, 23, 24, 17, 200, 50));
		this.addElement(new DialogEnergyBar(te.getEnergy(), 9, 20, 14, 50, 200, 0));
		this.addElement(new RedstoneToggleButton((IRedstoneControlled)te));
		
		this.addTooltip(new DialogTooltipMultiblock(158, 7, 11, 11, te));
		
		this.addText(new DialogMultiblockQuantity(te.getController(), 74, 19, 26, 26, Color.DIALOG_TEXT));
		this.addText(new DialogMultiblockQuantity(te.getController(), 73, 18, 26, 26, Color.WHITE));
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
