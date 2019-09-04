package jaminv.advancedmachines.machine.expansion;

import javax.annotation.Nullable;

import jaminv.advancedmachines.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileMachineExpansion extends TileEntity implements MachineUpgradeTile, VariantExpansion.Needs {

	private VariantExpansion variant = VariantExpansion.BASIC;
	public VariantExpansion getVariant() { return variant; }
	public int getMultiplier() { return variant.getMultiplier(); }
	
	@Override
	public void setVariant(VariantExpansion variant) {
		this.variant = variant;
	}
	
	protected MultiblockBorders borders = new MultiblockBorders();
	
	public void setBorders(MultiblockBorders borders) {
		this.borders = borders;
		this.markDirty();
	}
	
	public MultiblockBorders getBorders() {
		return borders;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		setVariant(VariantExpansion.lookup(compound.getString("variant")));
		if (compound.hasKey("borders")) {
			borders.deserializeNBT(compound.getCompoundTag("borders"));
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("variant", variant.getName());
		compound.setTag("borders",  borders.serializeNBT());
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
