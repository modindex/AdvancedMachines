package jaminv.advancedmachines.init;

import jaminv.advancedmachines.objects.fluids.FluidBase;
import jaminv.advancedmachines.util.Reference;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidInit {
	public static final FluidBase COAL_TAR = (FluidBase)new FluidBase("coal_tar",
			new ResourceLocation(Reference.MODID, "fluids/coal_tar_still"),
			new ResourceLocation(Reference.MODID, "fluids/coal_tar_flow")
		).setMaterial(new MaterialLiquid(MapColor.BLACK))
		.setDensity(1300).setGaseous(false).setViscosity(30000).setTemperature(290);
	
	public static void registerFluids() {
		FluidRegistry.registerFluid(COAL_TAR);
		FluidRegistry.addBucketForFluid(COAL_TAR);
	}			
}
