package jaminv.advancedmachines.objects.blocks.machine.instance.purifier;

import jaminv.advancedmachines.client.BakedModelMultiblock;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.BlockMachineMultiblock;
import jaminv.advancedmachines.util.enums.EnumGui;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMachinePurifier extends BlockMachineMultiblock {

	public BlockMachinePurifier(String name) {
		super(name);
	}
	
	protected int getGuiId() { return EnumGui.PURIFIER.getId(); }
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityMachinePurifier();
	}
	
	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return TileEntityMachinePurifier.class;
	}
	
	@Override
	public void registerModels() {
		registerCustomModel(BakedModelMultiblock.PURIFIER);
		registerVariantModels();
	}	
}
