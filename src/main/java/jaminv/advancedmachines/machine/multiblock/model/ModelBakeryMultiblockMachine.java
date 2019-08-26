package jaminv.advancedmachines.machine.multiblock.model;

import java.util.List;

import jaminv.advancedmachines.client.RawTextures;
import jaminv.advancedmachines.client.textureset.TextureSets;
import jaminv.advancedmachines.init.property.Properties;
import jaminv.advancedmachines.lib.render.ModelBakery;
import jaminv.advancedmachines.lib.render.TextureHelper;
import jaminv.advancedmachines.lib.render.quad.LayeredTexture;
import jaminv.advancedmachines.lib.render.quad.QuadBuilderBlock;
import jaminv.advancedmachines.lib.render.quad.QuadBuilderLayeredBlock;
import jaminv.advancedmachines.machine.BlockMachineMultiblock;
import jaminv.advancedmachines.machine.MachineHelper;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.machine.multiblock.face.SidedTexture;
import jaminv.advancedmachines.objects.material.MaterialExpansion;
import jaminv.advancedmachines.util.helper.BlockHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;

public class ModelBakeryMultiblockMachine implements ModelBakery {

	@Override
	public TextureAtlasSprite getParticleTexture(String variant) {
		return MachineHelper.getParticleTexture("expansion", variant);
	}

	@Override
	public List<BakedQuad> bakeModel(IBlockState state) {
		IExtendedBlockState ext = (IExtendedBlockState)state;
		
		return new QuadBuilderLayeredBlock(new LayeredTextureMultiblock(state, "expansion"))
			.withFace(BlockHelper.getExtendedFacing(state), 
				new LayeredTextureMultiblockMachine(state, ext.getValue(Properties.MACHINE_TYPE).getName()).withBase("expansion"))
			.withTopBottom(new LayeredTextureMultiblock(state, "expansion").withSided(SidedTexture.TOP))
		.build();
	}

	@Override
	public List<BakedQuad> bakeItemModel(ItemStack stack) {
		TextureAtlasSprite face = TextureHelper.getMissingTexture();
		String variant = MaterialExpansion.byMetadata(stack.getMetadata()).getName();
		
		Item item = stack.getItem();
		if (item instanceof ItemBlock) {
			Block block = Block.getBlockFromItem(item);
			if (block instanceof BlockMachineMultiblock) {
				MachineType type = ((BlockMachineMultiblock)block).getMachineType();
				face = RawTextures.get(type.getName(), "inactive", variant, "all");
			}
		}
		TextureAtlasSprite top = TextureSets.get("expansion", variant, SidedTexture.TOP.getName(), "all");
		return new QuadBuilderBlock.Unit(EnumFacing.NORTH, TextureSets.get("expansion", variant, SidedTexture.SIDE.getName(), "all"), face, top, top).build();
	}
}
