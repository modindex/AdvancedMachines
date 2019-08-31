package jaminv.advancedmachines.machine.multiblock.model;

import jaminv.advancedmachines.lib.render.quad.Texture;
import jaminv.advancedmachines.machine.multiblock.MultiblockBorderType;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import jaminv.advancedmachines.render.textureset.TextureSets;
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
	
	public Texture getTexture(VariantExpansion variant, TextureSide side) {
		return getTexture(variant, side, "base");
	}
	
	public Texture getItemTexture(VariantExpansion variant, TextureSide side) {
		return getTexture(variant, side, "all");
	}	
	
	private Texture getTexture(VariantExpansion variant, TextureSide side, String file) {
		return TextureSets.get(texture, variant.getName(), side.getName(), file);
	}
	
	public TextureAtlasSprite getParticleTexture(VariantExpansion variant) {
		return getTexture(variant, TextureSide.SIDE, "all").getSprite();
	}
	
	protected Texture getBorder(VariantExpansion variant, TextureSide side, MultiblockBorderType border, String edge) {
		if (border != MultiblockBorderType.NONE) {
			return TextureSets.get(texture, variant.getName(), side.getName(), "borders", border.getName(), edge);
		} else { return null; }
	}	
}
