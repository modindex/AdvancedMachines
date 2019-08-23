package jaminv.advancedmachines.machine.dialog;

import jaminv.advancedmachines.lib.dialog.Dialog;
import jaminv.advancedmachines.lib.dialog.control.DialogProgressBar;
import jaminv.advancedmachines.lib.dialog.struct.DialogTexture;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fluids.IFluidTank;

public class DialogFluid extends DialogProgressBar {
	
	IFluidTank tank;

	public DialogFluid(int x, int y, int w, int h, IFluidTank tank) {
		super(x, y, w, h, 0, 0, ProgressAxis.VERTICAL);
		this.tank = tank;
	}

	@Override
	protected float getPercent() {
		return tank.getFluidAmount() / (float)tank.getCapacity();
	}

	@Override
	public String getTooltip(int mouseX, int mouseY) {
		if (tank.getFluid() != null) {
			return I18n.format("dialog.common.fluid", tank.getFluid().getLocalizedName(), tank.getFluidAmount(), tank.getCapacity());
		} else {
			return I18n.format("dialog.common.fluid.empty", tank.getFluidAmount(), tank.getCapacity());
		}
	}

	@Override
	protected void drawProgressBar(Dialog gui, DialogTexture texture, int x, int y, int u, int v, int w, int h) {
		gui.drawFluid(x, y, tank.getFluid(), w, h);
	}
}
