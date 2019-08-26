package jaminv.advancedmachines.lib.util.registry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;

public class BypassStateMapper extends StateMapperBase {
	protected ModelResourceLocation loc;
	public BypassStateMapper(ModelResourceLocation resource) {
		this.loc = resource;			
	}

	@Override
	protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
		return loc;
	}
}
