package jaminv.advancedmachines.objects.blocks.machine.instance.alloy;

import jaminv.advancedmachines.lib.container.ContainerMachine;
import jaminv.advancedmachines.lib.container.layout.LayoutManager;
import jaminv.advancedmachines.lib.machine.IRedstoneControlled;
import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogEnergyBar;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogMultiblockQuantity;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogProcessBar;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogTooltipMultiblock;
import jaminv.advancedmachines.objects.blocks.machine.dialog.RedstoneToggleButton;
import jaminv.advancedmachines.util.Color;
import jaminv.advancedmachines.util.dialog.struct.DialogArea;
import net.minecraft.inventory.Container;

public class DialogMachineAlloy extends DialogMachineBase {
	
	public static final LayoutManager layout = new LayoutManager()
		.addLayout(35, 37, 1, 3)		// Input 
		.addLayout(125, 37)				// Output		
		.setInventoryLayout(8, 84)
		.setHotbarLayout(8, 142);
	
	@Override
	protected LayoutManager getLayout() { return layout; }

	public DialogMachineAlloy(Container container) {
		super(container, "textures/gui/alloy.png", 24, 0, 176, 166);
		
		this.addText(8, 8, 160, "dialog.alloy.title", 0x404040);
		this.addText(8, 73, "dialog.common.inventory", 0x404040);
	}
	
	public DialogMachineAlloy(ContainerMachine container, TileEntityMachineAlloy te) {
		this(container);

		this.addElement(new DialogProcessBar(te.getController(), 92, 37, 24, 17, 200, 50));
		this.addElement(new DialogEnergyBar(te.getEnergy(), 9, 20, 14, 50, 200, 0));
		this.addElement(new RedstoneToggleButton((IRedstoneControlled)te));
		
		this.addTooltip(new DialogTooltipMultiblock(158, 7, 11, 11, te));		
		
		this.addText(new DialogMultiblockQuantity(te.getController(), 92, 33, 26, 26, Color.DIALOG_TEXT));
		this.addText(new DialogMultiblockQuantity(te.getController(), 91, 32, 26, 26, Color.WHITE));		
	}
	
	@Override
	public DialogArea getJeiTexture() {
		return new DialogArea(31, 18, 146, 54);
	}
	
	@Override
	public DialogArea getJeiTarget() {
		return new DialogArea(153, 38, 14, 14);
	}
}
