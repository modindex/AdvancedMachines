package jaminv.advancedmachines.objects.blocks.machine.instance.furnace;

import jaminv.advancedmachines.lib.container.ContainerMachine;
import jaminv.advancedmachines.lib.container.layout.Layout.HotbarLayout;
import jaminv.advancedmachines.lib.container.layout.Layout.InventoryLayout;
import jaminv.advancedmachines.lib.container.layout.LayoutManager;
import jaminv.advancedmachines.lib.container.layout.impl.InputLayout;
import jaminv.advancedmachines.lib.container.layout.impl.OutputLayout;
import jaminv.advancedmachines.lib.machine.IRedstoneControlled;
import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogEnergyBar;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogMultiblockQuantity;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogProcessBar;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogTooltipMultiblock;
import jaminv.advancedmachines.objects.blocks.machine.dialog.RedstoneToggleButton;
import jaminv.advancedmachines.util.Color;
import jaminv.advancedmachines.util.dialog.struct.DialogArea;
import jaminv.advancedmachines.util.recipe.FurnaceManager;
import net.minecraft.inventory.Container;

public class DialogMachineFurnace extends DialogMachineBase {
	
	public static final LayoutManager layout = new LayoutManager()
		.addLayout(new InputLayout(FurnaceManager.getRecipeManager(), 53, 37))
		.addLayout(new OutputLayout(107, 37))
		.setInventoryLayout(new InventoryLayout(8, 84))
		.setHotbarLayout(new HotbarLayout(8, 142));
	
	@Override
	protected LayoutManager getLayout() { return layout; }
	
	public DialogMachineFurnace(Container container) {
		super(container, "textures/gui/furnace.png", 24, 0, 176, 195);
		
		this.addText(8, 8, 160, "dialog.furnace.title", Color.DIALOG_TEXT);
		this.addText(8, 73, "dialog.common.inventory", Color.DIALOG_TEXT);
	}
	

	public DialogMachineFurnace(ContainerMachine container, TileEntityMachineFurnace te) {
		this(container);
	
		this.addElement(new DialogProcessBar(te.getController(), 74, 37, 24, 17, 200, 50));
		this.addElement(new DialogEnergyBar(te.getEnergy(), 9, 20, 14, 50, 200, 0));
		this.addElement(new RedstoneToggleButton((IRedstoneControlled)te));
		
		this.addTooltip(new DialogTooltipMultiblock(158, 7, 11, 11, te));
		
		this.addText(new DialogMultiblockQuantity(te.getController(), 74, 34, 26, 26, Color.DIALOG_TEXT));
		this.addText(new DialogMultiblockQuantity(te.getController(), 73, 33, 26, 26, 0xFFFFFF));
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
