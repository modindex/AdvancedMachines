package jaminv.advancedmachines.init.init;

import jaminv.advancedmachines.ModReference;
import jaminv.advancedmachines.lib.util.registry.RegistryHelper;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class FluidInit {
	public static void init() {
		RegistryHelper.addFluid(new Fluid("coal_tar",
				new ResourceLocation(ModReference.MODID, "fluids/coal_tar_still"),
				new ResourceLocation(ModReference.MODID, "fluids/coal_tar_flow")
			).setDensity(1300).setGaseous(false).setViscosity(30000).setTemperature(290),
			"coal_tar", new MaterialLiquid(MapColor.BLACK));
		
		RegistryHelper.addFluid(new Fluid("tree_resin",
				new ResourceLocation(ModReference.MODID, "fluids/resin_still"),
				new ResourceLocation(ModReference.MODID, "fluids/resin_flow")
			).setDensity(2000).setGaseous(false).setViscosity(20000).setTemperature(300),
			"tree_resin", new MaterialLiquid(MapColor.BROWN));
	}
}
