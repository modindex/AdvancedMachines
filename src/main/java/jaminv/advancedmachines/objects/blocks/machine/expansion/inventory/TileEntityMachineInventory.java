package jaminv.advancedmachines.objects.blocks.machine.expansion.inventory;

import javax.annotation.Nullable;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.objects.blocks.inventory.ContainerInventory;
import jaminv.advancedmachines.objects.blocks.inventory.TileEntityInventory;
import jaminv.advancedmachines.objects.blocks.machine.expansion.BlockMachineExpansionBase;
import jaminv.advancedmachines.objects.blocks.machine.expansion.IMachineUpgradeTool;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.TileEntityMachineMultiblock;
import jaminv.advancedmachines.objects.material.MaterialExpansion;
import jaminv.advancedmachines.util.helper.InventoryHelper;
import jaminv.advancedmachines.util.interfaces.IDirectional;
import jaminv.advancedmachines.util.interfaces.IHasGui;
import jaminv.advancedmachines.util.interfaces.IHasMetadata;
import jaminv.advancedmachines.util.interfaces.ISwitchableIO;
import jaminv.advancedmachines.util.message.IOStateMessage;
import jaminv.advancedmachines.util.recipe.IRecipeManager;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityMachineInventory extends TileEntityInventory implements IHasGui, IMachineUpgradeTool, IHasMetadata, IDirectional, ISwitchableIO {
	
	protected EnumFacing facing = EnumFacing.NORTH;
	protected boolean inputState = true;
	protected int priority = 0;
	protected MultiblockBorders borders = new MultiblockBorders();
	protected BlockPos parent = null;
	
	private MaterialExpansion material;
	
	@Override
	public void setMeta(int meta) {
		material = MaterialExpansion.byMetadata(meta);
	}

	@Override
	public ContainerInventory createContainer(IInventory inventory) {
		return new ContainerInventory(inventory, DialogMachineInventory.layout, this);
	}

	@Override
	public GuiContainer createGui(IInventory inventory) {
		return new DialogMachineInventory(createContainer(inventory), this);
	}	
	
	public void setFacing(EnumFacing facing) {
		this.facing = facing;
	}
	public EnumFacing getFacing() {
		return facing;
	}
	
	public boolean getInputState() {
		return inputState;
	}
	public void setInputState(boolean state) {
		this.inputState = state;
		BlockMachineExpansionBase.scanMultiblock(world, this.getPos());
		world.markBlockRangeForRenderUpdate(this.pos, this.pos);
		
		if (world.isRemote) {
			Main.NETWORK.sendToServer(new IOStateMessage(this.getPos(), state, priority));
		}
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
				
		if (world.isRemote) {
			Main.NETWORK.sendToServer(new IOStateMessage(this.getPos(), inputState, priority));
		}
		
		if (parent == null) { return; }
		TileEntity te = world.getTileEntity(parent);
		if (te instanceof TileEntityMachineMultiblock) {
			((TileEntityMachineMultiblock)te).sortTools();
		}
	}
	
	@Override
	public void setParent(BlockPos pos) {
		parent = pos;		
	}	
	
	@Override
	public int getPriority() {
		return priority;
	}
	
	@Override
	public void onInventoryContentsChanged(int slot) {
		super.onInventoryContentsChanged(slot);
		
		if (parent == null) { return; }
		TileEntity te = world.getTileEntity(parent);
		if (te instanceof TileEntityMachineMultiblock) {
			((TileEntityMachineMultiblock)te).doSomething();
		}
	}

	@Override
	public boolean tickUpdate(TileEntityMachineMultiblock te) {
		if (inputState) {
			return moveInput(te);
		} else {
			return moveOutput(te);
		}
	}
	
	protected boolean moveInput(TileEntityMachineMultiblock te) {
		boolean didSomething = false;
 		ItemStackHandler inv = te.getInventory();
		IRecipeManager recipe = te.getRecipeManager();

		ItemStack[] input = new ItemStack[te.getInputCount()];
		for (int i = te.getFirstInputSlot(); i < te.getInputCount() + te.getFirstInputSlot(); i++) {
			input[i - te.getFirstInputSlot()] = inv.getStackInSlot(i);
		}
		
		for (int i = te.getFirstInputSlot(); i < te.getInputCount() + te.getFirstInputSlot(); i++) {
			ItemStack item = inv.getStackInSlot(i);
			if (item.isEmpty()) {
				for (int d = 0; d < inventory.getSlots(); d++) {
					ItemStack other = inventory.getStackInSlot(d);
					if (!other.isEmpty() && recipe.isItemValid(other, te.getInput())) {
						inventory.extractItem(d, other.getCount(), false);
						inv.insertItem(i, other, false);
						didSomething = true;
						break;
					}
				}
			}
			
			for (int d = 0; d < inventory.getSlots(); d++) {
				ItemStack other = inventory.getStackInSlot(d);				
				ItemStack newother = InventoryHelper.pushStack(other, inv, i, i, false);
				if (newother.getCount() != other.getCount()) { 
					didSomething = true;
					inv.setStackInSlot(d, newother);
				}
			}
		}
		
		return didSomething;
	}
	
	protected boolean moveOutput(TileEntityMachineMultiblock te) {
		boolean didSomething = false;
		ItemStackHandler inv = te.getInventory();
		
		for (int i = te.getFirstOutputSlot(); i < te.getOutputCount() + te.getFirstOutputSlot(); i++) {
			ItemStack other = inv.getStackInSlot(i);
			ItemStack newother = InventoryHelper.pushStack(other, inventory);
			if (newother.getCount() != other.getCount()) {
				didSomething = true;
				inv.setStackInSlot(i, newother);
			}
		}
		
		for (int i = te.getFirstSecondarySlot(); i < te.getSecondaryCount() + te.getFirstSecondarySlot(); i++) {
			ItemStack other = inv.getStackInSlot(i);
			ItemStack newother = InventoryHelper.pushStack(inv.getStackInSlot(i), inventory);
			if (newother.getCount() != other.getCount()) {
				didSomething = true;
				inv.setStackInSlot(i, newother);
			}
		}
		
		return didSomething;
	}
	
	
	public void setBorders(World world, MultiblockBorders borders) {
		this.borders = borders;
	}
	
	public MultiblockBorders getBorders() {
		return borders; 
	}	
	
	public final int SIZE = 27;
	@Override
	public int getInventorySize() {
		return SIZE;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("meta")) {
			setMeta(compound.getInteger("meta"));
		}
		if (compound.hasKey("facing")) {
			facing = EnumFacing.byName(compound.getString("facing"));
		}
		if (compound.hasKey("inputState")) {
			inputState = compound.getBoolean("inputState");
		}
		if (compound.hasKey("priority")) {
			priority = compound.getInteger("priority");
		}
		if (compound.hasKey("borders")) {
			borders.deserializeNBT(compound.getCompoundTag("borders"));
		}
		if (compound.hasKey("parent")) {
			parent = NBTUtil.getPosFromTag(compound.getCompoundTag("parent"));
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("meta", material.getMeta());
		compound.setString("facing", facing.getName());
		compound.setBoolean("inputState", inputState);
		compound.setInteger("priority", priority);
		compound.setTag("borders",  borders.serializeNBT());	
		if (parent != null) {
			compound.setTag("parent", NBTUtil.createPosTag(parent));
		}
		return compound;
	}
	
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
    }

    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }	
}
