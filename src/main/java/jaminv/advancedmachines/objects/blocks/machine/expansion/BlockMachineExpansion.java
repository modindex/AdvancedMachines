package jaminv.advancedmachines.objects.blocks.machine.expansion;

import jaminv.advancedmachines.objects.blocks.BlockMaterial;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.TileEntityMachineMultiblock;
import jaminv.advancedmachines.util.helper.BlockHelper;
import jaminv.advancedmachines.util.helper.BlockHelper.ScanResult;
import jaminv.advancedmachines.util.material.MaterialBase;
import jaminv.advancedmachines.util.material.MaterialExpansion;
import jaminv.advancedmachines.util.material.PropertyMaterial;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockMachineExpansion extends BlockMaterial implements IMachineUpgrade {

	public BlockMachineExpansion(String name) {
		super(name, MaterialBase.MaterialType.EXPANSION, null, Material.IRON, 5.0f);
	}

	@Override
	protected PropertyMaterial getVariant() {
		return PropertyMaterial.create("variant", MaterialBase.MaterialType.EXPANSION);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		scanMultiblock(worldIn, pos);
	}	
	
	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		scanMultiblock(worldIn, pos);
	}
	
	@Override
	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
		scanMultiblock(worldIn, pos);
	}
	
	protected void scanMultiblock(World world, BlockPos pos) {
		ScanResult result = BlockHelper.scanBlocks(world, pos, new TileEntityMachineMultiblock.MultiblockChecker());
		BlockPos end = result.getEnd();
		if (end == null) { return; }
		
		TileEntity te = world.getTileEntity(end);
		if (te instanceof TileEntityMachineMultiblock) {
			((TileEntityMachineMultiblock)te).scanMultiblock();
		}
	}

	@Override
	public UpgradeType getUpgradeType() {
		return UpgradeType.MULTIPLY;
	}

	@Override
	public int getUpgradeQty(World world, BlockPos pos) {
		return MaterialExpansion.byMetadata(this.getMetaFromState(world.getBlockState(pos))).getMultiplier();
	}
}
