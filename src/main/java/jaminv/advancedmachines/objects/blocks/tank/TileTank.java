package jaminv.advancedmachines.objects.blocks.tank;

import javax.annotation.Nullable;

import jaminv.advancedmachines.ModConfig;
import jaminv.advancedmachines.lib.dialog.fluid.DialogBucketToggle;
import jaminv.advancedmachines.lib.fluid.BucketHandler;
import jaminv.advancedmachines.lib.fluid.FluidObservable;
import jaminv.advancedmachines.lib.fluid.FluidTank;
import jaminv.advancedmachines.lib.fluid.FluidTankDefault;
import jaminv.advancedmachines.lib.util.blocks.HasItemNBT;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class TileTank extends TileEntity implements HasItemNBT, DialogBucketToggle.Provider, FluidObservable.Observer {
	
	protected BucketHandler bucketHandler = new BucketHandler();
	
	public BucketHandler getBucketToggle() { return bucketHandler; }
	
	protected FluidTankDefault tank = new FluidTankDefault(ModConfig.general.defaultMachineFluidCapacity,
		ModConfig.general.defaultMachineFluidTransfer);
	public FluidTank getTank() { return tank; }
	
	public TileTank() {
		super();
		tank.addObserver(this);
	}

	public boolean onBlockActivated(EntityPlayer player, EnumHand hand) {
		return bucketHandler.onBlockActivate(player, hand, tank);
	}
	
	@Override
	public void onTankContentsChanged() {
		markDirty();
		world.markBlockRangeForRenderUpdate(pos, pos);
	}

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
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("tank")) {
			tank.deserializeNBT(compound.getCompoundTag("tank"));
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("tank", tank.serializeNBT());
		return compound;
	}

	@Override
	public void readItemNBT(NBTTagCompound compound) {
		if (compound.hasKey("tank")) {
			tank.deserializeNBT(compound.getCompoundTag("tank"));
		}	
	}

	@Override
	public NBTTagCompound writeItemNBT(NBTTagCompound compound) {
		compound.setTag("tank", tank.serializeNBT());
		return compound;
	}
	
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
    }

    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }
    
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		handleUpdateTag(pkt.getNbtCompound());
	}	
}
