package jaminv.advancedmachines.objects.blocks.machine.instance.purifier;

import jaminv.advancedmachines.integration.jei.element.JeiEnergyBar;
import jaminv.advancedmachines.integration.jei.element.JeiProgressIndicator;
import jaminv.advancedmachines.objects.blocks.inventory.ContainerLayout;
import jaminv.advancedmachines.objects.blocks.inventory.Layout.HotbarLayout;
import jaminv.advancedmachines.objects.blocks.inventory.Layout.InventoryLayout;
import jaminv.advancedmachines.objects.blocks.machine.ContainerMachine;
import jaminv.advancedmachines.objects.blocks.machine.ContainerMachine.InputLayout;
import jaminv.advancedmachines.objects.blocks.machine.ContainerMachine.OutputLayout;
import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogEnergyBar;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogMultiblockQuantity;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogProcessBar;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogTooltipMultiblock;
import jaminv.advancedmachines.objects.blocks.machine.dialog.RedstoneToggleButton;
import jaminv.advancedmachines.util.Color;
import jaminv.advancedmachines.util.dialog.struct.DialogArea;
import jaminv.advancedmachines.util.interfaces.IRedstoneControlled;
import jaminv.advancedmachines.util.recipe.machine.purifier.PurifierManager;
import net.minecraft.inventory.Container;

public class DialogMachinePurifier extends DialogMachineBase {
	
	public static final ContainerLayout layout = new ContainerLayout()
		.addLayout(new InputLayout(PurifierManager.getRecipeManager(), 53, 23))
		.addLayout(new OutputLayout(107, 23))			// Output
		.addLayout(new OutputLayout(35, 55, 1, 6)) 		// Secondary
		.setInventoryLayout(new InventoryLayout(8, 84))
		.setHotbarLayout(new HotbarLayout(8, 142));
	
	@Override
	public ContainerLayout getLayout() { return layout; }
	
	public DialogMachinePurifier(Container container) {
		super(container, "textures/gui/purifier.png", 24, 0, 176, 195);
		
		this.addText(8, 8, 160, "dialog.purifier.title", Color.DIALOG_TEXT);
		this.addText(8, 73, "dialog.common.inventory", Color.DIALOG_TEXT);

		addJeiElement(new JeiEnergyBar(9, 20, 14, 50, 200, 0));
		addJeiElement(new JeiProgressIndicator(74, 23, 24, 17, 200, 50));		
		this.setDrawSecondaryChancePos(JeiDrawSecondaryChance.ABOVE);
	}
	
	public DialogMachinePurifier(ContainerMachine container, TileEntityMachinePurifier te) {
		this(container);
		
		this.addElement(new DialogProcessBar(te, 74, 23, 24, 17, 200, 50));
		this.addElement(new DialogEnergyBar(te, 9, 20, 14, 50, 200, 0));
		this.addElement(new RedstoneToggleButton((IRedstoneControlled)te));
		
		this.addTooltip(new DialogTooltipMultiblock(158, 7, 11, 11, te));
		
		this.addText(new DialogMultiblockQuantity(te, 74, 19, 26, 26, Color.DIALOG_TEXT));
		this.addText(new DialogMultiblockQuantity(te, 73, 18, 26, 26, Color.WHITE));
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
