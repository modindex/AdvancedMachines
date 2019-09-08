package jaminv.advancedmachines.machine.multiblock.face;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import jaminv.advancedmachines.lib.util.Variant;
import jaminv.advancedmachines.machine.BlockMachineMultiblock;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MachineFaceBuilder {
	public static @Nullable Pair<BlockPos, BlockPos> scanFace(World world, BlockPos parent, EnumFacing facing) {
		EnumFacing dir = facing.rotateAround(Axis.Y);
		for (int x = 0; x <= 2; x++) {
			for (int y = 0; y <= 2; y++) {
				BlockPos pos = parent.offset(dir, x).offset(EnumFacing.UP, y);
				if (scanFaceAt(world, parent, pos, 3, dir)) { return buildFace(world, parent, facing, pos, 3, dir); }
				if (x != 2 && y != 2 && scanFaceAt(world, parent, pos, 2, dir)) { return buildFace(world, parent, facing, pos, 2, dir); }				
			}
		}
		return null;
	}
	
	protected static boolean scanFaceAt(World world, BlockPos parent, BlockPos pos, int count, EnumFacing dir) {
		Block block = world.getBlockState(parent).getBlock();		
		Variant variant = null;
		if (block instanceof VariantExpansion.HasVariant) {
			variant = ((VariantExpansion.HasVariant)block).getVariant();
		}
		
		for (int x = -count; x < 2; x++) {
			for (int y = -count; y < 2; y++) {
				BlockPos check = pos.offset(dir, x).offset(EnumFacing.UP, y);
				
				boolean eval;
				TileEntity te = world.getTileEntity(check);
				
				if (te != null) {
					eval = (te instanceof MachineFaceTile);
					Block checkBlock = world.getBlockState(check).getBlock();
					if (eval) { eval = (checkBlock instanceof VariantExpansion.HasVariant) 
							&& ((VariantExpansion.HasVariant)checkBlock).getVariant() == variant; }
				} else { eval = false; }
				
				if (x == -count || y == -count || x == 1 || y == 1) {
					if (eval) { return false; }
				} else {
					if (!eval) { return false; }
				}
			}
		}
		return true;
	}
	
	protected static @Nullable Pair<BlockPos, BlockPos> buildFace(World world, BlockPos parent, EnumFacing facing, BlockPos pos, int count, EnumFacing dir) {
		int i = 0;
		Block block = world.getBlockState(parent).getBlock();
		if (!(block instanceof BlockMachineMultiblock)) { return null; }
		MachineType type = ((BlockMachineMultiblock)block).getMachineType();
		
		for (int x = -count+1; x < 1; x++) {
			for (int y = -count+1; y < 1; y++) {
				TileEntity te = world.getTileEntity(pos.offset(dir, x).offset(EnumFacing.UP, y));
				
				if (te instanceof MachineFaceTile) {
					((MachineFaceTile)te).setMachineFace(MachineFace.build(count, -x, -y), type, facing, pos);
				}
			}
		}
		
		return Pair.of(pos.offset(dir, -count+1).offset(EnumFacing.UP, -count+1), pos);
	}
}
