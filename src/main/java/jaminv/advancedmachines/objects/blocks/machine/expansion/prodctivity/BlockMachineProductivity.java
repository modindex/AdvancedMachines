package jaminv.advancedmachines.objects.blocks.machine.expansion.prodctivity;

import jaminv.advancedmachines.objects.blocks.machine.TileEntityMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.expansion.BlockMachineExpansionBase;
import jaminv.advancedmachines.objects.blocks.machine.expansion.TileEntityMachineExpansionBase;
import jaminv.advancedmachines.objects.blocks.machine.expansion.IMachineUpgrade.UpgradeType;
import jaminv.advancedmachines.objects.blocks.machine.expansion.inventory.TileEntityMachineInventory;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.TileEntityMachineMultiblock;
import jaminv.advancedmachines.util.enums.EnumGui;
import jaminv.advancedmachines.util.helper.BlockHelper;
import jaminv.advancedmachines.util.interfaces.IHasTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class BlockMachineProductivity extends BlockMachineExpansionBase implements ITileEntityProvider, IHasTileEntity {
	
	public BlockMachineProductivity(String name) {
		super(name);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityMachineProductivity();
	}
	
	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return TileEntityMachineProductivity.class;
	}
	
	@Override
	public UpgradeType getUpgradeType() {
		return UpgradeType.PRODUCTIVITY;
	}	

	@Override
	public void setMultiblock(World world, BlockPos pos, BlockPos parent, MultiblockBorders borders) {
		super.setMultiblock(world, pos, parent, borders);
		TileEntity tileentity = world.getTileEntity(pos);
		if (tileentity instanceof TileEntityMachineProductivity) {
			TileEntityMachineProductivity te = (TileEntityMachineProductivity)tileentity;
			te.setBorders(borders);
		}
	}
}
