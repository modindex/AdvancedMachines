package jaminv.advancedmachines.objects.blocks.machine.multiblock;

import java.util.EnumMap;
import java.util.Map;

import javax.annotation.Nullable;
import javax.management.NotificationBroadcaster;

import jaminv.advancedmachines.objects.blocks.machine.expansion.IMachineUpgrade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

public class UpgradeManager implements INBTSerializable<NBTTagCompound> {
	protected Map<IMachineUpgrade.UpgradeType, Integer> upgrades;
	protected BlockPos inventoryInput = null, inventoryOutput = null, energy = null;
	
	protected void reset() {
		upgrades = new EnumMap<IMachineUpgrade.UpgradeType, Integer>(IMachineUpgrade.UpgradeType.class);
		inventoryInput = inventoryOutput = null;
	}
	
	protected void add(IMachineUpgrade.UpgradeType type, int add) {
		if (upgrades == null) { reset(); }
		Integer qty = upgrades.get(type);
		if (qty == null) { qty = 0; }
		upgrades.put(type, qty + add);
	}
	
	public int get(IMachineUpgrade.UpgradeType type) {
		if (upgrades == null) { return 0; }
		Integer qty = upgrades.get(type);
		if (qty == null) { return 0; }
		return qty;
	}
	
	public void addInventoryInput(BlockPos pos) {
		if (inventoryInput != null) { return; }
		inventoryInput = new BlockPos(pos);
	}
	
	public void addInventoryOutput(BlockPos pos) {
		if (inventoryOutput != null) { return; }
		inventoryOutput = new BlockPos(pos);
	}
	
	public void addEnergy(BlockPos pos) {
		if (energy != null) { return; }
		energy = new BlockPos(pos);
	}
	
	@Nullable public BlockPos getInventoryInput() { return inventoryInput; }
	@Nullable public BlockPos getInventoryOutput() { return inventoryOutput; }
	@Nullable public BlockPos getEnergy() { return energy; }
	
    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();

        if (upgrades != null) {
        	NBTTagList nbtTagList = new NBTTagList();
        	int count = 0;
        	for (IMachineUpgrade.UpgradeType type : IMachineUpgrade.UpgradeType.values()) {
        		Integer qty = upgrades.get(type);
        		if (qty != null) {
        			NBTTagCompound upgradeTag = new NBTTagCompound();
        			upgradeTag.setString("type", type.getName());
        			upgradeTag.setInteger("qty", qty);
        			nbtTagList.appendTag(upgradeTag);
        			count++;
        		}
            }

            nbt.setTag("upgrades", nbtTagList);
            nbt.setInteger("count", count);
        }
        if (inventoryInput != null) { nbt.setTag("inventoryInput", NBTUtil.createPosTag(inventoryInput)); }
        if (inventoryOutput != null) { nbt.setTag("inventoryOutput", NBTUtil.createPosTag(inventoryInput)); }
        if (energy != null) { nbt.setTag("energy", NBTUtil.createPosTag(energy)); }
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
    	this.reset();
    	if (nbt.hasKey("count")) {
    		int count = nbt.getInteger("count");
            NBTTagList tagList = nbt.getTagList("upgrades", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < count; i++) {
                NBTTagCompound upgradeTag = tagList.getCompoundTagAt(i);
                String type = upgradeTag.getString("type");
                int qty = upgradeTag.getInteger("qty");
                upgrades.put(IMachineUpgrade.UpgradeType.getType(type), qty);            	
            }
    	}
    	if (nbt.hasKey("inventoryInput")) {
    		inventoryInput = NBTUtil.getPosFromTag(nbt.getCompoundTag("inventoryInput"));
    	}
    	if (nbt.hasKey("inventoryOutput")) {
    		inventoryOutput = NBTUtil.getPosFromTag(nbt.getCompoundTag("inventoryOutput"));
    	}
    	if (nbt.hasKey("energy")) {
    		energy = NBTUtil.getPosFromTag(nbt.getCompoundTag("energy"));
    	}
    }
}
