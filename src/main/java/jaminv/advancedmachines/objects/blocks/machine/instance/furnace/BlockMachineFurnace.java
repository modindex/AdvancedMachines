package jaminv.advancedmachines.objects.blocks.machine.instance.furnace;

import jaminv.advancedmachines.client.BakedModelMultiblock;
import jaminv.advancedmachines.client.BakedModelMultiblockFurnace;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.BlockMachineMultiblock;
import jaminv.advancedmachines.util.enums.EnumGui;
import jaminv.advancedmachines.util.material.MaterialBase.MaterialType;
import jaminv.advancedmachines.util.material.PropertyMaterial;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockMachineFurnace extends BlockMachineMultiblock {

	public BlockMachineFurnace(String name) {
		super(name);
	}
	
	protected int getGuiId() { return EnumGui.FURNACE.getId(); }
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityMachineFurnace();
	}
	
	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return TileEntityMachineFurnace.class;
	}
	
	@Override
	public void registerModels() {
		registerCustomModel(BakedModelMultiblockFurnace.RESOURCELOCATION);
		registerVariantModels();
	}		
}
