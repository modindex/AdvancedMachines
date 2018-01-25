package jaminv.advancedmachines.objects.blocks.machine.multiblock;

import jaminv.advancedmachines.objects.blocks.machine.BlockMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.TileEntityMachineMultiblock.MultiblockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public abstract class BlockMachineMultiblock extends BlockMachineBase {

	public BlockMachineMultiblock(String name) {
		super(name);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		
		//if (worldIn.isRemote) { return; }
		
		TileEntity te = worldIn.getTileEntity(pos);
		if (!(te instanceof TileEntityMachineMultiblock)) { return; }
		
		((TileEntityMachineMultiblock)te).scanMultiblock();
	}

}
