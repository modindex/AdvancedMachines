package jaminv.advancedmachines.objects.blocks.machine.alloy;

import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase.ContainerLayout;
import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase.HotbarLayout;
import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase.InventoryLayout;
import jaminv.advancedmachines.util.dialog.DialogBase;
import jaminv.advancedmachines.util.dialog.DialogBase.Target;
import jaminv.advancedmachines.util.dialog.DialogBase.Texture;

public class DialogMachineAlloy extends DialogMachineBase {
	
	public DialogMachineAlloy() {
		super("textures/gui/alloy.png", 24, 0, 176, 166);
		setProcessTexture(98, 37, 16, 16, 200, 50);
		setEnergyTexture(9, 20, 14, 50, 200, 0);
		
		addText(8, 8, 160, "dialog.alloy.title", 0x404040);
		addText(8, 73, "dialog.common.inventory", 0x404040);
	}
	
	public DialogMachineAlloy(TileEntityMachineAlloy te) {
		this();
		addTooltip(new TooltipEnergy(9, 20, 14, 50, te));
	}
	
	public static final DialogBase.Target jeiTarget = new DialogBase.Target(98, 37, 16, 16);
	public static final DialogBase.Target jeiBackground = new DialogBase.Target(31, 18, 162, 54);
	
	@Override
	public ContainerLayout getInputLayout() {
		return new ContainerLayout(35, 37, 1, 3);
	}
	@Override
	public ContainerLayout getOutputLayout() {
		return new ContainerLayout(125, 37);
	}
	@Override
	public ContainerLayout getSecondaryLayout() {
		return new ContainerLayout(0, 0, 0, 0);
	}
	@Override
	protected ContainerLayout getInventoryLayout() {
		return new InventoryLayout(8, 84);
	}
	@Override
	protected ContainerLayout getHotbarLayout() {
		return new HotbarLayout(8, 142);
	}
	
	@Override
	public Texture getJeiTexture() {
		return new Texture(31, 18, 162, 54);
	}
	
	@Override
	public Target getJeiTarget() {
		return new DialogBase.Target(80, 37, 16, 16);
	}	
}
