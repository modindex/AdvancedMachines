package jaminv.advancedmachines.objects.blocks.machine;

import jaminv.advancedmachines.util.dialog.DialogBase;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public abstract class DialogMachineBase extends DialogBase {
	
	public DialogMachineBase(String background, int xpos, int ypos, int width, int height) {
		super(background, xpos, ypos, width, height);
	}
	
	final protected int SLOT_X_SPACING = 18;
	final protected int SLOT_Y_SPACING = 18;
	final protected int BORDER_X_SPACING = 8;
	final protected int INVENTORY_ROWS = 3;
	final protected int INVENTORY_COLS = 9;	
	final protected int HOTBAR_ROWS = 1;
	final protected int HOTBAR_COLS = 9;
	
	public class ContainerLayout {
		protected final int xpos, ypos, xspacing, yspacing, rows, cols;
		public ContainerLayout(int xpos, int ypos, int xspacing, int yspacing, int rows, int cols) {
			this.xpos = xpos; this.ypos = ypos;
			this.xspacing = xspacing; this.yspacing = yspacing;
			this.rows = rows; this.cols = cols;
		}
		public ContainerLayout(int xpos, int ypos, int rows, int cols) {
			this(xpos, ypos, SLOT_X_SPACING, SLOT_Y_SPACING, rows, cols);
		}
		public ContainerLayout(int xpos, int ypos) {
			this(xpos, ypos, SLOT_X_SPACING, SLOT_Y_SPACING, 1, 1);
		}
		public int getXPos() { return xpos; }
		public int getYPos() { return ypos; }
		public int getXSpacing() { return xspacing; }
		public int getYSpacing() { return yspacing; }
		public int getRows() { return rows; }
		public int getCols() { return cols; }
		
		public Position getPosition(int index) {
			return new Position(xpos + (index % cols) * xspacing, ypos + (index / cols) * yspacing);
		}
	}
	public class InventoryLayout extends ContainerLayout {
		public InventoryLayout(int xpos, int ypos) {
			super(xpos, ypos, SLOT_X_SPACING, SLOT_Y_SPACING, INVENTORY_ROWS, INVENTORY_COLS);
		}
	}
	public class HotbarLayout extends ContainerLayout {
		public HotbarLayout(int xpos, int ypos) {
			super (xpos, ypos, SLOT_X_SPACING, SLOT_Y_SPACING, HOTBAR_ROWS, HOTBAR_COLS);
		}
	}
	
	public abstract ContainerLayout getInputLayout();
	public abstract ContainerLayout getOutputLayout();
	public abstract ContainerLayout getSecondaryLayout();
	protected abstract ContainerLayout getInventoryLayout();
	protected abstract ContainerLayout getHotbarLayout();	
	
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
		return getJeiOffset(this.getInputLayout());
	}
	
	public ContainerLayout getJeiOutputLayout() {
		return getJeiOffset(this.getOutputLayout());
	}
	
	public ContainerLayout getJeiSecondaryLayout() {
		return getJeiOffset(this.getSecondaryLayout());
	}
}
