package jaminv.advancedmachines.machine.expansion.prodctivity;

import jaminv.advancedmachines.machine.MachineHelper;
import jaminv.advancedmachines.machine.expansion.ModelBakeryExpansionType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class ModelBakeryProductivity extends ModelBakeryExpansionType {
	@Override
	public TextureAtlasSprite getParticleTexture(IBlockState state) {
		return MachineHelper.getParticleTexture("productivity", state);
	}

	@Override
	protected String getBaseTexture() { return "productivity"; }
}
