package jaminv.advancedmachines.machine.expansion;

import jaminv.advancedmachines.lib.render.ModelBakery;
import jaminv.advancedmachines.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.machine.multiblock.model.MultiblockTextureBase;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import jaminv.advancedmachines.util.interfaces.IHasTileEntity;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMachineProductivity extends BlockMachineExpansion {
	
	public BlockMachineProductivity(VariantExpansion variant) {
		super(variant);
	}
	
	@Override
	public UpgradeType getUpgradeType() {
		return UpgradeType.PRODUCTIVITY;
	}	

	@Override @SideOnly(Side.CLIENT) public ModelBakery getModelBakery() { 
		return new ModelBakeryMachineExpansion(MultiblockTextureBase.PRODUCTIVITY, variant);
	}
}
