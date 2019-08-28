package jaminv.advancedmachines.machine.expansion;

import jaminv.advancedmachines.lib.render.ModelBakery;
import jaminv.advancedmachines.machine.multiblock.model.MultiblockTextureBase;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMachineSpeed extends BlockMachineExpansion {
	
	public BlockMachineSpeed(VariantExpansion variant) {
		super(variant);
	}
	
	@Override
	public UpgradeType getUpgradeType() {
		return UpgradeType.SPEED;
	}	

	@Override @SideOnly(Side.CLIENT) public ModelBakery getModelBakery() { 
		return new ModelBakeryMachineExpansion(MultiblockTextureBase.SPEED, variant);
	}	
}
