package jaminv.advancedmachines.machine.multiblock.model;

import java.util.List;

import jaminv.advancedmachines.client.RawTextures;
import jaminv.advancedmachines.client.textureset.TextureSets;
import jaminv.advancedmachines.lib.render.quad.QuadBuilder;
import jaminv.advancedmachines.lib.render.quad.QuadBuilderBlock;
import jaminv.advancedmachines.machine.BlockMachineMultiblock;
import jaminv.advancedmachines.machine.multiblock.face.SidedTexture;
import jaminv.advancedmachines.objects.variant.HasVariant;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class QuadBuilderMultiblockItem implements QuadBuilder {

	ItemStack stack;
	String base, face;
	public QuadBuilderMultiblockItem(String base, ItemStack stack) {
		this.stack = stack;
		this.base = base;
	}
	
	public QuadBuilderMultiblockItem withFace(String face) {
		this.face = face;
		return this;
	}
	
	@Override
	public List<BakedQuad> build() {
		String variant = VariantExpansion.BASIC.getName();
		TextureAtlasSprite faceTexture = null;
		
		Item item = stack.getItem();
		if (item instanceof ItemBlock) {
			Block block = Block.getBlockFromItem(item);
			if (block instanceof HasVariant) {
				variant = ((HasVariant)block).getVariant().getName();
			}
			if (block instanceof BlockMachineMultiblock) {
				BlockMachineMultiblock machine = ((BlockMachineMultiblock)block);
				variant = machine.getVariant().getName();
				faceTexture = RawTextures.get(machine.getMachineType().getName(), "inactive", variant, "all");
			}
		}
		if (faceTexture == null && this.face != null) {
			faceTexture = RawTextures.get(face, variant, "all");
		}
		TextureAtlasSprite top = TextureSets.get(base, variant, SidedTexture.TOP.getName(), "all");
		
		QuadBuilderBlock quad = new QuadBuilderBlock(TextureSets.get(base, variant, SidedTexture.SIDE.getName(), "all")).withTopBottom(top);
		if (faceTexture != null) { quad.withFace(EnumFacing.NORTH, faceTexture); }
		return quad.build();
	}
}
