package jaminv.advancedmachines.machine.expansion.inventory;

import java.util.List;

import jaminv.advancedmachines.client.RawTextures;
import jaminv.advancedmachines.init.property.Properties;
import jaminv.advancedmachines.machine.expansion.ModelBakeryMachineExpansion;
import jaminv.advancedmachines.machine.multiblock.model.MultiblockTextureBase;
import jaminv.advancedmachines.machine.multiblock.model.QuadBuilderMultiblock;
import jaminv.advancedmachines.machine.multiblock.model.QuadBuilderMultiblockItem;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.property.IExtendedBlockState;

public class ModelBakeryMachineInventory extends ModelBakeryMachineExpansion {
	
	public ModelBakeryMachineInventory(VariantExpansion variant) {
		super(MultiblockTextureBase.MULTIPLY, variant);
	}

	@Override
	public List<BakedQuad> bakeModel(IBlockState state) {
		return new QuadBuilderMultiblock(state, base)
			.withFace(RawTextures.get("inventory", ((IExtendedBlockState)state).getValue(Properties.INPUT) ? "input" : "output"))
			.build();
	}

	@Override
	public List<BakedQuad> bakeItemModel(ItemStack stack) {
		return new QuadBuilderMultiblockItem(stack, base).withFace(RawTextures.get("inventory.input")).build();
	}
}
