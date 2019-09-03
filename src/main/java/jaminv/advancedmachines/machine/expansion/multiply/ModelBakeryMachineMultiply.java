package jaminv.advancedmachines.machine.expansion.multiply;

import java.util.List;

import jaminv.advancedmachines.machine.MachineHelper;
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

	@Override
	public List<BakedQuad> bakeModel(IBlockState state) {
		return new QuadBuilderMultiblock(state, MultiblockTextureBase.MULTIPLY)
			.withFace(MachineHelper.getMachineFace(state))
			.build();
	}

	@Override
	public List<BakedQuad> bakeItemModel(ItemStack stack) {
		return new QuadBuilderMultiblockItem(stack, MultiblockTextureBase.MULTIPLY).build();
	}
}
