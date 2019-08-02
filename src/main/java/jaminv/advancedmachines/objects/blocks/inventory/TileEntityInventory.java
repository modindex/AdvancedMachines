package jaminv.advancedmachines.objects.blocks.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

public abstract class TileEntityInventory extends TileEntity implements ItemStackHandlerObservable.IObserver {

	public abstract int getInventorySize();
	
	protected ItemStackHandlerObservable inventory;
	
	public TileEntityInventory() {
		super();
		this.inventory = createInventory();
		this.inventory.addObserver(this);		
	}
	
	protected ItemStackHandlerObservable createInventory() {
		return new ItemStackHandlerObservable(getInventorySize());
	}
	
	public ItemStackHandlerObservable getInventory() {
		return inventory;
	}
	
	@Override
	public void onInventoryContentsChanged(int slot) {
		this.markDirty();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("items")) {
			inventory.deserializeNBT((NBTTagCompound)compound.getTag("items"));
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("items",  inventory.serializeNBT());
		return compound;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
		}
		return super.getCapability(capability, facing);
	}
	
	public boolean canInteractWith(EntityPlayer playerIn) {
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}	
}
