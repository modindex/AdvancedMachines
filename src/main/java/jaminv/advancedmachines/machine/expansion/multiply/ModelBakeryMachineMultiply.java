package jaminv.advancedmachines.machine.expansion.multiply;

import java.util.List;

import jaminv.advancedmachines.machine.expansion.ModelBakeryMachineExpansion;
import jaminv.advancedmachines.machine.multiblock.model.MultiblockTextureBase;
import jaminv.advancedmachines.machine.multiblock.model.QuadBuilderMultiblock;
import jaminv.advancedmachines.machine.multiblock.model.QuadBuilderMultiblockItem;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.item.ItemStack;

public class ModelBakeryMachineMultiply extends ModelBakeryMachineExpansion {
	
	public ModelBakeryMachineMultiply(VariantExpansion variant) {
		super(MultiblockTextureBase.MULTIPLY, variant);
	}

	// TODO: Machine Face
	/*protected static class LayeredTextureMultiblockMachineFace extends LayeredTextureMultiblock {

		public LayeredTextureMultiblockMachineFace(IBlockState state) {	super(state, "expansion"); }

		@Override
		protected TextureAtlasSprite getBaseTexture(String variant) {
			IExtendedBlockState ext = (IExtendedBlockState)getState();
			MachineType type = ext.getValue(Properties.MACHINE_TYPE);
			MachineFace face = ext.getValue(Properties.MACHINE_FACE);
			boolean active = ext.getValue(Properties.ACTIVE);
			
			if (face == MachineFace.NONE) {
				return RawTextures.get(getBase(), variant, "base");
			} else {
				return RawTextures.get(type.getName(), active ? "active" : "inactive", variant, face.getName());
			}
		}
	}*/

	@Override
	public List<BakedQuad> bakeModel(IBlockState state) {
		return new QuadBuilderMultiblock(state, MultiblockTextureBase.MULTIPLY).build();
	}

	@Override
	public List<BakedQuad> bakeItemModel(ItemStack stack) {
		return new QuadBuilderMultiblockItem(stack, MultiblockTextureBase.MULTIPLY).build();
	}
}
