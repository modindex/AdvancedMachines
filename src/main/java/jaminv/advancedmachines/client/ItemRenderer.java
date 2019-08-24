package jaminv.advancedmachines.client;

import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ItemRenderer extends TileEntityItemStackRenderer {
	
	TileEntity te;
	
	public ItemRenderer(TileEntity te) {
		this.te = te;
	}

	@Override
	public void renderByItem(ItemStack itemStackIn, float partialTicks) {
		TileEntityRendererDispatcher.instance.render(te, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks);
	}

}
