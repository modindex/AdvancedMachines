package jaminv.advancedmachines.objects.blocks.machine;

import jaminv.advancedmachines.objects.blocks.inventory.DialogInventory;
import jaminv.advancedmachines.objects.blocks.inventory.DialogInventory.ContainerLayout;
import jaminv.advancedmachines.util.dialog.DialogBase.Target;
import jaminv.advancedmachines.util.dialog.DialogBase.Texture;
import jaminv.advancedmachines.util.dialog.DialogBase.Tooltip;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public abstract class DialogMachineBase extends DialogInventory {
	
	public DialogMachineBase(String background, int xpos, int ypos, int width, int height) {
		super(background, xpos, ypos, width, height);
	}
	
	private Texture process = null;
	protected void setProcessTexture(int xpos, int ypos, int width, int height, int u, int v) {
		process = new Texture(xpos, ypos, width, height, u, v);
	}
	public Texture getProcessTexture() { return process; }
	
	private Texture energy = null;
	protected void setEnergyTexture(int xpos, int ypos, int width, int height, int u, int v) {
		energy = new Texture(xpos, ypos, width, height, u, v);
	}
	
	public void drawBackground(GuiScreen gui, int guiLeft, int guiTop, TileEntityMachineBase te) {
		super.drawBackground(gui, guiLeft, guiTop);

		if (process != null) {
			float percent = te.getProcessPercent();
			int width = (int)(percent * process.getWidth());
			gui.drawTexturedModalRect(
				guiLeft + process.getXPos(),
				guiTop + process.getYPos(),
				process.getU(),
				process.getV(),
				width,
				process.getHeight()
			);
		}
		
		if (energy != null) {
			float percent = te.getEnergyPercent();
			int height = (int)(percent * energy.getHeight());
			gui.drawTexturedModalRect(
				guiLeft + energy.getXPos(),
				guiTop + energy.getYPos() + energy.getHeight() - height,
				energy.getU(),
				energy.getV() + energy.getHeight() - height,
				energy.getWidth(),
				height
			);
		}
	}
	
	public class TooltipEnergy extends Tooltip {
		
		protected final TileEntityMachineBase te;

		public TooltipEnergy(int xpos, int ypos, int width, int height, TileEntityMachineBase te) {
			super(xpos, ypos, width, height, "");
			this.te = te;
		}
		
		@Override
		public String getText() {
			return I18n.format("dialog.common.energy", te.getEnergyStored(), te.getMaxEnergyStored());
		}
	}
	
	public ResourceLocation getJeiBackground() { return this.getBackground(); }
	public abstract Texture getJeiTexture();
	public abstract Target getJeiTarget();
	
	protected ContainerLayout getJeiOffset(ContainerLayout texture) {
		Texture dialog = this.getDialogTexture();
		Texture jei = this.getJeiTexture();
		
		return new ContainerLayout(
			texture.getXPos() + dialog.getXPos() - jei.getXPos() - 1,
			texture.getYPos() + dialog.getYPos() - jei.getYPos() - 1,
			texture.getXSpacing(), texture.getYSpacing(),
			texture.getRows(), texture.getCols()
		);		
	}
	
	public ContainerLayout getJeiInputLayout() {
		return getJeiOffset(this.getLayout(0));
	}
	
	public ContainerLayout getJeiOutputLayout() {
		return getJeiOffset(this.getLayout(1));
	}
	
	public ContainerLayout getJeiSecondaryLayout() {
		return getJeiOffset(this.getLayout(2));
	}
}
