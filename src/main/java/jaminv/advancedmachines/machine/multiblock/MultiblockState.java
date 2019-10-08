package jaminv.advancedmachines.machine.multiblock;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MultiblockState implements INBTSerializable<NBTTagCompound> {
	
	protected MultiblockMessage message = null;
	protected BlockPos multiblockMin = null, multiblockMax = null;
	protected MultiblockUpgrades upgrades = MultiblockUpgrades.createEmpty();
	protected boolean valid = false;	
	
	public boolean isValid() { return valid; }
	
	public MultiblockState()  {}
	
	public MultiblockState(MultiblockMessage message) {
		this.message = message;
	}
	
	public MultiblockState(MultiblockMessage message, MultiblockUpgrades upgrades,
			BlockPos multiblockMin, BlockPos multiblockMax) {
		
		this.message = message;
		this.upgrades = upgrades;
		this.multiblockMin = multiblockMin;
		this.multiblockMax = multiblockMax;
		this.valid = true;
	}
	
	/**
	 * Human-Readable Multi-block State Message
	 * 
	 * This is only available on the client. It will fail if called from the server.
	 * Further, it is not saved to NBT. If this method returns NULL, the MultiblockState should be re-built. 
	 * @return String - Human-readable multi-block state message, or NULL if this object needs to be refreshed.
	 */	
	@SideOnly (Side.CLIENT)
	public @Nullable MultiblockMessage getMessage() { return message; }
	
	public @Nullable BlockPos getMultiblockMin() { return multiblockMin; }
	public @Nullable BlockPos getMultiblockMax() { return multiblockMax; }
	public MultiblockUpgrades getUpgrades() { return upgrades; }


	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setBoolean("valid", valid);
		compound.setTag("upgrades",  upgrades.serializeNBT());
        if (multiblockMin != null) { compound.setTag("multiblockMin", NBTUtil.createPosTag(multiblockMin)); }
        if (multiblockMax != null) { compound.setTag("multiblockMax", NBTUtil.createPosTag(multiblockMax)); }
		return compound;
	}


	@Override
	public void deserializeNBT(NBTTagCompound compound) {
		valid = compound.getBoolean("valid");
		if (compound.hasKey("upgrades")) {
			upgrades.deserializeNBT(compound.getCompoundTag("upgrades"));
		}
    	if (compound.hasKey("multiblockMin")) {
    		multiblockMin = NBTUtil.getPosFromTag(compound.getCompoundTag("multiblockMin"));
    	}
    	if (compound.hasKey("multiblockMax")) {
    		multiblockMax = NBTUtil.getPosFromTag(compound.getCompoundTag("multiblockMax"));
    	}		
	}	
}