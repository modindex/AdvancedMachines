package jaminv.advancedmachines.machine.expansion;

import javax.annotation.Nullable;

import jaminv.advancedmachines.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import jaminv.advancedmachines.util.interfaces.IHasMetadata;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityMachineExpansionType extends TileEntity implements MachineUpgradeTileEntity, IHasMetadata {

	private VariantExpansion material = VariantExpansion.BASIC;
	public VariantExpansion getMaterial() { return material; }
	public int getMultiplier() { return material.getMultiplier(); }
	
	@Override
	public void setMeta(int meta) {
		material = VariantExpansion.byMetadata(meta);
	}
	
	public TileEntityMachineExpansionType() {
		super();
	}
	
	protected MultiblockBorders borders = new MultiblockBorders();
	
	public void setBorders(World world, MultiblockBorders borders) {
		this.borders = borders;
	}
	
	public MultiblockBorders getBorders() {
		return borders; 
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		setMeta(compound.getInteger("meta"));
		if (compound.hasKey("borders")) {
			borders.deserializeNBT(compound.getCompoundTag("borders"));
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("meta", material.getMeta());
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
