package jaminv.advancedmachines.machine.multiblock.model;

import java.util.List;

import jaminv.advancedmachines.client.textureset.TextureSets;
import jaminv.advancedmachines.machine.multiblock.MultiblockBorderType;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public enum MultiblockTextureBase {
	MULTIPLY("multiply"),
	SPEED("speed"),
	PRODUCTIVITY("productivity"),
	TANK("tank");

	protected String texture;
	
	private MultiblockTextureBase(String texture) {
		this.texture = texture;
	}
	
	public TextureAtlasSprite getTexture(VariantExpansion variant, TextureSide side) {
		return getTexture(variant, side, "base");
	}
	
	public TextureAtlasSprite getItemTexture(VariantExpansion variant, TextureSide side) {
		return getTexture(variant, side, "all");
	}	
	
	private TextureAtlasSprite getTexture(VariantExpansion variant, TextureSide side, String file) {
		return TextureSets.get(texture, variant.getName(), side.getName(), file);
	}
	
	public TextureAtlasSprite getParticleTexture(VariantExpansion variant) {
		return getTexture(variant, TextureSide.SIDE, "all");
	}
	
	protected TextureAtlasSprite getBorder(VariantExpansion variant, TextureSide side, MultiblockBorderType border, String edge) {
		if (border != MultiblockBorderType.NONE) {
			return TextureSets.get(texture, variant.getName(), side.getName(), "borders", border.getName(), edge);
		} else { return null; }
	}	
}
