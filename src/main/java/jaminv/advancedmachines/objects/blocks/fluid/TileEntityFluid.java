package jaminv.advancedmachines.objects.blocks.fluid;

import javax.annotation.Nullable;

import jaminv.advancedmachines.objects.blocks.inventory.ItemStackHandlerObservable;
import jaminv.advancedmachines.objects.blocks.inventory.TileEntityInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

public abstract class TileEntityFluid extends TileEntityInventory implements FluidTankObservable.IObserver {

	@Override
	protected ItemStackHandlerObservable createInventory() {
		return new ItemStackHandlerObservable(getInventorySize()) {
			@Override
			protected int getStackLimit(int slot, ItemStack stack) {
				if (FluidUtil.getFluidHandler(stack) != null) {
					return 1;
				}
				return super.getStackLimit(slot, stack);
			}
		};
	}

	private FluidTankObservable tank;
	protected FluidTankObservable getTank() { return tank; }
	
	public abstract int getCapacity();
	
	public TileEntityFluid() {
		tank = new FluidTankObservable(getCapacity());
		tank.addObserver(this);
	}
	
	@Override
	public void onTankContentsChanged() {
		this.markDirty();
		world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 3);
	}

	public FluidStack getFluid() { return tank.getFluid(); }
	public int getFluidAmount() { return tank.getFluidAmount(); }

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
		}
		return super.getCapability(capability, facing);
	}
	
	public boolean canInteractWith(EntityPlayer playerIn) {
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}
	
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("fluid")) {
			tank.readFromNBT((NBTTagCompound)compound.getTag("fluid"));
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		
		NBTTagCompound fluid = new NBTTagCompound();
		tank.writeToNBT(fluid);
		compound.setTag("fluid", fluid);
		
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
    
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		handleUpdateTag(pkt.getNbtCompound());
	}	
}
