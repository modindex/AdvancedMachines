package jaminv.advancedmachines.objects.blocks.machine.dialog;

import jaminv.advancedmachines.objects.blocks.fluid.TileEntityFluid;
import jaminv.advancedmachines.util.dialog.control.DialogProgressBar;
import jaminv.advancedmachines.util.dialog.struct.DialogTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fluids.FluidStack;

public class DialogFluid extends DialogProgressBar {
	
	TileEntityFluid te;

	public DialogFluid(int x, int y, int w, int h, TileEntityFluid te) {
		super(x, y, w, h, 0, 0, ProgressAxis.VERTICAL);
		this.te = te;
	}

	@Override
	protected float getPercent() {
		return te.getFluidAmount() / (float)te.getCapacity();
	}

	@Override
	public String getTooltip(int mouseX, int mouseY) {
		if (te.getFluid() != null) {
			return I18n.format("dialog.common.fluid", te.getFluid().getLocalizedName(), te.getFluidAmount(), te.getCapacity());
		} else {
			return I18n.format("dialog.common.fluid.empty", te.getFluidAmount(), te.getCapacity());
		}
	}

	@Override
	protected void drawProgressBar(GuiScreen gui, DialogTexture texture, int x, int y, int u, int v, int w, int h) {
		if (te.getFluid() == null) { return; }
		FluidStack fluid = te.getFluid();
		
		Minecraft mc = Minecraft.getMinecraft();
		TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluid.getFluid().getStill(fluid).toString());
		
		gui.drawTexturedModalRect(x, y, sprite, w, h);
	}
}
