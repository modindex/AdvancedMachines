package jaminv.advancedmachines.objects.blocks.machine.expansion.inventory;

import javax.annotation.Nullable;

import jaminv.advancedmachines.objects.blocks.inventory.ContainerInventory;
import jaminv.advancedmachines.objects.blocks.inventory.GuiInventory;
import jaminv.advancedmachines.objects.blocks.inventory.TileEntityInventory;
import jaminv.advancedmachines.util.interfaces.IHasGui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;

public class TileEntityMachineInventory extends TileEntityInventory implements IHasGui {
	
	protected EnumFacing facing = EnumFacing.NORTH;
	protected boolean inputState = true;
	
	public void setFacing(EnumFacing facing) {
		this.facing = facing;
	}
	public EnumFacing getFacing() {
		return facing;
	}
	
	public boolean getInputState() {
		return inputState;
	}
	
	public final int SIZE = 27;
	@Override
	public int getInventorySize() {
		return SIZE;
	}
	
	private final DialogMachineInventory dialog = new DialogMachineInventory();
	
	@Override
	public ContainerInventory createContainer(IInventory inventory) {
		return new ContainerInventory(inventory, this, dialog);
	}
	
	@Override
	public GuiContainer createGui(IInventory inventory) {
		return new GuiInventory(createContainer(inventory), dialog);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("facing")) {
			facing = EnumFacing.byName(compound.getString("facing"));
		}
		if (compound.hasKey("inputState")) {
			inputState = compound.getBoolean("inputState");
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("facing", facing.getName());
		compound.setBoolean("inputState", inputState);
		
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
