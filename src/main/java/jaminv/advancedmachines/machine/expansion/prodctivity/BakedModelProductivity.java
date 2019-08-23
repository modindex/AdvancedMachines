package jaminv.advancedmachines.machine.expansion.prodctivity;

import java.util.function.Function;

import jaminv.advancedmachines.client.RawTextures;
import jaminv.advancedmachines.machine.expansion.BakedModelExpansionBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;

public class BakedModelProductivity extends BakedModelExpansionBase {
	public BakedModelProductivity(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		super(state, format, bakedTextureGetter);
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return RawTextures.get("productivity.basic.all");
	}

	@Override
	protected String getBaseTexture() { return "productivity"; }
}
