package jaminv.advancedmachines.objects.blocks.machine.expansion.redstone;

import javax.annotation.Nullable;

import jaminv.advancedmachines.objects.blocks.inventory.ContainerInventory;
import jaminv.advancedmachines.objects.blocks.inventory.TileEntityInventory;
import jaminv.advancedmachines.objects.blocks.machine.ContainerMachine;
import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.IMachineEnergy;
import jaminv.advancedmachines.objects.blocks.machine.MachineEnergyStorage;
import jaminv.advancedmachines.objects.blocks.machine.expansion.IMachineUpgradeTool;
import jaminv.advancedmachines.objects.blocks.machine.expansion.TileEntityMachineExpansion;
import jaminv.advancedmachines.objects.blocks.machine.instance.alloy.TileEntityMachineAlloy.GuiMachineAlloy;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.TileEntityMachineMultiblock;
import jaminv.advancedmachines.util.dialog.container.EmptyContainer;
import jaminv.advancedmachines.util.dialog.container.IContainerUpdate;
import jaminv.advancedmachines.util.dialog.container.IContainerUpdate;
import jaminv.advancedmachines.util.dialog.gui.GuiContainerObservable;
import jaminv.advancedmachines.util.dialog.gui.GuiScreenObservable;
import jaminv.advancedmachines.util.interfaces.IHasGui;
import jaminv.advancedmachines.util.interfaces.IRedstoneControlled;
import jaminv.advancedmachines.util.interfaces.IHasGui;
import jaminv.advancedmachines.util.recipe.IRecipeManager;
import jaminv.advancedmachines.util.recipe.machine.AlloyManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

public class TileEntityMachineRedstone extends TileEntityMachineExpansion implements IMachineUpgradeTool {
	
	protected EnumFacing facing = EnumFacing.NORTH;
	protected BlockPos parent;
	
	public void setFacing(EnumFacing facing) {
		this.facing = facing;
	}
	public EnumFacing getFacing() {
		return facing;
	}
	
	public TileEntityMachineRedstone() {
		super();
	}
	
	boolean redstone;
	public void checkRedstone() {
		boolean oldRedstone = redstone;
		redstone = world.isBlockPowered(pos);
		if (oldRedstone != redstone) {
			world.markBlockRangeForRenderUpdate(pos, pos);
		}
	}
	public boolean getRedstone() {
		return redstone;
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}
	
	@Override
	public void tickUpdate(TileEntityMachineMultiblock te) {
		te.setRedstone(redstone);
	}
	
	@Override
	public void setParent(BlockPos pos) {
		parent = pos;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("facing")) {
			facing = EnumFacing.byName(compound.getString("facing"));
		}
		if (compound.hasKey("redstone")) {
			redstone = compound.getBoolean("redstone");
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("facing", facing.getName());
		compound.setBoolean("redstone", redstone);
		return compound;
	}
	
	@Override
	public int getPriority() {
		return 0;
	}
}
