package jaminv.advancedmachines.machine.expansion.prodctivity;

import jaminv.advancedmachines.machine.MachineHelper;
import jaminv.advancedmachines.machine.expansion.ModelBakeryExpansionType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class ModelBakeryMachineProductivity extends ModelBakeryExpansionType {
	@Override
	public TextureAtlasSprite getParticleTexture(String variant) {
		return MachineHelper.getParticleTexture("productivity", variant);
	}

	@Override
	protected String getBaseTexture() { return "productivity"; }
}
