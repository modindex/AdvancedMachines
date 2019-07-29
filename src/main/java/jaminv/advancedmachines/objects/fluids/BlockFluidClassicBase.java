package jaminv.advancedmachines.objects.fluids;

import jaminv.advancedmachines.init.BlockInit;
import jaminv.advancedmachines.util.interfaces.IHasModel;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;

public class BlockFluidClassicBase extends BlockFluidClassic implements IHasModel {

	public BlockFluidClassicBase(String name, Fluid fluid, Material material) {
		super(fluid, material);
		
		BlockInit.BLOCKS.add(this);
		
		setUnlocalizedName(name);
		setRegistryName(name);
	}
	
	@Override
	public void registerModels() {
		ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(LEVEL).build());
	}
}
