package jaminv.advancedmachines.objects.blocks.machine.expansion.expansion;

import javax.annotation.Nullable;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.objects.blocks.inventory.ContainerInventory;
import jaminv.advancedmachines.objects.blocks.inventory.TileEntityInventory;
import jaminv.advancedmachines.objects.blocks.machine.MachineEnergyStorage;
import jaminv.advancedmachines.objects.blocks.machine.expansion.BlockMachineExpansionBase;
import jaminv.advancedmachines.objects.blocks.machine.expansion.IMachineUpgradeTileEntity;
import jaminv.advancedmachines.objects.blocks.machine.expansion.IMachineUpgradeTool;
import jaminv.advancedmachines.objects.blocks.machine.expansion.TileEntityMachineExpansionBase;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.TileEntityMachineMultiblock;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.ICanHaveMachineFace;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.MachineFace;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.MachineParent;
import jaminv.advancedmachines.util.dialog.gui.GuiContainerObservable;
import jaminv.advancedmachines.util.helper.InventoryHelper;
import jaminv.advancedmachines.util.interfaces.IHasGui;
import jaminv.advancedmachines.util.interfaces.IRedstoneControlled.RedstoneState;
import jaminv.advancedmachines.util.recipe.IRecipeManager;
import jaminv.advancedmachines.util.recipe.RecipeInput;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityMachineExpansion extends TileEntityMachineExpansionBase implements ICanHaveMachineFace {
	
	protected MachineFace face = MachineFace.NONE;
	protected MachineParent parent = MachineParent.NONE;
	protected EnumFacing facing = EnumFacing.UP;
	protected BlockPos parentpos = null;
	protected boolean active = false;
	
	public void setMachineFace(MachineFace face, MachineParent parent, EnumFacing facing, BlockPos pos) {
		this.face = face;
		this.parent = parent;
		this.facing = facing;
		parentpos = pos;
	}
	
	@Override
	public void setActive(boolean active) {
		this.active = active;		
	}
	
	public boolean isActive() { return active; }

	public MachineFace getMachineFace() { return face; }
	public MachineParent getMachineParent() { return parent; }
	public EnumFacing getFacing() {	return facing; }
	public BlockPos getParentPos() { return parentpos; }

	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
    	if (compound.hasKey("face")) {
    		face = MachineFace.lookup(compound.getString("face"));
    	}
    	if (compound.hasKey("parent")) {
    		parent = MachineParent.lookup(compound.getString("parent"));
    	}
		if (compound.hasKey("facing")) {
			facing = EnumFacing.byName(compound.getString("facing"));
		}		
		if (compound.hasKey("parentpos")) {
			parentpos = NBTUtil.getPosFromTag(compound.getCompoundTag("parentpos"));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		compound.setString("face", face.getName());
		compound.setString("parent", parent.getName());
		compound.setString("facing", facing.getName());    			
		if (parentpos != null) {
			compound.setTag("parentpos", NBTUtil.createPosTag(parentpos));
		}

		return compound;
	}
	
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
    }
    
	public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }	
}
