package jaminv.advancedmachines.objects.blocks.machine.expansion;

import javax.annotation.Nullable;

import jaminv.advancedmachines.objects.blocks.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.objects.material.MaterialExpansion;
import jaminv.advancedmachines.util.interfaces.IHasMetadata;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityMachineExpansionBase extends TileEntity implements IMachineUpgradeTileEntity, IHasMetadata {

	private MaterialExpansion material = MaterialExpansion.BASIC;
	public MaterialExpansion getMaterial() { return material; }
	public int getMultiplier() { return material.getMultiplier(); }
	
	@Override
	public void setMeta(int meta) {
		material = MaterialExpansion.byMetadata(meta);
	}
	
	public TileEntityMachineExpansionBase() {
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
