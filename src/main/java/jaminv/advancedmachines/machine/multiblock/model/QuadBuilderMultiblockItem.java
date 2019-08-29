package jaminv.advancedmachines.machine.multiblock.model;

import java.util.List;

import jaminv.advancedmachines.client.RawTextures;
import jaminv.advancedmachines.lib.render.quad.LayeredTextureList;
import jaminv.advancedmachines.lib.render.quad.QuadBuilder;
import jaminv.advancedmachines.lib.render.quad.QuadBuilderLayeredBlock;
import jaminv.advancedmachines.lib.render.quad.Texture;
import jaminv.advancedmachines.machine.BlockMachineMultiblock;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
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
	
	public QuadBuilderMultiblockItem(ItemStack stack, MultiblockTextureBase base) {
		this.stack = stack;
		this.base = base;
	}
	
	public QuadBuilderMultiblockItem withFace(Texture face) {
		this.face = face;
		return this;
	}
	
	@Override
	public List<BakedQuad> build() {
		VariantExpansion variant = VariantExpansion.BASIC;
		Item item = stack.getItem();
		if (item instanceof ItemBlock) {
			Block block = Block.getBlockFromItem(item);
			if (block instanceof VariantExpansion.Has) {
				variant = ((VariantExpansion.Has)block).getVariant();
			}
			if (block instanceof BlockMachineMultiblock) {
				BlockMachineMultiblock machine = ((BlockMachineMultiblock)block);
				face = RawTextures.get(machine.getMachineType().getName(), "inactive");
			}
		}
		Texture side = base.getItemTexture(variant, TextureSide.SIDE);
		Texture top = base.getItemTexture(variant, TextureSide.TOP);
		
		QuadBuilderLayeredBlock quad = new QuadBuilderLayeredBlock(new LayeredTextureList(side)).withTopBottom(new LayeredTextureList(top));
		if (face != null) { quad.withFace(EnumFacing.NORTH, new LayeredTextureList(side, face)); }
		return quad.build();
	}
}
