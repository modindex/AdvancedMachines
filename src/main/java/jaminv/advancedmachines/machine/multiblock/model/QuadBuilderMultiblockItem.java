package jaminv.advancedmachines.machine.multiblock.model;

import java.util.List;

import jaminv.advancedmachines.lib.render.quad.LayeredTextureList;
import jaminv.advancedmachines.lib.render.quad.QuadBuilder;
import jaminv.advancedmachines.lib.render.quad.QuadBuilderLayeredBlock;
import jaminv.advancedmachines.lib.render.quad.Texture;
import jaminv.advancedmachines.machine.BlockMachine;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import jaminv.advancedmachines.render.RawTextures;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class QuadBuilderMultiblockItem implements QuadBuilder {

	ItemStack stack;
	MultiblockTextureBase base; 
	Texture face;
	boolean inverted = false;
	
	public QuadBuilderMultiblockItem(ItemStack stack, MultiblockTextureBase base) {
		this.stack = stack;
		this.base = base;
	}
	
	public QuadBuilderMultiblockItem withFace(Texture face) {
		this.face = face;
		return this;
	}
	
	public QuadBuilderMultiblockItem invert() { this.inverted = true; return this; }
	public QuadBuilderMultiblockItem invert(boolean inverted) { this.inverted = inverted; return this; }
	
	@Override
	public List<BakedQuad> build() {
		VariantExpansion variant = VariantExpansion.BASIC;
		Item item = stack.getItem();
		if (item instanceof ItemBlock) {
			Block block = Block.getBlockFromItem(item);
			if (block instanceof VariantExpansion.HasVariant) {
				variant = ((VariantExpansion.HasVariant)block).getVariant();
			}
			if (block instanceof BlockMachine) {
				BlockMachine machine = ((BlockMachine)block);
				face = RawTextures.get(machine.getMachineType().getName(), "inactive");
			}
		}
		Texture side = base.getItemTexture(variant, TextureSide.SIDE);
		Texture top = base.getItemTexture(variant, TextureSide.TOP);
		
		QuadBuilderLayeredBlock quad = new QuadBuilderLayeredBlock(new LayeredTextureList(side)).withTopBottom(new LayeredTextureList(top)).invert(inverted);
		if (face != null) { quad.withFace(EnumFacing.NORTH, new LayeredTextureList(side, face)); }
		return quad.build();
	}
}
