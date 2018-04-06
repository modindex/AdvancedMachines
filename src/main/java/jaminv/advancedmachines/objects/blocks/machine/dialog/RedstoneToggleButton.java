package jaminv.advancedmachines.objects.blocks.machine.dialog;

import jaminv.advancedmachines.objects.blocks.machine.expansion.inventory.TileEntityMachineInventory;
import jaminv.advancedmachines.objects.blocks.machine.TileEntityMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.expansion.inventory.DialogMachineInventory.IOState;
import jaminv.advancedmachines.objects.blocks.machine.expansion.inventory.DialogMachineInventory.IOToggleButton;
import jaminv.advancedmachines.util.dialog.control.DialogToggleButton;
import jaminv.advancedmachines.util.dialog.control.DialogToggleButton.IEnumIterable;
import jaminv.advancedmachines.util.dialog.struct.DialogTooltip;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class RedstoneToggleButton extends DialogToggleButton<RedstoneToggleButton.RedstoneState> {
	public static enum RedstoneState implements IEnumIterable<RedstoneState> {
		IGNORE(0, "dialog.common.redstone.ignore"), ACTIVE(1, "dialog.common.redstone.active"), INACTIVE(2, "dialog.common.redstone.inactive");
		private static RedstoneState[] vals = values();
		
		private final int value;
		private final String name;
		private RedstoneState(int value, String name) {
			this.value = value;
			this.name = name;
		}
		
		public int getState() { return value; }
		public String getName() { return name; }
		
		@Override
		public RedstoneState next() {
			return vals[(this.ordinal() + 1) % vals.length];
		}
	}
	
	protected final TileEntityMachineBase te;
		
	public RedstoneToggleButton(TileEntityMachineBase te, int x, int y, int w, int h) {
		// This is created before the NBT data for the tile entity is loaded, so the default is largely irrevelent here and is set later.
		super(x, y, w, h, RedstoneState.IGNORE);
		this.te = te;
	}
	
	public RedstoneToggleButton(TileEntityMachineBase te) {
		this(te, 153, 22, 14, 14);
		this.addDefaultTextures();
	}

	public void addDefaultTextures() {
		this.addTexture(RedstoneState.IGNORE, 200, 67);
		this.addTexture(RedstoneState.ACTIVE, 228, 67);
		this.addTexture(RedstoneState.INACTIVE, 214, 67);
	}
		
	@Override
	protected void onStateChanged(RedstoneState newstate) {
		te.setRedstoneState(newstate);
	}
		
	@Override
	public void draw(GuiScreen screen, FontRenderer font, int drawX, int drawY) {
		this.state = te.getRedstoneState();
		super.draw(screen, font, drawX, drawY);
	}
	
	@Override
	public String getTooltip(int mouseX, int mouseY) {
		return I18n.format(state.getName());
	}	
}
