package jaminv.advancedmachines.objects.blocks.machine.expansion;

import java.util.HashMap;
import java.util.Map;

import jaminv.advancedmachines.objects.blocks.BlockMaterial;
import jaminv.advancedmachines.objects.blocks.machine.BlockMachineMultiblock;
import jaminv.advancedmachines.objects.blocks.machine.TileEntityMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.TileEntityMachineMultiblock;
import jaminv.advancedmachines.objects.items.material.MaterialBase;
import jaminv.advancedmachines.objects.items.material.PropertyMaterial;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class BlockMachineExpansion extends BlockMaterial {

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
		if (worldIn.isRemote) { return; }

		TileEntityMachineBase te = this.findController(worldIn, pos);

		TextComponentTranslation component;
		if (te == null) {
			component = new TextComponentTranslation("message.no_controller");
		} else {
			BlockPos tepos = te.getPos();
			component = new TextComponentTranslation("message.controller_found", te.getDisplayName(), tepos.getX(), tepos.getY(), tepos.getZ());
		}
		component.getStyle().setColor(TextFormatting.BLUE);
		placer.sendMessage(component);
	}	
	
	protected TileEntityMachineMultiblock findController(World world, BlockPos pos) {
		Map<BlockPos, Boolean> prev = new HashMap<>();
		BlockPos cur = pos;
		EnumFacing last = null;
		
		while(true) {
			prev.put(cur, true);
			
			boolean found = false;
			for(EnumFacing dir : EnumFacing.VALUES) {
				// Prevent backtracking
				if	(last != null && last.getAxis() == dir.getAxis() && last.getAxisDirection() != last.getAxisDirection()) {
					continue;
				}
				
				// Prevent searching too far
				if (Math.abs(pos.getX() - cur.getX()) > 3 || Math.abs(pos.getZ() - cur.getZ()) > 3 || Math.abs(pos.getY() - cur.getY()) > 2) {
					continue;
				}
				
				BlockPos next = cur.offset(dir);
				if (prev.get(next) != null) { continue; }	// We've already checked this block
				
				Block block = world.getBlockState(next).getBlock();
				if (block instanceof BlockMachineMultiblock) {
					TileEntity te = world.getTileEntity(next);
					if (te instanceof TileEntityMachineBase) {
						return (TileEntityMachineMultiblock)te;
					}
				}
				
				if (block instanceof BlockMachineExpansion) {
					cur = next;
					found = true;
					break;
				}
			}
			
			if (!found) { return null; }
		}
	}

}
