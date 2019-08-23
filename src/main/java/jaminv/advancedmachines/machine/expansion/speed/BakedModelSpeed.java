package jaminv.advancedmachines.machine.expansion.speed;

import java.util.function.Function;

import jaminv.advancedmachines.client.RawTextures;
import jaminv.advancedmachines.machine.expansion.BakedModelExpansionBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;

public class BakedModelSpeed extends BakedModelExpansionBase {
	public BakedModelSpeed(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		super(state, format, bakedTextureGetter);
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return RawTextures.get("speed.basic.all");
	}

	@Override
	protected String getBaseTexture() { return "speed"; }
}
