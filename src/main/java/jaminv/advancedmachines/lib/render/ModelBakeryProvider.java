package jaminv.advancedmachines.lib.render;

import javax.annotation.Nonnull;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ModelBakeryProvider {
	@SideOnly (Side.CLIENT)
	public @Nonnull ModelBakery getModelBakery();
}
